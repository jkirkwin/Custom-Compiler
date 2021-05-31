package ast;

/**
 * A binary subtraction expression 
 */
public class SubtractExpression extends BinaryOperationExpression {

    public SubtractExpression(int line, int offset, Expression left, Expression right) {
        super(line, offset, left, right);
    }
}

