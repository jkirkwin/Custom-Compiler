package codegen;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Represents a program written in the Unnamed Language 
 * as a collection of Jasmin instructions.
 */
public class JasminProgram {

    public static class Builder {
    
        private String className;
        private String sourceFile;
        private List<JasminMethod> methods;

        public Builder() {
            methods = new ArrayList<JasminMethod>();
            // TODO Add object initializer method here
        }

        public Builder withClassName(String className) {
            assert className != null;
            this.className = className;
            return this;
        }

        public Builder withSourceFile(String sourceFile) {
            assert sourceFile != null;
            this.sourceFile = sourceFile;
            return this;
        }

        public Builder addMethod(JasminMethod method) {
            methods.add(method);
            return this;
        }

        public JasminProgram build() {
            assert className != null;
            assert sourceFile != null;
            return new JasminProgram(className, sourceFile, methods);
        }
    }

    private final String className;
    private final String sourceFile;
    private final List<JasminMethod> methods;

    private JasminProgram(String className, String sourceFile, List<JasminMethod> methods) {
        this.className = className;
        this.sourceFile = sourceFile;
        this.methods = Collections.unmodifiableList(new ArrayList<JasminMethod>(methods)); // TODO Make sure the builder always adds an initializer and that the visitor adds __main() and main()
    }
}
