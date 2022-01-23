package org.sf.n4j.impl.jline.shell;

import com.google.common.collect.ImmutableMap;
import lombok.NonNull;
import org.jline.builtins.ConfigurationPath;
import org.jline.console.CommandRegistry;
import org.jline.console.ConsoleEngine;
import org.jline.console.Printer;
import org.jline.console.ScriptEngine;
import org.jline.console.impl.Builtins;
import org.jline.console.impl.DefaultPrinter;
import org.jline.console.impl.SystemRegistryImpl;
import org.jline.keymap.KeyMap;
import org.jline.reader.*;
import org.jline.reader.impl.DefaultParser;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.OSUtils;
import org.jline.widget.TailTipWidgets;
import org.jline.widget.Widgets;
import org.sf.n4j.intf.base.diagnostics.FailureAnalysis;
import org.sf.n4j.intf.jline.shell.InteractiveShell;
import org.sf.n4j.intf.jline.shell.InteractiveShellException;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Supplier;

class MVELInteractiveShell implements InteractiveShell {

    private final Set<CommandRegistry> commandRegistries;
    private final ScriptEngine jlineScriptEngine;
    private final Set<String> builtInScripts;


    MVELInteractiveShell(@NonNull Set<CommandRegistry> commandRegistries,
                         @NonNull org.jline.console.ScriptEngine jlineScriptEngine,
                         @NonNull Set<String> builtInScripts){
        this.commandRegistries = commandRegistries;
        this.jlineScriptEngine = jlineScriptEngine;
        this.builtInScripts = builtInScripts;
    }


    @Override
    public void run(String[] args) {
        try {
            Supplier<Path> workDir = () -> Paths.get(System.getProperty("user.dir"));

            //
            // Parser & Terminal
            //
            DefaultParser parser = new DefaultParser();
            parser.setEofOnUnclosedBracket(DefaultParser.Bracket.CURLY, DefaultParser.Bracket.ROUND, DefaultParser.Bracket.SQUARE);
            parser.setEofOnUnclosedQuote(true);
            parser.setEscapeChars(null);
            parser.setRegexVariable(null);                          // we do not have console variables!
            Terminal terminal = TerminalBuilder.builder().build();
            Thread executeThread = Thread.currentThread();
            terminal.handle(Terminal.Signal.INT, signal -> executeThread.interrupt());

            //
            // Command registries
            //
            File file = new File(workDir.get().toUri());
            String root = file.getCanonicalPath();
            ConfigurationPath configPath = new ConfigurationPath(Paths.get(root), Paths.get(root));
            Printer printer = new DefaultPrinter(jlineScriptEngine, configPath);
            ConsoleEngineImpl consoleEngine = new ConsoleEngineImpl(jlineScriptEngine
                    , printer
                    , workDir, configPath);
            Set<Builtins.Command> commands = new HashSet<>(Arrays.asList(Builtins.Command.values()));
            commands.remove(Builtins.Command.TTOP);                          // ttop command is not supported in GraalVM
            Builtins builtins = new Builtins(commands, workDir, configPath, (String fun) -> new ConsoleEngine.WidgetCreator(consoleEngine, fun));
            SystemRegistryImpl systemRegistry = new SystemRegistryImpl(parser, terminal, workDir, configPath);
            CommandRegistry[] allCommandRegistries = createCommandRegistryArray(consoleEngine, builtins, commandRegistries);
            systemRegistry.setCommandRegistries(allCommandRegistries);
            systemRegistry.addCompleter(jlineScriptEngine.getScriptCompleter());

            //
            // LineReader
            //
            LineReader reader = LineReaderBuilder.builder()
                    .terminal(terminal)
                    .completer(systemRegistry.completer())
                    .parser(parser)
                    .variable(LineReader.SECONDARY_PROMPT_PATTERN, "%M%P > ")
                    .variable(LineReader.INDENTATION, 2)
                    .variable(LineReader.LIST_MAX, 100)
                    .variable(LineReader.HISTORY_FILE, Paths.get(root, "history"))
                    .option(LineReader.Option.INSERT_BRACKET, true)
                    .option(LineReader.Option.EMPTY_WORD_OPTIONS, false)
                    .option(LineReader.Option.USE_FORWARD_SLASH, true)             // use forward slash in directory separator
                    .option(LineReader.Option.DISABLE_EVENT_EXPANSION, true)
                    .build();
            if (OSUtils.IS_WINDOWS) {
                reader.setVariable(LineReader.BLINK_MATCHING_PAREN, 0); // if enabled cursor remains in begin parenthesis (gitbash)
            }

            //
            // complete command registries
            //
            consoleEngine.setLineReader(reader);
            builtins.setLineReader(reader);

            //
            // widgets and console initialization
            //
            new TailTipWidgets(reader, systemRegistry::commandDescription, 5, TailTipWidgets.TipType.COMPLETER);
            KeyMap<Binding> keyMap = reader.getKeyMaps().get("main");
            keyMap.bind(new Reference(Widgets.TAILTIP_TOGGLE), KeyMap.alt("s"));

            loadBuiltIns(jlineScriptEngine,"/builtins/std_builtin_functions.mvel");

            loadOtherBuiltins(jlineScriptEngine,builtInScripts);

            //
            // REPL-loop
            //
            //System.out.println(terminal.getName() + ": " + terminal.getType());
            while (true) {
                try {
                    systemRegistry.cleanUp();            // reset output streams
                    String line = reader.readLine("idb> ");
                    Object result = systemRegistry.execute(line);
                    if (result != null) {
                        System.out.println(result);
                    }
                } catch (UserInterruptException e) {
                    // Ignore
                } catch (EndOfFileException e) {
                    break;
                } catch (Exception e) {
                    systemRegistry.trace(true, e);    // print exception
                }
            }
            systemRegistry.close();
        }catch (Exception e){
            e.printStackTrace();
            Map<String,Object> failureContext = ImmutableMap.of(
                    FailureAnalysis.WHERE, "JLine Interactive Shell start",
                    FailureAnalysis.ACTION, "Check the Interactive Shell configuration",
                    FailureAnalysis.STATE_MESSAGE,e.getMessage(),
                    Exception.class.getName(),
                    e
            );
            throw new InteractiveShellException(e,"shell.mvel.err.00001",failureContext);
        }
    }


    private CommandRegistry[] createCommandRegistryArray(ConsoleEngineImpl consoleEngine,
                                                         Builtins builtins,
                                                         Set<CommandRegistry> commandRegistries) {
        List<CommandRegistry> allCommandRegistries = new ArrayList<>();

        //add the builtin and the script engine command registries
        allCommandRegistries.add(consoleEngine);
        allCommandRegistries.add(builtins);

        //add the ones provided by the other modules
        allCommandRegistries.addAll(commandRegistries);

        return allCommandRegistries.toArray(new CommandRegistry[0]);
    }

    private void loadOtherBuiltins(ScriptEngine jlineScriptEngine, Set<String> builtInScripts) throws Exception {
        for (String builtInScript : builtInScripts) {
            loadBuiltIns(jlineScriptEngine,builtInScript);
        }
    }

    private void loadBuiltIns(ScriptEngine jlineScriptEngine, String scriptPath) throws Exception {
        try(BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(scriptPath)))){
            Optional<String> fullScriptOptional = bufferedReader.lines().reduce((accumulatedString, newLine) -> {
                return accumulatedString + System.lineSeparator() + newLine;
            });
            jlineScriptEngine.execute(fullScriptOptional.get());
        }
    }

}
