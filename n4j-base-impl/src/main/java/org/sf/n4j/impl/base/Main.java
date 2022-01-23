package org.sf.n4j.impl.base;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import lombok.extern.slf4j.Slf4j;
import org.sf.n4j.intf.base.ConfigProvider;
import org.sf.n4j.config.ts.TypeSafeConfigProviderFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class Main {

    private static ConcurrentHashMap<String, Optional<Injector>> memoizedInjectMap = new ConcurrentHashMap<>();


    public static Optional<Injector> inject(final Class appMainClass, final String[] progArgs) {
        return memoizedInjectMap.computeIfAbsent(appMainClass.getName(), className -> {
            return _inject(appMainClass, progArgs);
        });
    }

    private static Optional<Injector> _inject(Class appMainClass, String[] progArgs) {
        Module[] modulesDefinedByApp = new Module[]{};
        try {
            modulesDefinedByApp = getModulesDefinedByApp(appMainClass);
        } catch (NoSuchMethodException noSuchMethodException) {
            log.trace("No method '{}' defined in class '{}'", "modules", appMainClass.getName());
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            System.exit(1);
        }

        for (Module module : modulesDefinedByApp) {
            log.trace("Module : {}", module.getClass().getName());
        }

        List<Module> finalListOfModules = getFinalListOfModules(appMainClass,
                TypeSafeConfigProviderFactory.load(),
                progArgs,
                modulesDefinedByApp);

        log.trace("Finalized List of Modules ...");
        for (Module module : finalListOfModules) {
            log.trace("Module : {}", module.getClass().getName());
        }

        Injector injector = Guice.createInjector(finalListOfModules);

        return Optional.ofNullable(injector);

    }

    public static void main(Map<String, Object> mapArgs) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        String[] progArgs = (String[]) mapArgs.get("args");
        String actualMainClass = (String) mapArgs.get("App-Main-Class");
        log.trace("Main Class to Invoke : {}, Args : {}", actualMainClass, progArgs);
        Class<?> appMainClass = Main.class.getClassLoader().loadClass(actualMainClass);
        Optional<Injector> optionalInjector = inject(appMainClass, progArgs);
        invokeMain(optionalInjector.get(), appMainClass, progArgs);
    }

    private static void invokeMain(Injector injector, Class<?> appMainClass, String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method modulesMethod = appMainClass.getMethod("main", new Class[]{String[].class});
        modulesMethod.invoke(null, (Object) args);
    }

    private static List<Module> getFinalListOfModules(Class appMainClass,
                                                      ConfigProvider configProvider,
                                                      String[] args,
                                                      Module[] modulesDefinedByApp) {
        List<Module> finalListOfModules = new ArrayList<>();
        finalListOfModules.addAll(Arrays.asList(modulesDefinedByApp));
        finalListOfModules.add(new BaseModule(configProvider, args, appMainClass));
        return finalListOfModules;
    }

    private static Module[] getModulesDefinedByApp(Class appMainClass) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method modulesMethod = appMainClass.getMethod("modules", new Class[]{});
        if(modulesMethod != null) {
            Object modulesArray = modulesMethod.invoke(null, new Object[]{});
            return (Module[]) modulesArray;
        }else{
            log.warn("No public static modules() method defined for class : {}",appMainClass.getName());
            return new Module[]{};//as good as no modules
        }
    }
}
