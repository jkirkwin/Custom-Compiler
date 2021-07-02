package ir;

/**
 * Represents a GOTO or "jump" IR instruction that moves
 * control to a specified label.
 */
public class JumpInstruction implements IRInstruction {
    
    private final Label label;

    public JumpInstruction(Label l) {
        label = l;
    }

    public String toString() {
        return "GOTO " + label.toString() + ';';
    }
}