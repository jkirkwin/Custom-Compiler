package type;

public class ArrayType extends Type {
    
    public final int size;
    public final SimpleType simpleType;

    private final String description;

    public ArrayType(int size, SimpleType simpleType) {
        this.size = size;
        this.simpleType = simpleType;

        this.description = simpleType.toShortString() + "[" + size + "]";
    }

    public String toShortString() {
        return description;
    }

}
