package semantic;

import ast.*;

/**
 * A checked exception thrown by a visitor in response to a violation of 
 * language semantics including type errors.
 */
public class SemanticException extends ASTVisitorException {

    /**
     * Formats a string representing the position of a token in the 
     * source code.
     */
    protected static String getPositionString(int line, int offset) {
        return line + ":" + offset;
    }

    protected static String getPositionString(ASTNode node) {
        return getPositionString(node.line, node.offset);
    }

    /**
     * Construct a SemanticException with no positional information.
     */
    public SemanticException(String message) {
        super(message);
        posString = "";
    }

    /**
     * Construct a SemanticException associated with a single position
     * in the source code.
     */
    public SemanticException(String message, ASTNode astNode) {
        super(message, astNode);
        posString = getPositionString(astNode);
    }

    /**
     * Construct a SemanticException associated with two different positions
     * in the source code.
     */
    public SemanticException(String message, ASTNode first, ASTNode second) {
        super(message, first);
        posString = getPositionString(first) + ", " + getPositionString(second);
    }

    private final String posString;

    /**
     * Retirieves a string describing the cause of the exception and the 
     * corresponding position(s) in the source code which caused the error
     * if any such positions exist.
     */
    public String getMessageWithPosition() { // TODO Update this to have the format 'ERROR:line:offset: <Message>'
        if (hasPosition()) {
            return getMessage() + " at " + posString;
        }
        else {
            return getMessage();
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
