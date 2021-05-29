package ast;

/**
 * Represents a literal integer value.
 */
public class IntLiteral extends Expression {

    public final int value;

    public IntLiteral(int line, int offset, int value) {
        super(line, offset);
        this.value = value;
    }

}

