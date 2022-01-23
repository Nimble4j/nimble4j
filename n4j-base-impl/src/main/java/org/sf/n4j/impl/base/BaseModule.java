package org.sf.n4j.impl.base;

import com.google.common.collect.ImmutableList;
import com.google.inject.AbstractModule;
import com.google.inject.Key;
import com.google.inject.Provides;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.Multibinder;
import lombok.NonNull;
import org.sf.n4j.intf.base.ExceptionReporter;
import org.sf.n4j.intf.base.annotations.Args;
import org.sf.n4j.intf.base.annotations.Config;
import org.sf.n4j.intf.base.annotations.EventBus;
import org.sf.n4j.intf.base.ConfigProvider;
import org.sf.n4j.intf.base.annotations.EventListener;
import org.sf.n4j.intf.base.diagnostics.FailureAnalysisReporter;
import org.sf.n4j.intf.base.diagnostics.FailureAnalyzer;

import javax.inject.Singleton;
import java.util.List;
import java.util.Set;

public class BaseModule extends AbstractModule {

    public static final String APP_EVENTBUS_NAME = "APP_EVENT_BUS";

    private final ConfigProvider appConfigProvider;
    private final String[] arguments;
    private Class appMainClass;

    public BaseModule(@NonNull ConfigProvider appConfigProvider,String[] args){
        this.appConfigProvider = appConfigProvider;
        this.arguments = args;
    }

    public BaseModule(@NonNull ConfigProvider appConfigProvider,String[] args,Class appMainClass){
        this.appConfigProvider = appConfigProvider;
        this.arguments = args;
        this.appMainClass = appMainClass;
    }

    @Override
    protected void configure() {
        super.configure();

        //register the set binding for Event listeners
        Multibinder.newSetBinder(binder(), Key.get(Object.class, EventListener.class));

        //register the set binding for FailureAnalyzer
        Multibinder.newSetBinder(binder(),FailureAnalyzer.class);

        //bind the program arguments to Args annotation so that it is available to all modules
        bind(Key.get(new TypeLiteral<List<String>>(){}, Args.class)).toInstance(ImmutableList.copyOf(arguments));

        if(appMainClass != null){
            binder().requestStaticInjection(appMainClass);
        }
    }

    //export the Application Config as @Config singleton
    @Provides
    @Singleton
    @Config
    public ConfigProvider getAppConfigProvider(){
        return appConfigProvider;
    }

    //export the Application wide eventbus to allow event bus mechanism within the app
    @Provides
    @Singleton
    @EventBus
    public com.google.common.eventbus.EventBus getAppEventBus(@EventListener Set<Object> eventListeners){

        com.google.common.eventbus.EventBus defaultEventBus = new com.google.common.eventbus.EventBus(APP_EVENTBUS_NAME);

        if(!eventListeners.isEmpty()){
            //register the defined event listeners to the default event  bus
            eventListeners.forEach(listener->{
                defaultEventBus.register(listener);
            });
        }
        return defaultEventBus;
    }

    @Provides
    @Singleton
    public ExceptionReporter getExceptionReporter(Set<FailureAnalyzer> failureAnalyzers){
        FailureAnalyzer defaultFailureAnalyzer = new DefaultFailureAnalyzer();
        FailureAnalysisReporter failureReporter = new DefaultFailureAnalysisReporter();
        return new FailureAnalyzers(defaultFailureAnalyzer,failureAnalyzers,failureReporter);
    }

}
