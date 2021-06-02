package ast;

/**
 * A statement which prints the result of an an expression..
 */
public class PrintStatement extends Statement {

    public final Expression expression;

    public PrintStatement(int line, int offset, Expression expression) {
        super(line, offset);

        this.expression = expression;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
} 
