package org.sf.n4j.impl.servlet.container;

import com.google.common.collect.ImmutableMap;
import com.google.inject.Injector;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.sf.n4j.intf.base.diagnostics.FailureAnalysis;
import org.sf.n4j.intf.servlet.container.ServletContainer;
import org.sf.n4j.intf.servlet.container.ServletContainerConfig;
import org.sf.n4j.intf.servlet.container.ServletContainerException;
import org.sf.n4j.intf.servlet.container.ServletContainerStartupFailedException;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import java.util.EnumSet;
import java.util.Map;

@Slf4j
class JettyServletContainer implements ServletContainer {

    static final String GUICE_CATCH_ALL_FILTER_CLASS = "com.google.inject.servlet.GuiceFilter";
    static final String GUICE_CATCH_ALL_FILTER_PATH = "/*";

    private final ServletContainerConfig servletContainerConfig;
    private final Injector injector;

    JettyServletContainer(@NonNull ServletContainerConfig servletContainerConfig,
                          Injector appInjector) {
        this.servletContainerConfig = servletContainerConfig;
        this.injector = appInjector;
    }

    @Override
    public void start(){
        log.debug("Starting Servlet Container with config : {}",this.servletContainerConfig);

        Server httpServer = createHttpServer(this.servletContainerConfig);

        ServletContextHandler servletContextHandler = getServletContextHandler(this.servletContainerConfig);


        try {
            registerCatchAllGuiceServletModuleFilter(servletContextHandler);
            httpServer.setHandler(servletContextHandler);
            httpServer.start();
        } catch (Exception e) {
            Map<String,Object> failureContext = ImmutableMap.of(
                    FailureAnalysis.WHERE, "Jetty servlet container start",
                    FailureAnalysis.ACTION, "Check the servlet container configuration",
                   FailureAnalysis.STATE_MESSAGE,e.getMessage(),
                ServletContainerConfig.class.getName(),
                    this.servletContainerConfig,
                    Exception.class.getName(),
                    e
            );

            throw new ServletContainerStartupFailedException("servlet.container.err.00001",
                    failureContext);
        }
    }

    private Server createHttpServer(ServletContainerConfig containerConfig) {
        Server server = new Server();
        ServerConnector connector = new ServerConnector(server);
        connector.setHost(containerConfig.getHost());
        connector.setPort(containerConfig.getPort());
        server.setConnectors(new Connector[] {connector});
        return server;
    }

    private ServletContextHandler getServletContextHandler(ServletContainerConfig containerConfig){
        ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        servletContextHandler.setContextPath(containerConfig.getContextPath());
        //ensure we add the GuiceServletConfig here so the guice-servlet module is successfully integrated with the
        //servlet web app
        servletContextHandler.addEventListener(new GuiceServletConfig(injector));
        return servletContextHandler;
    }

    private void registerCatchAllGuiceServletModuleFilter(ServletContextHandler servletContextHandler) throws ServletContainerException {
        //here add the catch-all guice servlet filter so that Guice servlet module takes control of defining servlets & filters
        Class<? extends Filter> guiceCatchAllFilterClassObj = loadClass(GUICE_CATCH_ALL_FILTER_CLASS,Filter.class);
        FilterHolder guiceFilterHolder = new FilterHolder(guiceCatchAllFilterClassObj);
        servletContextHandler.addFilter(guiceFilterHolder,GUICE_CATCH_ALL_FILTER_PATH, EnumSet.of(DispatcherType.ASYNC,
                DispatcherType.REQUEST));
    }

    private Class loadClass(String className,Class clzType) throws ServletContainerException {
        try {
            Class<?> classObj = this.getClass().getClassLoader().loadClass(className);
            if(clzType.isAssignableFrom(classObj)){
                return classObj;
            }else{
                throw new ServletContainerException(String.format("Registered Class '%s' is not a valid instance of '%s'!",className,clzType.getName()));
            }
        } catch (ClassNotFoundException e) {
            throw new ServletContainerException(String.format("Registered Class '%s' is not found!",className));
        }
    }

}
