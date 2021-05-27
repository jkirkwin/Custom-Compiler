package ast;

import java.util.List;

/**
 * The declaration of a function includes its return type, name, and 
 * formal parameters.
 */
public class FunctionDecl extends ASTNode {

    public final TypeNode typeNode;
    public final Identifier identifier;
    public final List<FormalParameter> formals;

    public FunctionDecl(TypeNode typeNode, Identifier identifier, List<FormalParameter> formals) {
        super(typeNode); // Use the line and offset values of the type node

        this.typeNode = typeNode;
        this.identifier = identifier;
        this.formals = formals;
    }

}
