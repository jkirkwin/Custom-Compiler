package ir;

import type.*;

/**
 * A logical or bitwise negation operation.
 */
public class NegationOperation implements IRAssignableExpression {
  
    private static boolean isValidType(Type t) {
        return type.TypeUtils.isBoolean(t) || type.TypeUtils.isInt(t);
    }

    public final Temporary operand;
    public final Type operandType;

    public NegationOperation(Temporary temp) {
        operand = temp;
        operandType = operand.type();
        if (!isValidType(operandType)) {
            throw new IllegalArgumentException("Cannot negate type " + operand.type());
        }
    }

    /**
     * Used for the Visitor pattern. See {@link IRProgramVisitor} for details.
     */
    public <T> T accept(IRProgramVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public String toString() {
        return operandType.toIRString() + "! " + operand.toString();
    }
}
