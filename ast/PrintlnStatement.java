package ast;

/**
 * A statement which prints the reuslt of an expression followed 
 * by a newline character.
 */
public class PrintlnStatement extends Statement {

    public final Expression expression;

    public PrintlnStatement(int line, int offset, Expression expression) {
        super(line, offset);

        this.expression = expression;
    }

    public <T> T accept(ASTVisitor<T> visitor) throws ASTVisitorException {
        return visitor.visit(this);
    }

}
