package ast;

import java.util.List;
import java.util.ArrayList;

/**
 * A binary multiplication expression
 */
public class MultiplyExpression extends BinaryOperationExpression {

    public MultiplyExpression(int line, int offset, Expression left, Expression right) {
        super(line, offset, left, right);
    }

}

