package org.sf.n4j.intf.base.diagnostics;

import lombok.Value;

import java.util.Map;

@Value
public class FailureAnalysis {

    public static final String ACTION = "ACTION";
    public static final String WHERE = "WHERE";
    public static final String STATE_MESSAGE = "STATE_MESSAGE";
    public static final String DATA = "DATA";

    private String description;
    private String action;
    private Throwable cause;
    private Map<String,Object> context;

}
