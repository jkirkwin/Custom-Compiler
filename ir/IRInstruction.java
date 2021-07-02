package ir;

/**
 * A marker interface which all IR instruction classes
 * must implement so that they may be stored together. 
 */
public interface IRInstruction {

    /**
     * Generates a textual representation of the given instruction, 
     * ending in a semicolon.
     */
    public String toString();

}
