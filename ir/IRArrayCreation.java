package ir;

import type.ArrayType;

public class IRArrayCreation implements IRAssignableExpression {
    
    public final ArrayType arrayType;

    public IRArrayCreation(ArrayType arrayType) {
        this.arrayType = arrayType;
    }

    /**
     * Used for the Visitor pattern. See {@link IRProgramVisitor} for details.
     */
    public <T> T accept(IRProgramVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public String toString() {
        return "NEWARRAY " + arrayType.simpleType.toIRString() + ' ' + arrayType.size;
    }
}
