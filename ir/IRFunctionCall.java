package ir;

import java.util.ArrayList;
import java.util.List;

public class IRFunctionCall implements IRAssignableExpression {

    private static String getArgumentString(List<Temporary> args) {
        String s = "(";
        
        for (int i = 0; i < args.size(); ++i) {
            s += args.get(i).toString();
            if (i < args.size() - 1) {
                s += ", ";
            }
        }

        return s + ")";
    }

    private final String functionName;
    private final String argsString;

    /**
     * Creates a call operation with no arguments
     */
    public IRFunctionCall(String name) {
        this(name, new ArrayList<Temporary>());
    }

    /**
     * Creates a call operation with the given arguments
     */
    public IRFunctionCall(String name, List<Temporary> args) {
        functionName = name;
        argsString = getArgumentString(args);
    }

    public String toString() {
        return "CALL " + functionName + argsString;
    }
}
