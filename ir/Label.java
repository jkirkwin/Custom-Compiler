package ir;

/**
 * Represents a label within an IR program. May be used as part
 * of a label instruction or a jump instruction.
 */
public class Label { // TODO either rename to IRLabel, or move this to common so it can be used as a jasmin label and use toIRString instead of toString.
    public final int index;
    
    Label(int i) {
        index = i;
    }

    public String toString() {
        return "L" + Integer.toString(index);
    }
}