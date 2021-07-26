package type;

/**
 * Represents a simple or compound type in the unnamed language.
 */
public abstract class Type {

    /**
     * Returns a short string representation of the type for use
     * during intermediate code generation.
     */
    public abstract String toIRString(); 

    /**
     * Returns the string representation used for the type by the
     * Jasmin assembler.
     */
    public abstract String toJasminString();

    public abstract boolean equals(Object o);

}
