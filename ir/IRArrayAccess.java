package ir;

import type.ArrayType;

public class IRArrayAccess implements IRAssignableExpression {
    
    Temporary array;
    Temporary index;

    public IRArrayAccess(Temporary array, Temporary index) {
        this.array = array;
        this.index = index;
    }

    /**
     * Used for the Visitor pattern. See {@link IRProgramVisitor} for details.
     */
    public <T> T accept(IRProgramVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public String toString() {
        return array.toString() + '[' + index.toString() + ']';
    }
}
