package org.sf.n4j.examples.two;

import com.google.inject.Injector;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import static org.sf.n4j.impl.base.Main.inject;
import com.google.inject.Module;

@Slf4j
public class AppMain {

    public static Module[] modules(){
        return new Module[]{
          new ExampleTwoModule()
        };
    }

    @Inject
    static SomeService someService;

    @Inject
    static Injector appInjector;

    public static void main(String[] args) {

        inject(AppMain.class,args);

        log.info("Starting App with App Injector {} and args []",appInjector,args);
        someService.start();
    }
}
