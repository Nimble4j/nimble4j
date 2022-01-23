package org.sf.n4j.examples.failureanalysis;

import com.google.common.collect.ImmutableMap;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.sf.n4j.intf.base.BaseException;
import org.sf.n4j.intf.base.ExceptionReporter;
import org.sf.n4j.intf.base.diagnostics.FailureAnalysis;
import org.sf.n4j.impl.base.BaseModule;
import org.sf.n4j.config.ts.TypeSafeConfigProviderFactory;

public class Main {

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new BaseModule(TypeSafeConfigProviderFactory.load(),args));

        ExceptionReporter exceptionReporter = injector.getInstance(ExceptionReporter.class);

        ExampleService exampleService = injector.getInstance(ExampleService.class);

        try{
            exampleService.start();
        }catch(BaseException baseException){
            exceptionReporter.report(baseException,baseException.getExceptionContext());
        }catch(Exception otherException){
            exceptionReporter.report(otherException, ImmutableMap.of(
                    FailureAnalysis.WHERE,"ExampleService Start",
                    FailureAnalysis.STATE_MESSAGE,"ExampleService failed to start",
                    FailureAnalysis.ACTION,"Check the configuration for ExampleService (ExampleOne.service)"
            ));
        }
    }
}
