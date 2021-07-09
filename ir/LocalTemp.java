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

    public String sourceId() {
        return sourceId;
    }

    public String toString() {
        return "T" + globalIndex();
    }

    protected boolean hasAlias() {
        return true;
    }

    protected String getAlias() {
        return "L(\"" + sourceId + "\")";
    }
    
}

