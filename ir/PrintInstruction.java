package ir;

import type.SimpleType;

public class PrintInstruction implements IRInstruction {

    public final Temporary temp;
    
    public PrintInstruction(Temporary temp) {
        this.temp = temp;
    }

    /**
     * Used for the Visitor pattern. See {@link IRProgramVisitor} for details.
     */
    public <T> T accept(IRProgramVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public String toString() {
        return "PRINT" + temp.type().toIRString() + ' ' + temp.toString() + ';';
    }
}

