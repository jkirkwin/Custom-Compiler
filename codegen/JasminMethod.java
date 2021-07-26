package codegen;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import type.MethodType;

public class JasminMethod {
    public static class Builder {

        // Signature
        private String methodName;
        private boolean isStatic;
        private MethodType methodType;

        // Body
        private final List<JasminStatement> statements;
        
        // TODO Add local variable mapping stuff in here.
        // need to be able to set .limit, .var, etc

        public Builder() {
            // All UL-generated methods will be static,
            // so we make it the default.
            isStatic = true;
            statements = new ArrayList<JasminStatement>();
        }

        public Builder withMethodName(String methodName) {
            this.methodName = methodName;
            return this;
        }


        public Builder withMethodType(MethodType methodType) {
            this.methodType = methodType;
            return this;
        }

        public Builder setStatic(boolean isStatic) {
            this.isStatic = isStatic;
            return this;
        }

        public Builder addStatement(JasminStatement statement) {
            statements.add(statement);
            return this;
        }

        public Builder addStatements(Collection<JasminStatement> statements) {
            for (var statement : statements) {
                this.statements.add(statement);
            }
            return this;
        }

        public JasminMethod build() {
            return new JasminMethod(methodName, isStatic, methodType, statements);
        }
    }

    // These are all immutable, so they're made public for convenience.
    public final String methodName;
    public final boolean isStatic;
    public MethodType type;
    public final List<JasminStatement> statements;

    public JasminMethod(String methodName, boolean isStatic, MethodType type, List<JasminStatement> statements) {
        this.methodName = methodName;
        this.isStatic = isStatic;
        this.type = type;
        this.statements = Collections.unmodifiableList(new ArrayList<JasminStatement>(statements));
    }
}
