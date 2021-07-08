package ir;

import type.ArrayType;

public class IRArrayCreation implements IRAssignableExpression {
    
    private final ArrayType arrayType;

    public IRArrayCreation(ArrayType arrayType) {
        this.arrayType = arrayType;
    }

    public String toString() {
        return "NEWARRAY " + arrayType.simpleType.toIRString() + ' ' + arrayType.size;
    }
}
