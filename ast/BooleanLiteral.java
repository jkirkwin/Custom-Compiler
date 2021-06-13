package ast;

/**
 * Represents a literal boolean value.
 */
public class BooleanLiteral extends Expression {

    public final boolean value;

    public BooleanLiteral(int line, int offset, boolean value) {
        super(line, offset);
        this.value = value;
    }

    public <T> T accept(ASTVisitor<T> visitor) throws ASTVisitorException {
        return visitor.visit(this);
    }

}

