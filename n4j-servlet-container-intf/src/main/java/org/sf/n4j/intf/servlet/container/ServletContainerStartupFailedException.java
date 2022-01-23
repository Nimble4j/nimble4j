package org.sf.n4j.intf.servlet.container;

import org.sf.n4j.intf.base.BaseException;

import java.util.Map;

public class ServletContainerStartupFailedException extends BaseException {
    public ServletContainerStartupFailedException(String code, Map<String, Object> exceptionContext) {
        super(code, exceptionContext);
    }

    public ServletContainerStartupFailedException(String message, String code, Map<String, Object> exceptionContext) {
        super(message, code, exceptionContext);
    }

    public ServletContainerStartupFailedException(String message, Throwable cause, String code, Map<String, Object> exceptionContext) {
        super(message, cause, code, exceptionContext);
    }

    public ServletContainerStartupFailedException(Throwable cause, String code, Map<String, Object> exceptionContext) {
        super(cause, code, exceptionContext);
    }

    public ServletContainerStartupFailedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, String code, Map<String, Object> exceptionContext) {
        super(message, cause, enableSuppression, writableStackTrace, code, exceptionContext);
    }
}
