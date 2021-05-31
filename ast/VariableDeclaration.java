package ast;

/**
 * Represents the declaration of a variable. This includes
 * the type and name of that variable. Initial values provided
 * at declaration are not supported.
 */
public class VariableDeclaration extends ASTNode {

    public final TypeNode typeNode;
    public final Identifier id;

    public VariableDeclaration(TypeNode typeNode, Identifier id) {
        super(typeNode); // Use same line/offset as the type

        this.typeNode = typeNode;
        this.id = id;
    }

}
