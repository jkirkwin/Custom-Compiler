package type;

/**
 * Type-related helper methods
 */
public final class TypeUtils {
    public static boolean isBoolean(Type t) {
        return t == BooleanType.INSTANCE;
    }

    public static boolean isChar(Type t) {
        return t == CharacterType.INSTANCE;
    }

    public static boolean isInt(Type t) {
        return t == IntegerType.INSTANCE;
    }

    public static boolean isFloat(Type t) {
        return t == FloatType.INSTANCE;
    }

    public static boolean isString(Type t) {
        return t == StringType.INSTANCE;
    }

    public static boolean isArray(Type t) {
        return t instanceof ArrayType;
    }

}
