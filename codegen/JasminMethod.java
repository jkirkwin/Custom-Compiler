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
        private final List<JasminVariableDeclaration> variables;
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

        public Builder addVariable(JasminVariableDeclaration variable) {
            variables.add(variable);
            return this;
        }

        public Builder addVariables(Collection<JasminVariableDeclaration> variables) {
            for (var variable : variables) {
                addVariable(variable)
            }
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
            return new JasminMethod(methodName, isStatic, methodType, variables, statements);
        }
    }

    // These are all immutable, so they're made public for convenience.
    public final String methodName;
    public final boolean isStatic;
    public MethodType type;
    public final List<JasminVariableDeclaration> variables;
    public final List<JasminStatement> statements;

    private JasminMethod(String methodName, 
                        boolean isStatic, 
                        MethodType type, 
                        List<JasminVariableDeclaration> variables, 
                        List<JasminStatement> statements) {
        this.methodName = methodName;
        this.isStatic = isStatic;
        this.type = type;
        this.variables = Collections.unmodifiableList(new ArrayList<JasminVariableDeclaration>(variables));
        this.statements = Collections.unmodifiableList(new ArrayList<JasminStatement>(statements));
    }
}
