package ir;

/**
 * Defines a label which can be jumped to.
 */
public class LabelInstruction implements IRInstruction {
    public final Label label;

    public LabelInstruction(Label l) {
        label = l;
    }

    public String toString() {
        return label.toString() + ":;";
    }
}
