package ast;

/**
 * An AST node which represents a Type, either compound (Array) or simple.
 */
public class TypeNode extends ASTNode {

    // TODO I think this should have a member that is a subclass of the abstract type.Type class.
    public final String typeString;

    /** TODO Remove the string parameter here
     *
     * Constructs a TypeNode with a string value and
     * the given {@link line} and {@link offset} values.
     */
    public TypeNode(String typeString, int line, int offset) {
        super(line, offset);
        this.typeString = typeString;
    }

}
