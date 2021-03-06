package type;

public class BooleanType extends SimpleType {

    public static final BooleanType INSTANCE = new BooleanType();

    private BooleanType() {
    }

    @Override
    public String toString() {
        return "boolean";
    }

    @Override
    public String toIRString() {
        return "Z";
    }

    @Override
    public String toJasminString() {
        return "Z";
    }
}
