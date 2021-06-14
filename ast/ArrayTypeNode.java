package ast;

import type.ArrayType;

/**
 * An AST node which represents am array type.
 */
public class ArrayTypeNode extends TypeNode {

    /**
     * Retrieves the underlying ArrayType, preventing client code
     * from performing potentially unsafe casts.
     */
    public final ArrayType getArrayType() {
        return (ArrayType)type;
    }

    public ArrayTypeNode(int line, int offset, ArrayType type) {
        super(line, offset, type);
    }

    public <T> T accept(ASTVisitor<T> visitor) throws ASTVisitorException {
        return visitor.visit(this);
    }

}
