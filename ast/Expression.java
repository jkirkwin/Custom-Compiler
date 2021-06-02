package ast;

/**
 * Represents all expression subtypes in the AST.
 */
public abstract class Expression extends ASTNode {

    public Expression(ASTNode n) {
        super(n);
    }

    public Expression(int line, int offset) {
        super(line, offset);
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
