package ir;

import type.Type;

/**
 * Represents a temporary used to hold a parameter value.
 */
public class ParamTemp extends Temporary {

    private String sourceId;

    /**
     * Constructs a new parameter-temporary with a souce-code-identifier
     * to be used for informative error messages.
     */
    public ParamTemp(int id, Type type, String sourceId) {
        super(id, 0, type);
        this.sourceId = sourceId;
    }

    /**
     * Used for the Visitor pattern. See {@link IRProgramVisitor} for details.
     */
    public <T> T accept(IRProgramVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public String sourceId() {
        return sourceId;
    }

    public String toString() {
        return "T" + globalIndex();
    }

    public boolean hasAlias() {
        return true;
    }

    public String getAlias() {
        return "P(\"" + sourceId + "\")";
    }

    public String getRawAlias() {
        return sourceId;
    }
    
}
