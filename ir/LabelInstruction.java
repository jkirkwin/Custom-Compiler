package ir;

import common.Label;

/**
 * Defines a label which can be jumped to.
 */
public class LabelInstruction implements IRInstruction {
    public final Label label;

    public LabelInstruction(Label l) {
        label = l;
    }

    /**
     * Used for the Visitor pattern. See {@link IRProgramVisitor} for details.
     */
    public <T> T accept(IRProgramVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public String toString() {
        return label.toString() + ":;";
    }
}
