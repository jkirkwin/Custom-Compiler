package codegen;

/**
 * Represents a comment in the Jasmin assembly code on its 
 * own line, within a method. 
 */
public class JasminComment implements JasminStatement {

    public final String text;

    public JasminComment(String text) {
        this.text = text;
    }

    /**
     * Construct a JasminComment with the text indented by
     * the given number of tabs.
     */
    public JasminComment(String text, int indentTabs) {
        this.text = "\n".repeat(indentTabs) + text;
    }

}

