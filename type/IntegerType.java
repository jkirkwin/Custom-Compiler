package type;

public class IntegerType extends SimpleType {

    public static final IntegerType INSTANCE = new IntegerType();

    private IntegerType() {
    }
    
    public String toShortString() {
        return "int";
    }

}
