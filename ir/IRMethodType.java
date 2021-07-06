package ir;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import type.Type;

/**
 * Represents the type of a method in the IR.
 * Contains a return type and zero or more ordered argument types.
 */
public class IRMethodType {

    public static class Builder {
        private Type returnType = null;
        private final List<Type> argumentTypes = new ArrayList<Type>();

        public Builder withReturnType(Type retType) {
            returnType = retType;
            return this;
        }

        public Builder addArgumentType(Type argType) {
            argumentTypes.add(argType);
            return this;
        }

        public IRMethodType build() {
            return new IRMethodType(returnType, argumentTypes);
        }
    }

    public final Type returnType;
    public final List<Type> argumentTypes;

    public IRMethodType(Type returnType) {
        assert returnType != null;

        this.returnType = returnType;
        argumentTypes = new ArrayList<Type>();
    }

    public IRMethodType(Type returnType, List<Type> argTypes) {
        assert returnType != null;
        assert argTypes != null;

        this.returnType = returnType;
        argumentTypes = Collections.unmodifiableList(new ArrayList<Type>(argTypes));
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("(");

        for (var t : argumentTypes) {
            sb.append(t.toIRString());
        }

        sb.append(')')
          .append(returnType.toIRString());

        return sb.toString();
    }
}