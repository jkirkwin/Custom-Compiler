package type;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents the type of a method.
 * Contains a return type and zero or more ordered argument types.
 */
public class MethodType {

    /**
     * Used to more easily construct a MethodType.
     */
    public static class Builder {
        private Type returnType = VoidType.INSTANCE;
        private final List<Type> argumentTypes = new ArrayList<Type>();

        /**
         * Creates a new Builder for a MethodType with 
         * return type void and no arguments.
         */
        public Builder withReturnType(Type retType) {
            returnType = retType;
            return this;
        }

        public Builder addArgumentType(Type argType) {
            argumentTypes.add(argType);
            return this;
        }

        public MethodType build() {
            return new MethodType(returnType, argumentTypes);
        }
    }

    public final Type returnType;
    public final List<Type> argumentTypes; // Immutable so can be public

    /**
     * Construct a new MethodType with no arguments.
     */
    public MethodType(Type returnType) {
        assert returnType != null;

        this.returnType = returnType;
        argumentTypes = new ArrayList<Type>();
    }

    public MethodType(Type returnType, List<Type> argTypes) {
        assert returnType != null;
        assert argTypes != null;

        this.returnType = returnType;
        argumentTypes = Collections.unmodifiableList(new ArrayList<Type>(argTypes));
    }

    // TODO Rename to toIRString
    public String toString() {
        StringBuilder sb = new StringBuilder("(");

        for (var t : argumentTypes) {
            sb.append(t.toIRString());
        }

        sb.append(')')
          .append(returnType.toIRString());

        return sb.toString();
    }

    // TODO add toJasminString?
}
