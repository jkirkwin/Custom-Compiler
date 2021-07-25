package ir;

/**
 * Marker interface which represents an expression in the IR
 * that can be assigned from (i.e. can be on the RHS of an 
 * assignment instruction).
 */
public interface IRAssignableExpression {
 
    /**
     * Used for the Visitor pattern. See {@link IRProgramVisitor} for details.
     */
    public <T> T accept(IRProgramVisitor<T> visitor);

    /**
     * The IR string representation of the expression
     */
    public String toString();

}
