package ast;

import java.util.Optional;

/**
 * Represents an if statement with an optional else clause.
 */
public class IfStatement extends Statement {

    public final Expression condition;
    public final Block ifBlock;
    public final Optional<Block> elseBlock;

    /**
     * Construct an if statement with no else clause.
     */
    public IfStatement(int line, int offset, Expression condition, Block ifBlock) {
        super(line, offset);

        assert condition != null;
        assert ifBlock != null;

        this.condition = condition;
        this.ifBlock = ifBlock;
        this.elseBlock = Optional.empty();
    }
 
    /**
     * Construct an if statement with an else clause.
     */
    public IfStatement(int line, int offset, Expression condition, Block ifBlock, Block elseBlock) {
        super(line, offset);

        assert condition != null;
        assert ifBlock != null;
        assert elseBlock != null;

        this.condition = condition;
        this.ifBlock = ifBlock;
        this.elseBlock = Optional.of(elseBlock);
    }

    public <T> T accept(ASTVisitor<T> visitor) throws ASTVisitorException {
        return visitor.visit(this);
    }

}
