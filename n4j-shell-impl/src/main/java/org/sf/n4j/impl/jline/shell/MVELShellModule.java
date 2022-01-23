package org.sf.n4j.impl.jline.shell;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.multibindings.MapBinder;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.multibindings.ProvidesIntoMap;
import com.google.inject.multibindings.StringMapKey;
import org.jline.console.CommandRegistry;
import org.sf.n4j.intf.jline.shell.InteractiveShell;
import org.sf.n4j.intf.jline.shell.annotations.ShellBuiltInScript;
import org.sf.n4j.intf.jline.shell.annotations.ShellCommandRegistry;
import org.sf.n4j.intf.jline.shell.annotations.ShellSystemVar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.util.Map;
import java.util.Set;

public class MVELShellModule extends AbstractModule {

    @Override
    protected void configure() {
        MapBinder.newMapBinder(binder(),String.class,Object.class,ShellSystemVar.class);
        Multibinder.newSetBinder(binder(), CommandRegistry.class, ShellCommandRegistry.class);
        Multibinder.newSetBinder(binder(),String.class, ShellBuiltInScript.class);
    }

    @Provides
    @Singleton
    public org.jline.console.ScriptEngine getJLineScriptEngine(@ShellSystemVar Map<String,Object> systemVarsMap){
        return new MVELScriptEngine(systemVarsMap);
    }

    @ProvidesIntoMap
    @ShellSystemVar
    @StringMapKey("LOG")
    public Object getShellLogger(){
        Logger logger = LoggerFactory.getLogger("org.sf.n4j.jline.shell");
        return logger;
    }

    @Provides
    @Singleton
    public InteractiveShell getInteractiveShell(
            @ShellCommandRegistry Set<CommandRegistry> commandRegistries,
            @ShellBuiltInScript Set<String> shellBuiltInScripts,
            org.jline.console.ScriptEngine jlineScriptEngine){
        return new MVELInteractiveShell(commandRegistries,jlineScriptEngine,shellBuiltInScripts);
    }
}
