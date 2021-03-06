package ast;

/**
 * A binary multiplication expression
 */
public class MultiplyExpression extends BinaryOperationExpression {

    public MultiplyExpression(int line, int offset, Expression left, Expression right) {
        super(line, offset, left, right);
    }
    
    public <T> T accept(ASTVisitor<T> visitor) throws ASTVisitorException {
        return visitor.visit(this);
    }

}

