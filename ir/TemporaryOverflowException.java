package ir;

import ast.ASTNode;
import ast.ASTVisitorException;

/**
 * Thrown when the number of temporaries generated to form the IR
 * of the input program exceeds the maximum allowable limit.
 */
public class TemporaryOverflowException extends ASTVisitorException {

    private static final String ERROR_MESSAGE = 
        "Exceeded the maximum number of temporaries supported by the IR format";

    public TemporaryOverflowException(ASTNode node) {
        super(ERROR_MESSAGE, node);
    }

    public TemporaryOverflowException() {
        super(ERROR_MESSAGE);
    }

}
