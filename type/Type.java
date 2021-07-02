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

    public abstract boolean equals(Object o);

}
