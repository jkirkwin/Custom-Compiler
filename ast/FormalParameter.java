package ast;

/**
 * Represents a formal parameter (type and name) in a function declaration.
 */
public class FormalParameter extends ASTNode {

    private TypeNode typeNode;
    private Identifier identifier;

    public FormalParameter(TypeNode typeNode, Identifier identifier) {
        super (typeNode);

        this.typeNode = typeNode;
        this.identifier = identifier;
    }

}
