package ir;

import type.*;

/**
 * Represents a typed operator with two operands of that type. The set
 * of possible operators is: +, -, /, *, rem, <, <=, >, >=, ==, and !=
 */
public class BinaryOperation {

    public static enum OperatorGroups {
        ARITHMETIC,
        COMPARISON
    }

    public static enum Operators {
        PLUS("+", OperatorGroups.ARITHMETIC),
        MINUS("-", OperatorGroups.ARITHMETIC),
        MULTIPLY("*", OperatorGroups.ARITHMETIC),
        DIVIDE("/", OperatorGroups.ARITHMETIC),
        REMAINDER("rem", OperatorGroups.ARITHMETIC),

        LESS("<", OperatorGroups.COMPARISON),
        LESS_EQUAL("<=", OperatorGroups.COMPARISON),
        GREATER(">", OperatorGroups.COMPARISON),
        GREATER_EQUAL(">=", OperatorGroups.COMPARISON),
        EQUAL("==", OperatorGroups.COMPARISON),
        NOT_EQUAL("!=", OperatorGroups.COMPARISON);

        private final String operatorString;
        final OperatorGroups group;

        private Operators(String s, OperatorGroups g) {
            operatorString = s;
            group = g;
        }

        public String toString() {
            return operatorString;
        }
    }

    private static boolean isValidType(Operators operator, Type type) {
        switch (operator) {
            // Arithmetic operators are defined for the same types, except
            // PLUS also supports strings.
            case PLUS:
                if (TypeUtils.isString(type)) {
                    return true;
                }
            case MINUS:
            case MULTIPLY:
            case DIVIDE:
            case REMAINDER:
                return TypeUtils.isInt(type)
                    || TypeUtils.isFloat(type)
                    || TypeUtils.isChar(type);


            // Comparison operators are defined for the same types, except
            // equality/inequality which also support booleans.
            case EQUAL:
            case NOT_EQUAL:
                if (TypeUtils.isBoolean(type)) {
                    return true;
                }
            case LESS:
            case LESS_EQUAL:
            case GREATER:
            case GREATER_EQUAL:
                return TypeUtils.isInt(type)
                    || TypeUtils.isFloat(type)
                    || TypeUtils.isChar(type)
                    || TypeUtils.isString(type);
        
            default:
                throw new IllegalStateException("No such operator is supported");
        }
    }
    
    /**
     * Factory method used to instantiate BinaryOperations.
     */
    public static BinaryOperation getOperation(Operators operator, Temporary left, Temporary right) {
        if (!left.type().equals(right.type())) {
            throw new IllegalArgumentException("Operand types do not match");
        }

        Type type = left.type();
        if (!isValidType(operator, type)) {
            throw new IllegalArgumentException("Operator " + operator.toString() + " not defined over type " + type.toString());
        }

        return new BinaryOperation(operator, left, right);
    }

    private final Operators operator;
    private final Temporary left;
    private final Temporary right;

    private BinaryOperation(Operators operator, Temporary left, Temporary right) {
        this.operator = operator;
        this.left = left;
        this.right = right;

        assert (isValidType(operator, operationType()));
    }

    public Type operationType() {
        return left.type(); // Both operands will have the same type
    }

    public Type resultType() {
        switch(operator.group) {
            case ARITHMETIC:
                // Arithmetic operations yield the same type as the inputs
                return operationType();

            case COMPARISON:
                // Comparison operations always yield a boolean
                return BooleanType.INSTANCE;

            default:
                throw new IllegalStateException("No such operator group is defined");
        }
    }

    public String toString() {
        return left.toString() + ' ' + operationType().toString() + operator.toString() + ' ' + right;
    }
}
