package org.sf.n4j.impl.base;

import org.sf.n4j.intf.base.diagnostics.FailureAnalysis;
import org.sf.n4j.intf.base.diagnostics.FailureAnalyzer;

import java.util.Map;

class DefaultFailureAnalyzer implements FailureAnalyzer {

    @Override
    public FailureAnalysis analyze(Throwable failure, Map<String, Object> failureContext) {

        String description = getProblemDescription(failure,failureContext);
        String action = getPrescriptiveAction(failure,failureContext);
        FailureAnalysis failureAnalysis = new FailureAnalysis(description,action,failure,failureContext);
        return failureAnalysis;
    }

    private String getPrescriptiveAction(Throwable failure, Map<String, Object> failureContext) {
        //The best we can do here is a standard prescriptive message irrespective of the context

        //check if the context has an indication of the possible action
        if(failureContext.containsKey(FailureAnalysis.ACTION)){
            return failureContext.get(FailureAnalysis.ACTION).toString();
        }

        Object location = failureContext.get(FailureAnalysis.WHERE);

        return String.format("Please check the configuration for the '%s' module",location);
    }

    private String getProblemDescription(Throwable failure, Map<String, Object> failureContext) {
        String message = failure.getMessage();
        String description = String.format("Error occured during '%s', cause : %s",failureContext.get(FailureAnalysis.WHERE),message);
        return description;
    }

    @Override
    public boolean canAnalyze(Throwable throwable, Map<String, Object> failureContext) {
        //this is a catch all failure analyzer
        return true;
    }
}
