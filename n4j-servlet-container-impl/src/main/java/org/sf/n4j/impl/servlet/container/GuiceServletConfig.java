package org.sf.n4j.impl.servlet.container;

import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import lombok.NonNull;

class GuiceServletConfig extends GuiceServletContextListener {

    private Injector appInjector;

    GuiceServletConfig(@NonNull Injector appInjector){
        this.appInjector = appInjector;
    }

    @Override
    protected Injector getInjector() {
        return appInjector;
    }
}
