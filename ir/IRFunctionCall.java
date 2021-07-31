package ir;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class IRFunctionCall implements IRAssignableExpression {

    private static String getArgumentString(List<Temporary> args) {
        String s = "(";
        
        for (int i = 0; i < args.size(); ++i) {
            s += args.get(i).toString();
            if (i < args.size() - 1) {
                s += " ";
            }
        }

        return s + ")";
    }

    public final String functionName;
    public final List<Temporary> args;

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
        this.args = Collections.unmodifiableList(new ArrayList<Temporary>(args));
    }

    /**
     * Used for the Visitor pattern. See {@link IRProgramVisitor} for details.
     */
    public <T> T accept(IRProgramVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public String toString() {
        return "CALL " + functionName + getArgumentString(args);
    }
}
