package common;

/**
 * Represents a label within an IR or Jasmin assembly program.
 */
public class Label {
    public final int index;
    
    Label(int i) {
        index = i;
    }

    public String toIRString() {
        return "L" + Integer.toString(index);
    }

    public String toJasminString() {
        return "L_" + Integer.toString(index);
    }

    public String toString() {
        return toIRString();
    }
}