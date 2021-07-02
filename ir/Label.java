package ir;

/**
 * Represents a label within an IR program. May be used as part
 * of a label instruction or a jump instruction.
 */
public class Label {
    public final int index;
    
    Label(int i) {
        index = i;
    }

    public String toString() {
        return "L" + Integer.toString(index);
    }
}