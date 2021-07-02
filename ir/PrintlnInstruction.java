package ir;

import type.SimpleType;

public class PrintlnInstruction implements IRInstruction {

    private Temporary temp;
    
    public PrintlnInstruction(Temporary temp) {
        this.temp = temp;
    }

    public String toString() {
        return "PRINTLN" + temp.type().toIRString() + ' ' + temp.toString() + ';';
    }
}

