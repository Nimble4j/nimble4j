package org.sf.n4j.intf.base;

import java.util.Map;

public interface ExceptionReporter {

    public void report(Throwable throwable, Map<String,Object> context);

}
