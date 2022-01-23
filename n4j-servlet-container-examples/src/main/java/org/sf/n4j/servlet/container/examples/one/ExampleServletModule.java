package org.sf.n4j.servlet.container.examples.one;

import com.google.inject.Provides;
import com.google.inject.Scopes;
import com.google.inject.servlet.ServletModule;
import org.sf.n4j.intf.servlet.container.ServletContainer;

import javax.inject.Singleton;

public class ExampleServletModule extends ServletModule {

    @Override
    protected void configureServlets() {
        super.configureServlets();
        bind(HelloWorldServlet.class).in(Scopes.SINGLETON);
        serve("/hello").with(HelloWorldServlet.class);
    }

    @Provides
    @Singleton
    public ExampleOneStarter getExampleOneStarter(ServletContainer servletContainer){
        return new ExampleOneStarter(servletContainer);
    }

}
