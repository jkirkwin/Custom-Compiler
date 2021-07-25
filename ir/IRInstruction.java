package ir;

/**
 * A marker interface which all IR instruction classes
 * must implement so that they may be stored together. 
 */
public interface IRInstruction {


    /**
     * Used for the Visitor pattern. See {@link IRProgramVisitor} for details.
     */
    public <T> T accept(IRProgramVisitor<T> visitor);

    /**
     * Generates a textual representation of the given instruction, 
     * ending in a semicolon.
     */
    public String toString();

}
