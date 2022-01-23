package org.sf.n4j.impl.jline.shell;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.jline.console.ScriptEngine;
import org.jline.reader.Completer;
import org.jline.reader.impl.completer.AggregateCompleter;
import org.mvel2.jsr223.MvelBindings;

import javax.script.ScriptEngineManager;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@Slf4j
class MVELScriptEngine implements ScriptEngine {

    public enum Format {JSON, MVEL, NONE}

    protected MvelBindings bindings;
    //protected MvelBindings globalBindings;
    protected javax.script.ScriptEngine mvelScriptEngine;
    private ObjectMapper objectMapper;
    private Set<String> systemVars;
    private Map<String,Object> systemVarMap;
    private static final String REGEX_SYSTEM_VAR = "[A-Z]+[A-Z_]*";

    public MVELScriptEngine(@NonNull Map<String,Object> systemVarsMap){
        bindings = new MvelBindings();
        //globalBindings = new MvelBindings();
        objectMapper = new ObjectMapper();
        ScriptEngineManager manager = new ScriptEngineManager();
        this.mvelScriptEngine = manager.getEngineByName("mvel");
        this.systemVarMap = systemVarsMap;
        this.systemVars = systemVarsMap.keySet();
        initSystemBindings(bindings,systemVarsMap);
    }


    private void initSystemBindings(MvelBindings bindings,Map<String,Object> systemVarMap) {
        systemVarMap.forEach((k,v)->{
            bindings.put(k,v);
        });
    }

    @Override
    public String getEngineName() {
        return "MVEL";
    }

    @Override
    public Collection<String> getExtensions() {
        return Arrays.asList("mvel");
    }

    @Override
    public Completer getScriptCompleter() {
        List<Completer> completers = new ArrayList<>();
        return new AggregateCompleter(completers);
    }

    @Override
    public boolean hasVariable(String variable) {
        return bindings.containsKey(variable);
    }

    @Override
    public void put(String variable, Object value) {
        //if it is a system var then it cannot be removed or changed
        if(systemVars.contains(variable)){
            return;
        }
        //log.debug("Put Variable : {},{}",variable,value);
        bindings.put(variable,value);
    }

    @Override
    public Object get(String variable) {
        return bindings.get(variable);
    }

    @Override
    public Map<String, Object> find(String name) {
        Map<String, Object> out = new HashMap<>();
        if (name == null) {
            out = bindings;
        } else {
            for (String v : internalFind(name)) {
                out.put(v, get(v));
            }
        }
        return out;
    }

    private List<String> internalFind(String var) {
        List<String> out = new ArrayList<>();
        if(!var.contains(".") && var.contains("*")) {
            var = var.replaceAll("\\*", ".*");
        }
        for (String v :  (Set<String>)bindings.keySet()) {
            if (v.matches(var)) {
                out.add(v);
            }
        }
        return out;
    }

    @Override
    public void del(String... vars) {
        if (vars == null) {
            return;
        }
        for (String variable: vars) {
            _del(variable);
        }
    }

    private void _del(String variable){
        if (variable == null) {
            return;
        }

        //if it is a system var then it cannot be removed or changed
        if(systemVars.contains(variable)){
            return;
        }

        //check in script bindings to remove
        if(bindings.containsKey(variable)){
            bindings.remove(variable);
        }else if (!variable.contains(".") && variable.contains("*")) {
            for (String v : internalFind(variable)){
                if (bindings.containsKey(v) && !v.equals("_") && !v.matches(REGEX_SYSTEM_VAR)) {
                    bindings.remove(v);
                }
            }
        }

    }

    @Override
    public String toJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString(Object obj) {
        if(obj != null){
            return obj.toString();
        }
        return "<null>";
    }

    @Override
    public Map<String, Object> toMap(Object obj) {
        if(obj != null){
            Map<String,Object> toMapResult = new HashMap<>();
            toMapResult.put(obj.getClass().getSimpleName(),obj.toString());
            return toMapResult;
        }
        return null;
    }

    @Override
    public Object deserialize(String value, String formatStr) {
        Object out = value;
        Format format = formatStr != null && !formatStr.isEmpty() ? Format.valueOf(formatStr.toUpperCase()) : null;
        if (format == Format.NONE) {
            // do nothing
        } else if (format == Format.JSON) {
            try {
                if(Strings.isNullOrEmpty(value)){
                    out = new HashMap<String,String>();
                }else {
                    out = objectMapper.reader().readValue(value);
                }
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        } else if (format == Format.MVEL) {
            try {
                out = execute(value);
            } catch (Exception e) {
                throw new IllegalArgumentException(e.getMessage());
            }
        }
        return out;
    }

    @Override
    public List<String> getSerializationFormats() {
        return Arrays.asList(Format.JSON.toString(), Format.NONE.toString());
    }

    @Override
    public List<String> getDeserializationFormats() {
        return Arrays.asList(Format.JSON.toString(), Format.MVEL.toString(), Format.NONE.toString());
    }

    @Override
    public void persist(Path path, Object o) {
        log.info("Persist called with Path : {} and object : {}",path,o);
        //throw new RuntimeException("This Capability is not yet implemented");
    }

    @Override
    public void persist(Path path, Object o, String s) {
        throw new RuntimeException("This Capability is not yet implemented");
    }

    @Override
    public Object execute(String script) throws Exception {
        log.debug("Execute : {}",script);
        Object output = mvelScriptEngine.eval(script,bindings);
        return output;
    }

    @Override
    public Object execute(File file, Object[] objects) throws Exception {
        String scriptToExecute = Files.readString(file.toPath());
        //MvelCompiledScript compiledScript = new MvelCompiledScript((MvelScriptEngine)this.mvelScriptEngine,scriptToExecute);
        //Object result = compiledScript.eval(bindings);
        Object result = execute(scriptToExecute);
        return result;
    }

    @Override
    public Object execute(Object o, Object... objects) {
        log.debug("Execute called with object : {} and values : {}",o,objects);
        throw new RuntimeException("This Capability is not yet implemented");
    }
}
