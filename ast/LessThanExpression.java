package ast;

/**
 * A binary operation answering whether the left operand is
 * less than the right operand.
 */
public class LessThanExpression extends BinaryOperationExpression {
    
    public LessThanExpression(int line, int offset, Expression left, Expression right) {
        super(line, offset, left, right);
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

}

