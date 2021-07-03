package ir;

public class TemporaryAssignmentInstruction implements IRInstruction {

    private final Temporary destination;
    public final IRAssignableExpression value;

    public TemporaryAssignmentInstruction(Temporary destination, IRAssignableExpression value) {
        this.destination = destination;
        this.value = value;
    }

    public String toString() {
        return destination.toString() + " := " + value.toString() + ';';
    }
}
