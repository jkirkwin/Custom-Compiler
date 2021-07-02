package ir;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents an IR program as a list of functions.
 */
public class IRProgram {

    /**
     * Used to construct an IR program by repeatedly adding functions.
     */
    public static class Builder {

        private final List<IRFunction> functions;

        public Builder() {
            functions = new ArrayList<IRFunction>();
        }

        public IRProgram build() {
            return new IRProgram(functions);
        }

        public Builder addFunction(IRFunction f) {
            functions.add(f);
            return this;
        }
    }

    public final List<IRFunction> functions;

    public IRProgram(List<IRFunction> functions) { // TODO May need to add a name to the program and builder?
        this.functions = Collections.unmodifiableList(new ArrayList<IRFunction>(functions));
    }

    public String toString() {
        // TODO Add a class name and maybe "PROG" to the beginning? Unsure.
        StringBuilder sb = new StringBuilder(); 
        for (var func : functions) {
            sb.append(func.toString());
        }
        return sb.toString();
    }

}