package ast;

/**
 * Represents a use or declaration of an identifier.
 */
public class Identifier extends Expression {

    public final String value;

    /**
     * Construct an identifier with the given string value and
     * position metadata.
     */
    public Identifier(String value, int line, int offset) {
        super(line, offset);
        this.value = value;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
