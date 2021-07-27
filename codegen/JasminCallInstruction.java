package codegen;

/**
 * A Jasmin function call
 */
public class JasminCallInstruction implements JasminStatement {

    public final JasminMethodSignature targetSignature;

    public JasminCallInstruction(JasminMethodSignature targetSignature) {
        this.targetSignature = targetSignature;
    }

}
