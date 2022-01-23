package org.sf.n4j.examples.two;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

import javax.inject.Singleton;

public class ExampleTwoModule extends AbstractModule {

    @Provides
    @Singleton
    public SomeService getSomeService(){
        return new SomeServiceImpl();
    }
}
