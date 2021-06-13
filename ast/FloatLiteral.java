package ast;

/**
 * Represents a literal floating point value.
 */
public class FloatLiteral extends Expression {

    public final float value;

    public FloatLiteral(int line, int offset, float value) {
        super(line, offset);
        this.value = value;
    }

    public <T> T accept(ASTVisitor<T> visitor) throws ASTVisitorException {
        return visitor.visit(this);
    }
    
}

