package ast;

import type.ArrayType;

/**
 * An AST node which represents am array type.
 */
public class ArrayTypeNode extends TypeNode {

    public ArrayTypeNode(int line, int offset, ArrayType type) {
        super(line, offset, type);
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

}
