package ast;

/**
 * A simple statement consisting of an expression followed
 * by a semi colon.
 */
public class ExpressionStatement extends Statement {

    public final Expression expression;

    public ExpressionStatement(Expression expression) {
        super(expression);
        this.expression = expression;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
