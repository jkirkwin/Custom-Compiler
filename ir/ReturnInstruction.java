package ir;

import java.util.Optional;

/**
 * Represents a return instruction which may or may not have
 * a return value.
 */
public class ReturnInstruction implements IRInstruction {

    private final Optional<Temporary> operand;

    public ReturnInstruction() {
        operand = Optional.empty();
    }

    public ReturnInstruction(Temporary operand) {
        this.operand = Optional.of(operand);
    }

    public String toString() {
        if (operand.isPresent()) {
            return  "RETURN " + operand.get().toString() + ';';
        }
        else {
            return "RETURN;";
        }
    }
}