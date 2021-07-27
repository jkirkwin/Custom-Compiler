package codegen;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class JasminMethod {
    public static class Builder {

        // Always allow for at least 5 slots on the operand stack
        private static final int MIN_STACK_LIMIT = 5;

        // Signature
        private JasminMethodSignature signature;

        // Body
        private final List<JasminVariableDeclaration> variables;
        private final List<JasminStatement> statements;
        
        // Jasmin metadata
        private int stackLimit;

        public Builder() {
            variables = new ArrayList<JasminVariableDeclaration>();
            statements = new ArrayList<JasminStatement>();
            stackLimit = MIN_STACK_LIMIT;
        }

        public Builder withSignature(JasminMethodSignature signature) {
            this.signature = signature;
            return this;
        }

        public Builder addVariable(JasminVariableDeclaration variable) {
            variables.add(variable);
            return this;
        }

        public Builder addVariables(Collection<JasminVariableDeclaration> variables) {
            for (var variable : variables) {
                addVariable(variable);
            }
            return this;
        }

        public Builder addStatement(JasminStatement statement) {
            statements.add(statement);

            // If the statement is a function call that requires more than the current 
            // amount of stack space for its parameters, increase the stack limit to 
            // accomodate it.
            if (statement instanceof JasminCallInstruction) {
                var functionCall = (JasminCallInstruction)statement;
                var methodType = functionCall.targetSignature.methodType;

                // + 2 just to be safe. We're probably okay with just the arg count.
                int argCount = methodType.argumentTypes.size();
                int minStackSize = argCount + 2; 
                if (minStackSize > stackLimit) {
                    stackLimit = minStackSize; 
                }
            }

            return this;
        }

        public Builder addStatements(Collection<JasminStatement> statements) {
            for (var statement : statements) {
                addStatement(statement);
            }
            return this;
        }

        public JasminMethod build() {
            return new JasminMethod(signature, stackLimit, variables, statements);
        }
    }

    // These are all immutable, so they're made public for convenience.
    public final JasminMethodSignature signature;
    public final int stackLimit;
    public final List<JasminVariableDeclaration> variables;
    public final List<JasminStatement> statements;

    private JasminMethod(JasminMethodSignature signature, 
                        int stackLimit,
                        List<JasminVariableDeclaration> variables, 
                        List<JasminStatement> statements) {
        this.signature = signature;
        this.stackLimit = stackLimit;
        this.variables = Collections.unmodifiableList(new ArrayList<JasminVariableDeclaration>(variables));
        this.statements = Collections.unmodifiableList(new ArrayList<JasminStatement>(statements));
    }
}
