package ast;

/**
 * TODO Add a docstring
 */
public class Expression extends ASTNode {

    public Expression(ASTNode n) {
        super(n); // TODO Add docstring and whatver else is needed.
    }

    public Expression(int line, int offset) {
        super(line, offset);
    }

    public Expression() {
        super(-1, -1);
        System.out.println("Instantiated Expression with no params. Dummy values set for line and offset");
        // TODO Add call to superclass constructor
        // TODO Add any necessary parameters
    }

    // TODO Should be made abstract once we're ready to add some sub-types

}
