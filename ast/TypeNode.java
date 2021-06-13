package ast;

import type.Type;

/**
 * An AST node which represents a Type, either compound (Array) or simple.
 */
public abstract class TypeNode extends ASTNode {

    public final Type type;

    public TypeNode(int line, int offset, Type type) {
        super(line, offset);
        this.type = type;
    }

    public <T> T accept(ASTVisitor<T> visitor) throws ASTVisitorException {
        return visitor.visit(this);
    }

}
