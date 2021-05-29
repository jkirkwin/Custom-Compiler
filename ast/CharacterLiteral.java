package ast;

/**
 * Represents a literal character value enclosed in single quotes.
 */
public class CharacterLiteral extends Expression { // TODO Will subsequent steps be harder if we represent all these literals with a single generic Literal<T> class?

    public final char value;

    public CharacterLiteral(int line, int offset, char value) {
        super(line, offset);
        this.value = value;
    }

}

