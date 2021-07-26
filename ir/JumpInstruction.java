package ir;

import common.Label;

/**
 * Represents a GOTO or "jump" IR instruction that moves
 * control to a specified label.
 */
public class JumpInstruction implements IRInstruction {
    
    private final Label label;

    public JumpInstruction(Label l) {
        label = l;
    }

    /**
     * Used for the Visitor pattern. See {@link IRProgramVisitor} for details.
     */
    public <T> T accept(IRProgramVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public String toString() {
        return "GOTO " + label.toString() + ';';
    }
}