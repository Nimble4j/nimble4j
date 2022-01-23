package org.sf.n4j.examples.one;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.name.Names;
import lombok.extern.slf4j.Slf4j;
import org.sf.n4j.impl.base.BaseModule;
import org.sf.n4j.intf.base.ConfigProvider;
import org.sf.n4j.intf.base.annotations.EventBus;
import org.sf.n4j.config.ts.TypeSafeConfigProviderFactory;


@Slf4j
public class Main {

    public static void main(String[] args) {
        ConfigProvider appConfig = TypeSafeConfigProviderFactory.load();
        Injector injector = Guice.createInjector(
                new BaseModule(appConfig,args),
                new ExampleOneModule());

        ConfigProvider exampleOneConfigProvider = injector.getInstance(Key.get(ConfigProvider.class, Names.named("ExampleOneConfig")));

        ExampleOneConfig exampleOneConfig = exampleOneConfigProvider.bindTo(ExampleOneConfig.class);

        log.info("Starting with Config {}",exampleOneConfig);
        com.google.common.eventbus.EventBus eventBus = injector.getInstance(Key.get(com.google.common.eventbus.EventBus.class, EventBus.class));
        log.info("EventBus : {}",eventBus);
        com.google.common.eventbus.EventBus eventBus2 = injector.getInstance(Key.get(com.google.common.eventbus.EventBus.class, EventBus.class));
        log.info("EventBus2 : {}",eventBus2);
        if(eventBus == eventBus2){
            log.info("Singleton eventbus");
        }else{
            log.info("Non-Singleton evenbus");
        }

        eventBus.post("EVENT_ONE");
        eventBus.post("EVENT_TWO");

    }
}
