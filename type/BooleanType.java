package type;

public class BooleanType extends SimpleType {

    public static final BooleanType INSTANCE = new BooleanType();

    private BooleanType() {
    }

    public String toShortString() {
        return "boolean";
    }

}
