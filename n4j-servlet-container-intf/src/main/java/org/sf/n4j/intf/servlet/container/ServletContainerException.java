package org.sf.n4j.intf.servlet.container;

public class ServletContainerException extends RuntimeException {

    public ServletContainerException() {
    }

    public ServletContainerException(String message) {
        super(message);
    }

    public ServletContainerException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServletContainerException(Throwable cause) {
        super(cause);
    }

    public ServletContainerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
