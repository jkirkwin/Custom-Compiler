package ir;

/**
 * Marker interface which represents an expression in the IR
 * that can be assigned from (i.e. can be on the RHS of an 
 * assignment instruction).
 */
public interface IRAssignableExpression {

    /**
     * The IR string representation of the expression
     */
    public String toString();

}
