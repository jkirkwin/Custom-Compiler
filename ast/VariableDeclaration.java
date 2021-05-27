package ast;

/**
 * TODO Add a docstring
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
