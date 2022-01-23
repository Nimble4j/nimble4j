package org.sf.n4j.examples.failureanalysis;

import org.sf.n4j.intf.base.BaseException;

import java.util.Map;

public class ExampleServiceStartupFailedException extends BaseException {
    public ExampleServiceStartupFailedException(String code, Map<String, Object> exceptionContext) {
        super(code, exceptionContext);
    }

    public ExampleServiceStartupFailedException(String message, String code, Map<String, Object> exceptionContext) {
        super(message, code, exceptionContext);
    }

    public ExampleServiceStartupFailedException(String message, Throwable cause, String code, Map<String, Object> exceptionContext) {
        super(message, cause, code, exceptionContext);
    }

    public ExampleServiceStartupFailedException(Throwable cause, String code, Map<String, Object> exceptionContext) {
        super(cause, code, exceptionContext);
    }

    public ExampleServiceStartupFailedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, String code, Map<String, Object> exceptionContext) {
        super(message, cause, enableSuppression, writableStackTrace, code, exceptionContext);
    }
}
