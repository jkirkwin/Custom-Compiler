package ir;

import type.Type;

/**
 * Repesents a "true" temporary which does not refer to any named object in
 * the source code.
 */
public class TrueTemp extends Temporary {

    TrueTemp(int id, int offset, Type type) {
        super(id, offset, type);
    }

    public String toString() {
        return "T" + globalIndex();
    }

    /**
     * Used for the Visitor pattern. See {@link IRProgramVisitor} for details.
     */
    public <T> T accept(IRProgramVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public boolean hasAlias() {
        return false;
    }

    public String getAlias() {
        throw new UnsupportedOperationException("Temporary has no alias");
    }

    public String getRawAlias() {
        throw new UnsupportedOperationException("Temporary has no alias");
    }
}
