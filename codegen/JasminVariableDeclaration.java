package codegen;

import common.Label;
import ir.Temporary;
import type.Type;

/**
 * Represents a Jasmin .var directive of the form
 *      .var <n> is <name> <type> from <start label> to <end label>
 * 
 * Each of these has a one-to-one correspondence with a temporary 
 * declaration in the IR.
 */
public class JasminVariableDeclaration implements JasminStatement {

    public final int n;
    public final String name;
    public final Type type;
    public final Label startLabel;
    public final Label endLabel;

    public JasminVariableDeclaration(int n,
                                     String name,
                                     Type type,
                                     Label startLabel,
                                     Label endLabel) {
        this.n = n;
        this.name = name;
        this.type = type;
        this.startLabel = startLabel;
        this.endLabel = endLabel;
    }

    public JasminVariableDeclaration(Temporary temp, Label startLabel, Label endLabel) {
        this(
            temp.globalIndex(),
            temp.hasAlias() ? temp.getRawAlias() : temp.toString(),
            temp.type(),
            startLabel,
            endLabel
        );
    }

    public String toJasminString() {
        StringBuilder sb = new StringBuilder();
        return sb.append(".var ")
                 .append(n)
                 .append(" is ")
                 .append(name)
                 .append(" ")
                 .append(type.toJasminString())
                 .append(" from ")
                 .append(startLabel.toJasminString())
                 .append(" to ")
                 .append(endLabel.toJasminString())
                 .toString();
    }
}
