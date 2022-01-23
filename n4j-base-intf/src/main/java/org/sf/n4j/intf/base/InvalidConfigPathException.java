package org.sf.n4j.intf.base;

import java.util.Map;

public class InvalidConfigPathException extends ConfigException{
    public InvalidConfigPathException(String code, Map<String, Object> exceptionContext) {
        super(code, exceptionContext);
    }

    public InvalidConfigPathException(String message, String code, Map<String, Object> exceptionContext) {
        super(message, code, exceptionContext);
    }

    public InvalidConfigPathException(String message, Throwable cause, String code, Map<String, Object> exceptionContext) {
        super(message, cause, code, exceptionContext);
    }

    public InvalidConfigPathException(Throwable cause, String code, Map<String, Object> exceptionContext) {
        super(cause, code, exceptionContext);
    }

    public InvalidConfigPathException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, String code, Map<String, Object> exceptionContext) {
        super(message, cause, enableSuppression, writableStackTrace, code, exceptionContext);
    }
}
