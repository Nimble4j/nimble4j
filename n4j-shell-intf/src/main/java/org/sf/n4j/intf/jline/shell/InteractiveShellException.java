package org.sf.n4j.intf.jline.shell;

import org.sf.n4j.intf.base.BaseException;

import java.util.Map;

public class InteractiveShellException extends BaseException {
    public InteractiveShellException(String code, Map<String, Object> exceptionContext) {
        super(code, exceptionContext);
    }

    public InteractiveShellException(String message, String code, Map<String, Object> exceptionContext) {
        super(message, code, exceptionContext);
    }

    public InteractiveShellException(String message, Throwable cause, String code, Map<String, Object> exceptionContext) {
        super(message, cause, code, exceptionContext);
    }

    public InteractiveShellException(Throwable cause, String code, Map<String, Object> exceptionContext) {
        super(cause, code, exceptionContext);
    }

    public InteractiveShellException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, String code, Map<String, Object> exceptionContext) {
        super(message, cause, enableSuppression, writableStackTrace, code, exceptionContext);
    }
}
