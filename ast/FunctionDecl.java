package ast;

/**
 * The declaration of a function includes its return type, name, and 
 * formal parameters.
 */
public class FunctionDecl extends ASTNode {

    public final TypeNode typeNode;

    public FunctionDecl(TypeNode typeNode) {
        this.typeNode = typeNode; // TODO Needs formal params and an id
        // TODO call superclass constructor with appropriate line and offset. Use identifier or type?
    }

}
