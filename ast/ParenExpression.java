package ast;

/**
 * A wrapper around an {@link Expression} which was surrounded by 
 * parentheses in the source code. This is used to allow more accurate
 * code re-creation from the AST (i.e. it lets the pretty printer 
 * re-create the parentheses as they were in the source file).
 */
public class ParenExpression extends Expression {

    public final Expression expression;

    public ParenExpression(int line, int offset, Expression expression) {
        super(line, offset);
        this.expression = expression;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
