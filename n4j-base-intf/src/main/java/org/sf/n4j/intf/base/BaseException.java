package org.sf.n4j.intf.base;

import java.util.Map;

public class BaseException extends RuntimeException{

    private String code;
    private Map<String,Object> exceptionContext;

    public BaseException(String code, Map<String, Object> exceptionContext) {
        this.code = code;
        this.exceptionContext = exceptionContext;
    }

    public BaseException(String message, String code, Map<String, Object> exceptionContext) {
        super(message);
        this.code = code;
        this.exceptionContext = exceptionContext;
    }

    public BaseException(String message, Throwable cause, String code, Map<String, Object> exceptionContext) {
        super(message, cause);
        this.code = code;
        this.exceptionContext = exceptionContext;
    }

    public BaseException(Throwable cause, String code, Map<String, Object> exceptionContext) {
        super(cause);
        this.code = code;
        this.exceptionContext = exceptionContext;
    }

    public BaseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, String code, Map<String, Object> exceptionContext) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.code = code;
        this.exceptionContext = exceptionContext;
    }

    public String getCode() {
        return code;
    }

    public Map<String, Object> getExceptionContext() {
        return exceptionContext;
    }
}
