package org.sf.n4j.examples.failureanalysis;

import com.google.common.collect.ImmutableMap;
import org.sf.n4j.intf.base.diagnostics.FailureAnalysis;

public class ExampleService {
    public void start() {
        throw new ExampleServiceStartupFailedException(
                "Port Number is already in use",
                "err.example.service.00001",
                ImmutableMap.of(FailureAnalysis.WHERE,"Example Service start",
                        FailureAnalysis.ACTION,"Please check the port number and host for the example service",
                        FailureAnalysis.STATE_MESSAGE,"Startup failed for Example Service")
        );
    }
}
