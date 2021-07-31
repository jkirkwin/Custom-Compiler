package ir;

import java.util.ArrayList;
import java.util.List;

public class FunctionCallInstruction implements IRInstruction {

    public final IRFunctionCall functionCall;

    public FunctionCallInstruction(String functionName) {
        this(new IRFunctionCall(functionName));
    }

    public FunctionCallInstruction(String functionName, List<Temporary> args) {
        this(new IRFunctionCall(functionName, args));
    }

    public FunctionCallInstruction(IRFunctionCall functionCall) {
        this.functionCall = functionCall;
    }

    /**
     * Used for the Visitor pattern. See {@link IRProgramVisitor} for details.
     */
    public <T> T accept(IRProgramVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public String toString() {
        return functionCall.toString() + ';';
    }

}
