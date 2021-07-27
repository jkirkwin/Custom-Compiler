package codegen;

import common.Label;

/**
 * Represents a Jasmin label declaration.
 */
public class JasminLabelInstruction implements JasminStatement {
 
    public final Label label;

    public JasminLabelInstruction(Label l) {
        label = l;
    }

    public String toString() {
        return label.toString() + ':';
    }

}
