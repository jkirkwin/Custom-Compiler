package type;

public class VoidType extends SimpleType {

    public static final VoidType INSTANCE = new VoidType();

    public String toShortString() {
        return "void";
    }

    private VoidType() {
    }

}
