package org.sf.n4j.impl.base;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.sf.n4j.intf.base.ExceptionReporter;
import org.sf.n4j.intf.base.diagnostics.FailureAnalysis;
import org.sf.n4j.intf.base.diagnostics.FailureAnalysisReporter;
import org.sf.n4j.intf.base.diagnostics.FailureAnalyzer;

import java.util.Map;
import java.util.Set;

@Slf4j
class FailureAnalyzers implements ExceptionReporter {


    private final FailureAnalyzer defaultFailureAnalyzer;
    private final Set<FailureAnalyzer> otherFailureAnalyzers;
    private final FailureAnalysisReporter reporter;

    FailureAnalyzers(@NonNull FailureAnalyzer defaultFailureAnalyzer,
                     @NonNull Set<FailureAnalyzer> otherFailureAnalyzers,
                     @NonNull FailureAnalysisReporter reporter){
        this.defaultFailureAnalyzer = defaultFailureAnalyzer;
        this.otherFailureAnalyzers = otherFailureAnalyzers;
        this.reporter = reporter;
    }

    @Override
    public void report(Throwable throwable,
                       Map<String,Object> context) {
        FailureAnalyzer failureAnalyzer = findAptFailureAnalyzer(throwable,context);
        FailureAnalysis failureAnalysis = failureAnalyzer.analyze(throwable, context);
        reporter.report(failureAnalysis);
    }

    private FailureAnalyzer findAptFailureAnalyzer(Throwable throwable, Map<String, Object> context) {
        //go through the failure analyzer and as each one if they can handle the throwable
        //if they say yes then the first one matching is given the opportunity to analyze
        //else the default one gets the chance
        FailureAnalyzer selectedAnalyzer = defaultFailureAnalyzer;
        try {
            for (FailureAnalyzer otherFailureAnalyzer : otherFailureAnalyzers) {
                if (otherFailureAnalyzer.canAnalyze(throwable, context)) {
                    selectedAnalyzer = otherFailureAnalyzer;
                    break;
                }
            }
        }catch(Exception e){
            log.warn("Unexpected exception during failure analysis handling {}",e.getMessage());
            //TODO: Ability to configure stack trace printing
            e.printStackTrace();
        }
        return selectedAnalyzer;
    }
}
