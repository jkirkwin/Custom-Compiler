package ir;

import type.*;

/**
 * Represents a constant in the IR. There are a fixed number of possible
 * contant types, and a factory method is provided for each one.
 */
public class IRConstant<T> implements IRAssignableExpression {

    public static IRConstant<Boolean> forBoolean(boolean value) {
        return new IRConstant<Boolean>(type.BooleanType.INSTANCE, value);
    }

    public static IRConstant<Character> forCharacter(char value) {
        return new IRConstant<Character>(type.CharacterType.INSTANCE, value);
    }

    public static IRConstant<Float> forFloat(float value) {
        return new IRConstant<Float>(type.FloatType.INSTANCE, value);
    }

    public static IRConstant<Integer> forInteger(int value) {
        return new IRConstant<Integer>(type.IntegerType.INSTANCE, value);
    }

    public static IRConstant<String> forString(String value) {
        return new IRConstant<String>(type.StringType.INSTANCE, value);
    }

    private final SimpleType simpleType;
    private final T value;

    private IRConstant(SimpleType t, T value) {
        assert t != null;
        assert value != null;

        simpleType = t;
        this.value = value;
    }

    /**
     * Used for the Visitor pattern. See {@link IRProgramVisitor} for details.
     */
    public <T> T accept(IRProgramVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public SimpleType type() {
        return simpleType;
    }

    public T value() {
        return value;
    }

    public String toString() {
        if (value instanceof Boolean) {
            return value.toString().toUpperCase();
        }
        else if (value instanceof String) {
            return '"' + value.toString() + '"';
        }
        else if (value instanceof Character) {
            return "'" + value.toString() + "'";
        }
        else {
            return value.toString();
        }
    }
}
