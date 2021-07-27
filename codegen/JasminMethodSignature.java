package codegen;

import type.MethodType;

/**
 * Simple class that wraps up the data associated with a Jasmin 
 * method signature.
 */
public class JasminMethodSignature {

    public boolean isStatic;
    public MethodType methodType;
    public String methodName;

    public JasminMethodSignature(boolean isStatic, MethodType methodType, String methodName) {
        this.isStatic = isStatic;
        this.methodType = methodType;
        this.methodName = methodName;    
    }

}
