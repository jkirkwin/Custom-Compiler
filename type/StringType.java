package type;

public class StringType extends SimpleType {

    public static final StringType INSTANCE = new StringType();

    private StringType() {
    }

    @Override
    public String toString() {
        return "string";
    }

    @Override
    public String toIRString() {
        return "U";
    }
}
