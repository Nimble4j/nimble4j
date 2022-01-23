package org.sf.n4j.intf.servlet.container;

import lombok.Data;

@Data
public class ServletContainerConfig {

    private String protocol;
    private String host;
    private int port;
    private String contextPath;

}
