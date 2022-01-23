package org.sf.n4j.examples.one;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import org.sf.n4j.intf.base.ConfigProvider;
import org.sf.n4j.intf.base.annotations.Config;

import javax.inject.Named;
import javax.inject.Singleton;

public class ExampleOneModule extends AbstractModule {

    public static final String CONFIG_KEY_PATH = "example.one.config";

    @Provides
    @Singleton
    @Named("ExampleOneConfig")
    public ConfigProvider getExampleOneConfig(@Config ConfigProvider appConfigProvider){
        ConfigProvider exampleOneConfigProvider = appConfigProvider.getProviderForConfigPath("example.one.config");
        return exampleOneConfigProvider;
    }

   /* @ProvidesIntoSet
    @EventListener
    public Object getTestEventListener(){
        return new TestEventListener();
    }*/
}
