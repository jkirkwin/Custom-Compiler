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
}
