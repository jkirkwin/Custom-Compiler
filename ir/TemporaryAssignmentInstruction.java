package ir;

public class TemporaryAssignmentInstruction implements IRInstruction {

    public final Temporary destination;
    public final IRAssignableExpression value;

    public TemporaryAssignmentInstruction(Temporary destination, IRAssignableExpression value) {
        this.destination = destination;
        this.value = value;
    }

    /**
     * Used for the Visitor pattern. See {@link IRProgramVisitor} for details.
     */
    public <T> T accept(IRProgramVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public String toString() {
        return destination.toString() + " := " + value.toString() + ';';
    }
}
