package ir;

import common.Label;
import type.BooleanType;

public class ConditionalJumpInstruction extends JumpInstruction {

    private final Temporary condition;

    /**
     * Create a new conditional jump instruction that will go to the 
     * specified label if the given temporary has the value true.
     */
    public ConditionalJumpInstruction(Temporary condition, Label target) {
        super(target);
        assert condition.type() == type.BooleanType.INSTANCE;
        this.condition = condition;
    }

    /**
     * Used for the Visitor pattern. See {@link IRProgramVisitor} for details.
     */
    public <T> T accept(IRProgramVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return "IF " + condition.toString() + " " + super.toString();
    }
}