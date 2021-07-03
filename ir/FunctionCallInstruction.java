package ir;

import java.util.ArrayList;
import java.util.List;

public class FunctionCallInstruction implements IRInstruction {

    private final IRFunctionCall functionCall;

    public FunctionCallInstruction(String functionName) {
        this(functionName, new ArrayList<Temporary>());
    }

    public FunctionCallInstruction(String functionName, List<Temporary> args) {
        functionCall = new IRFunctionCall(functionName, args);
    }

    public String toString() {
        return functionCall.toString() + ';';
    }

}
