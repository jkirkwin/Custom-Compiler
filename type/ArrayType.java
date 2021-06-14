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

    @Override
    public boolean equals(Object other) {
        if (other != null && other instanceof ArrayType) {
            ArrayType otherArrayType = (ArrayType) other;
            return size == otherArrayType.size && simpleType.equals(otherArrayType.simpleType);
        }
        
        return false;
    }

}
