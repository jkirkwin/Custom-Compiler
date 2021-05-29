package ast;

/**
 * Represents a literal character value enclosed in single quotes.
 */
public class CharacterLiteral extends Expression {

    public final char value;

    public CharacterLiteral(int line, int offset, char value) {
        super(line, offset);
        this.value = value;
    }

}

