package ir;

/**
 * An assignment of a temporary into an array index
 */
public class ArrayAssignmentInstruction implements IRInstruction {

    public final IRArrayAccess arrayAccess;
    public final Temporary value;

    public ArrayAssignmentInstruction(IRArrayAccess arrayAccess, Temporary value) {
        this.arrayAccess = arrayAccess;
        this.value = value;
    }

    /**
     * Used for the Visitor pattern. See {@link IRProgramVisitor} for details.
     */
    public <T> T accept(IRProgramVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public String toString() {
        return arrayAccess.toString() + " := " + value.toString() + ';';
    }

}
