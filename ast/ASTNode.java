package ast;

/**
 * The root of the abstract syntax tree class hierarchy. 
 * All nodes in the AST will extend this class.
 */
public class ASTNode {

    private static final int DEFAULT_LINE = -1;
    private static final int DEFAULT_OFFSET = -1;

    /**
     * The line on which the entity represented by this node was found in the 
     * source file.
     */
    public final int line;

    /**
     * The offset (i.e. column) at which the entity represented by this node
     * was found in the source file.
     */
    public final int offset;

    /**
     * Constructs an ASTNode with default values for {@link line} and
     * {@link offset}.
     */
    protected ASTNode() {
        line = DEFAULT_LINE;
        offset = DEFAULT_OFFSET;
    }

    /**
     * Constructs an ASTNode with the provided values for {@link line} and
     * {@link offset}.
     */
    protected ASTNode(int line, int offset) {
        this.line = line;
        this.offset = offset;
    }

}