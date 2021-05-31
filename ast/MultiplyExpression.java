package ast;

/**
 * A binary multiplication expression
 */
public class MultiplyExpression extends BinaryOperationExpression {

    public MultiplyExpression(int line, int offset, Expression left, Expression right) {
        super(line, offset, left, right);
    }

}

