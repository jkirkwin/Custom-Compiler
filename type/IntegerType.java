package type;

public class IntegerType extends SimpleType {

    public static final IntegerType INSTANCE = new IntegerType();

    private IntegerType() {
    }
    
    @Override
    public String toString() {
        return "int";
    }
    
    @Override
    public String toIRString() {
        return "I";
    }

    @Override
    public String toJasminString() {
        return "I";
    }
}
