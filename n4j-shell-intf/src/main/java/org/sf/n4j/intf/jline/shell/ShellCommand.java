package org.sf.n4j.intf.jline.shell;

import lombok.Value;
import org.jline.console.CommandInput;

import java.util.function.Function;

@Value
public class ShellCommand {

    private String name;
    private String info;
    private Function<CommandInput,?> executeFunction;

}
