package type;

public class FloatType extends SimpleType {

    public static final FloatType INSTANCE = new FloatType();

    private FloatType() {
    }

    public String toShortString() {
        return "float";
    }

}
