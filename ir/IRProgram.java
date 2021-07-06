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
        private String programName;

        public Builder() {
            functions = new ArrayList<IRFunction>();
        }

        public IRProgram build() {
            if (programName == null || functions == null) {
                throw new IllegalStateException("Cannot build program without all attributes");
            }
            return new IRProgram(programName, functions);
        }

        public Builder addFunction(IRFunction f) {
            functions.add(f);
            return this;
        }

        public Builder withName(String s) {
            programName = s;
            return this;
        }
    }

    private final List<IRFunction> functions;
    private final String programName;

    public IRProgram(String programName, List<IRFunction> functions) {
        this.programName = programName;
        this.functions = Collections.unmodifiableList(new ArrayList<IRFunction>(functions));
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(); 
        
        sb.append("PROG ")
          .append(programName)
          .append('\n');
        
        for (var func : functions) {
            sb.append(func.toString());
        }
        
        return sb.toString();
    }
}
