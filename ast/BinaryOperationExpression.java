package ast;

/**
 * An abstract expression class which represents an unspecified left-associative 
 * binary operation and two sub-expressions on which to operate.
 */
public class BinaryOperationExpression extends Expression {
    public final Expression left;
    public final Expression right;

    /**
     * Construct a new binary operation expression with the provided operands.
     */
    public BinaryOperationExpression(int line, int offset, Expression left, Expression right) {
        super(line, offset); 
        
        this.left = left;
        this.right = right;
    }

    public <T> T accept(ASTVisitor<T> visitor) throws ASTVisitorException {
        return visitor.visit(this);
    }
}
    
