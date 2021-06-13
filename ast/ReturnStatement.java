package ast;

import java.util.Optional;

/**
 * A statement which causes the current function to return. 
 * May or may not include a return value.
 */
public class ReturnStatement extends Statement {

    /**
     * If present, yields the value to be returned.
     */
    public final Optional<Expression> returnExpression;

    /**
     * Construct a new return statement with no return value.
     */
    public ReturnStatement(int line, int offset) {
        super(line, offset);

        returnExpression = Optional.empty();
    }

    /**
     * Construct a new return statement node with the provided return expression.
     */
    public ReturnStatement(int line, int offset, Expression returnExpression) {
        super(line, offset);
        
        assert returnExpression != null;
        this.returnExpression = Optional.of(returnExpression);
    }

    public <T> T accept(ASTVisitor<T> visitor) throws ASTVisitorException {
        return visitor.visit(this);
    }
}
