package ast;

/**
 * Represents a literal floating point value.
 */
public class FloatLiteral extends Expression {

    public final float value;

    public FloatLiteral(int line, int offset, float value) {
        super(line, offset);
        this.value = value;
    }

}

