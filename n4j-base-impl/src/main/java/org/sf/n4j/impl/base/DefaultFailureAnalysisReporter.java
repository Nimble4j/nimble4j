package org.sf.n4j.impl.base;

import lombok.extern.slf4j.Slf4j;
import org.sf.n4j.intf.base.diagnostics.FailureAnalysis;
import org.sf.n4j.intf.base.diagnostics.FailureAnalysisReporter;

@Slf4j
class DefaultFailureAnalysisReporter implements FailureAnalysisReporter {
    @Override
    public void report(FailureAnalysis analysis) {
        //this one just reports to the logging subsystem
        String message = buildMessage(analysis);
        //TODO: Severity based logging
        log.error(message);
    }

    private String buildMessage(FailureAnalysis failureAnalysis) {
        StringBuilder builder = new StringBuilder();
        builder.append(String.format("%n%n"));
        builder.append(String.format("***************************%n"));
        builder.append(String.format("%s%n",failureAnalysis.getContext().get(FailureAnalysis.STATE_MESSAGE)));
        builder.append(String.format("***************************%n%n"));
        builder.append(String.format("Description:%n%n"));
        builder.append(String.format("%s%n", failureAnalysis.getDescription()));
        String action = failureAnalysis.getAction();
        if (action != null && !action.isEmpty()) {
            builder.append(String.format("%nAction:%n%n"));
            builder.append(String.format("%s%n", action));
        }
        return builder.toString();
    }
}
