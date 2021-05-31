package ast;

/**
 * Compares two operands for equality.
 */
public class EqualityExpression extends BinaryOperationExpression {
    public EqualityExpression(int line, int offset, Expression left, Expression right) {
        super(line, offset, left, right);
    }
}
