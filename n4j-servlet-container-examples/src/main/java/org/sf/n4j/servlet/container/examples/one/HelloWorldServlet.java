package org.sf.n4j.servlet.container.examples.one;

import com.google.common.net.HttpHeaders;
import org.sf.n4j.intf.base.ConfigProvider;
import org.sf.n4j.intf.servlet.container.ServletContainerConfig;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

class HelloWorldServlet extends HttpServlet {

    @Inject
    @Named("servlet.container.config")
    ConfigProvider servletContainerConfigProvider;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletContainerConfig servletContainerConfig = servletContainerConfigProvider.bindTo(ServletContainerConfig.class);

        StringBuffer buffer = new StringBuffer("Servlet Config :");
        buffer.append(String.format(" Host: '%s', Port : '%s', Context Path = '%s'",servletContainerConfig.getHost(),
                servletContainerConfig.getPort(),servletContainerConfig.getContextPath()));
        resp.addHeader(HttpHeaders.CONTENT_TYPE,"text/plain");
        resp.getWriter().write(buffer.toString());
        resp.flushBuffer();
    }
}
