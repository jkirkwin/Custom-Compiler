package ast;

/**
 * An expression representing a binary addition operation.
 */
public class AddExpression extends BinaryOperationExpression {
 
    public AddExpression(int line, int offset, Expression left, Expression right) {
        super(line, offset, left, right);
    }

}

