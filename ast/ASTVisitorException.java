package ast;

/**
 * An error type which may be emitted by an {@link ASTVisitor}
 */
public class ASTVisitorException extends Exception {

    protected final int line;
    protected final int offset;

    /**
     * Create a new ASTVisitorException which is not tied to any specific
     * position in the input file. 
     */
    public ASTVisitorException(String message) {
        super(message);
        line = -1;
        offset = -1;
    }

    /**
     * Create a new ASTVisitorException which is tied to the position 
     * of the provided AST node.
     */
    public ASTVisitorException(String message, ASTNode astNode) {
        super(message);
        this.line = astNode.line;
        this.offset = astNode.offset;
    }
}
