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

    public String sourceId() {
        return sourceId;
    }

    public String toString() {
        return "T" + globalIndex();
    }
    
}
