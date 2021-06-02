package ast;

/**
 * Represents the while loop construct. 
 * Comprised of a conditional {@link Expression} and a {@link Block}.
 */
public class WhileStatement extends Statement {

    public final Expression condition;
    public final Block block;

    public WhileStatement(int line, int offset, Expression condition, Block block) {
        super(line, offset);

        this.condition = condition;
        this.block = block;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

}
