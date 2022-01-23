package org.sf.n4j.impl.servlet.container;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Provides;
import org.sf.n4j.intf.base.ConfigProvider;
import org.sf.n4j.intf.base.annotations.Config;
import org.sf.n4j.intf.servlet.container.ServletContainer;
import org.sf.n4j.intf.servlet.container.ServletContainerConfig;

import javax.inject.Named;
import javax.inject.Singleton;

public class JettyServletContainerModule extends AbstractModule {

    public static final String CONFIG_KEY_PATH = "servlet.container.config";

    @Provides
    @Singleton
    @Named(CONFIG_KEY_PATH)
    public ConfigProvider getServletContainerConfig(@Config ConfigProvider appConfigProvider){
        return appConfigProvider.getProviderForConfigPath(CONFIG_KEY_PATH);
    }

    @Provides
    @Singleton
    public ServletContainer getServletContainer(@Named(CONFIG_KEY_PATH) ConfigProvider servletContainerConfigProvider,
                                                Injector appInjector){
        ServletContainerConfig servletContainerConfig = servletContainerConfigProvider.bindTo(ServletContainerConfig.class);
        return new JettyServletContainer(servletContainerConfig,appInjector);
    }
}
