package semantic;

import ast.*;

/**
 * A checked exception thrown by a visitor in response to a violation of 
 * language semantics including type errors.
 */
public class SemanticException extends ASTVisitorException {

    /**
     * Construct a SemanticException with no positional information.
     */
    public SemanticException(String message) {
        super(message);
    }

    /**
     * Construct a SemanticException associated with a specific position
     * in the source code.
     * @param message A message describing the error
     * @param astNode An AST node giving the position of the error in the 
     *                source file
     */
    public SemanticException(String message, ASTNode astNode) {
        super(message, astNode);
    }

    /**
     * Retirieves a string describing the cause of the exception and the 
     * corresponding position(s) in the source code which caused the error
     * if any such positions exist.
     * 
     * The result will be formatted as 
     *      ERROR: <Line Number>:<Line Offset> | <Description>
     * if positional information is meaningful, or as
     *      ERROR: <Description>
     * if there is not position associated with the error.
     */
    public String getMessageWithPosition() {
        if (hasPosition()) {
            return "ERROR: " + line + ":" + offset + " | " + getMessage();
        }
        else {
            return "ERROR: " + getMessage();
        }
    }

    /**
     * Answers whether this exception is tied to a specific
     * position in the source code.
     */
    public boolean hasPosition() {
        return line >= 0 && offset >= 0;
    }
}
