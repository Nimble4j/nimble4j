package org.sf.n4j.intf.base.diagnostics;

import java.util.Map;

public interface FailureAnalyzer {

    public FailureAnalysis analyze(Throwable failure, Map<String,Object> failureContext);

    boolean canAnalyze(Throwable throwable, Map<String, Object> failureContext);
}
