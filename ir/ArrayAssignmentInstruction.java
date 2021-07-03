package ir;

/**
 * An assignment of a temporary into an array index
 */
public class ArrayAssignmentInstruction implements IRInstruction {

    private final IRArrayAccess arrayAccess;
    private final Temporary value;

    public ArrayAssignmentInstruction(IRArrayAccess arrayAccess, Temporary value) {
        this.arrayAccess = arrayAccess;
        this.value = value;
    }

    public String toString() {
        return arrayAccess.toString() + " := " + value.toString() + ';';
    }

}
