package org.sf.n4j.servlet.container.examples.one;

import com.google.inject.Module;
import lombok.extern.slf4j.Slf4j;
import org.sf.n4j.impl.servlet.container.JettyServletContainerModule;

import javax.inject.Inject;

import static org.sf.n4j.impl.base.Main.inject;

@Slf4j
public class ExampleOneMain {

    public static Module[] modules(){
        return new Module[]{
                new ExampleServletModule(),
                new JettyServletContainerModule()
        };
    }

    @Inject
    static ExampleOneStarter exampleOneStarter;

    public static void main(String[] args) {

        log.info("Starting Example One Main...");

        inject(ExampleOneMain.class,args);

        exampleOneStarter.run();
    }
}
