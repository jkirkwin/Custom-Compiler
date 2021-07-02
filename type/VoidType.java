package type;

public class VoidType extends SimpleType {

    public static final VoidType INSTANCE = new VoidType();

    @Override
    public String toString() {
        return "void";
    }

    private VoidType() {
    }

    @Override
    public String toIRString() {
        return "V";
    }
}
