package ir;

import java.util.ArrayList;
import java.util.List;

public class FunctionCallInstruction implements IRInstruction {

    private final IRFunctionCall functionCall;

    public FunctionCallInstruction(String functionName) {
        this(new IRFunctionCall(functionName));
    }

    public FunctionCallInstruction(String functionName, List<Temporary> args) {
        this(new IRFunctionCall(functionName, args));
    }

    public FunctionCallInstruction(IRFunctionCall functionCall) {
        this.functionCall = functionCall;
    }

    public String toString() {
        return functionCall.toString() + ';';
    }

}
