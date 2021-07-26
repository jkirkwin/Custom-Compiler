package ir;

import type.Type;

/**
 * Represents a temporary used to hold a named local variable.
 */
public class LocalTemp extends Temporary {

    private String sourceId;

    /**
     * Constructs a new local-temporary with a souce-code-identifier
     * to be used for informative error messages.
     */
    public LocalTemp(int localId, int offset, Type type, String sourceId) {
        super(localId, offset, type);
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
        return "L(\"" + sourceId + "\")";
    }

    public String getRawAlias() {
        return sourceId;
    }
}

