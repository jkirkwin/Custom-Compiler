package ast;

import type.SimpleType;

/**
 * An AST node which represents a simple (non-array) type.
 */
public class SimpleTypeNode extends TypeNode {

    public SimpleType getSimpleType() {
        return (SimpleType) super.type;
    }

    public SimpleTypeNode(int line, int offset, SimpleType type) {
        super(line, offset, type);
    }

}
