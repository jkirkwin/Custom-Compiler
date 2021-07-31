package ir;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents an IR program as a list of functions.
 */
public class IRProgram {

    private static final String FILE_EXT = ".ir";

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

    public final List<IRFunction> functions;
    public final String programName;

    public IRProgram(String programName, List<IRFunction> functions) {
        this.programName = programName;
        this.functions = Collections.unmodifiableList(new ArrayList<IRFunction>(functions));
    }

    /**
     * Used for the Visitor pattern. See {@link IRProgramVisitor} for details.
     */
    public <T> T accept(IRProgramVisitor<T> visitor) throws Exception {
        return visitor.visit(this);
    }

    /**
     * Save the IR program to a file corresponding to its name. If 
     * no such file already exists, one will be created. If such a 
     * file does already exist, it will be overwritten.
     * @return The name of the file that was written 
     */
    public String saveToFile() throws FileNotFoundException {
        String irFileName = programName + FILE_EXT;
        File irFile = new File(irFileName);
        
        try (PrintWriter writer = new PrintWriter(irFile)) {
            writer.print("PROG ");
            writer.println(programName);
    
            for (var func : functions) {
                writer.print(func.toString());
            }
    
            return irFileName;
        }
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
