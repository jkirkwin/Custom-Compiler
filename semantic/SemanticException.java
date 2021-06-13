package semantic;

import ast.*;

/**
 * A checked exception thrown by a visitor in response to a violation of 
 * language semantics including type errors.
 */
public class SemanticException extends ASTVisitorException {

    public SemanticException(String message) {
        super(message);
    }

    public SemanticException(String message, ASTNode astNode) {
        super(message, astNode);
    }

}