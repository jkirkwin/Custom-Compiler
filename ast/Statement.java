package ast;

/**
 * A statement can have many forms. All concete statement types 
 * (e.g. if, while, assignment, etc.) should extend this class
 * so that collections of statements can be used in other ASTNodes.
 */
public abstract class Statement extends ASTNode {
    
    /**
     * Forwards to the parent constructor with the same signature.
     */
    public Statement(int line, int offset) {
        super(line, offset);
    }

    /**
     * Forwards to the parent constructor with the same signature.
     */
    public Statement(ASTNode node) {
        super(node);
    }

    public <T> T accept(ASTVisitor<T> visitor) throws ASTVisitorException {
        return visitor.visit(this);
    }
}
