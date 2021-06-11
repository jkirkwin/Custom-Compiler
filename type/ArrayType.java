package type;

public class ArrayType extends Type {
    
    public final int size;
    public final SimpleType simpleType;

    public ArrayType(int size, SimpleType simpleType) {
        this.size = size;
        this.simpleType = simpleType;
    }

    @Override
    public String toString() {
        return simpleType.toString() + "[" + size + "]";
    }

}
