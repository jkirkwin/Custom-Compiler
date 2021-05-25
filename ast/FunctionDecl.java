package ast;

/**
 * The declaration of a function includes its return type, name, and 
 * formal parameters.
 */
public class FunctionDecl extends ASTNode {

    public final TypeNode typeNode;
    public final Identifier identifier;

    public FunctionDecl(TypeNode typeNode, Identifier identifier) {
        this.typeNode = typeNode; // TODO Needs formal params
        // TODO call superclass constructor with appropriate line and offset. Use identifier or type?
        this.identifier = identifier;
    }

}
