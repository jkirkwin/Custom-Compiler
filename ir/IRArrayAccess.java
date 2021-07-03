package ir;

import type.ArrayType;

public class IRArrayAccess implements IRAssignableExpression {
    
    Temporary array;
    Temporary index;

    public IRArrayAccess(Temporary array, Temporary index) {
        this.array = array;
        this.index = index;
    }

    public String toString() {
        return array.toString() + '[' + index.toString() + ']';
    }
}
