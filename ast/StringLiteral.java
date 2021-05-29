package ast;

/**
 * Represents a String literal value enclosed in double quotes.
 */
public class StringLiteral extends Expression {

    public final String value;

    public StringLiteral(int line, int offset, String value) {
        super(line, offset);
        this.value = value;
    }

}

