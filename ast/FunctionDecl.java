package ast;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

/**
 * The declaration of a function includes its return type, name, and 
 * formal parameters.
 */
public class FunctionDecl extends ASTNode {

    public final TypeNode typeNode;
    public final Identifier identifier;
    public final List<FormalParameter> formals;

    public FunctionDecl(TypeNode typeNode, Identifier identifier, List<FormalParameter> formals) {
        super(typeNode);

        this.typeNode = typeNode;
        this.identifier = identifier;
        this.formals = Collections.unmodifiableList(new ArrayList<FormalParameter>(formals));
    }

}
