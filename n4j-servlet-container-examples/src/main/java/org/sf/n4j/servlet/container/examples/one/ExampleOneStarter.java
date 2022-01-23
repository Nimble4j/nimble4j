package org.sf.n4j.servlet.container.examples.one;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.sf.n4j.intf.servlet.container.ServletContainer;

@Slf4j
public class ExampleOneStarter implements Runnable {
    private final ServletContainer servletContainer;

    public ExampleOneStarter(@NonNull ServletContainer servletContainer) {
        this.servletContainer = servletContainer;
    }

    @Override
    public void run() {
        log.info("About to start Example One App services...");

        log.info("Starting the servlet container...");

        this.servletContainer.start();

        log.info("Servlet container started successfully");
    }
}
