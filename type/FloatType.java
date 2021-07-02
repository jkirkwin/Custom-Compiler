package type;

public class FloatType extends SimpleType {

    public static final FloatType INSTANCE = new FloatType();

    private FloatType() {
    }

    @Override
    public String toString() {
        return "float";
    }

    @Override
    public String toIRString() {
        return "F";
    }
}
