package ir;

import type.SimpleType;

public class PrintInstruction implements IRInstruction {

    private Temporary temp;
    
    public PrintInstruction(Temporary temp) {
        this.temp = temp;
    }

    public String toString() {
        return "PRINT" + temp.type().toIRString() + ' ' + temp.toString() + ';';
    }
}

