package ast;

/**
 * Represents a formal parameter (type and name) in a function declaration.
 */
public class FormalParameter extends ASTNode {

    public final TypeNode typeNode;
    public final Identifier identifier;

    public FormalParameter(TypeNode typeNode, Identifier identifier) {
        super (typeNode);

        this.typeNode = typeNode;
        this.identifier = identifier;
    }

    public <T> T accept(ASTVisitor<T> visitor) throws ASTVisitorException {
        return visitor.visit(this);
    }
}
