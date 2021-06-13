package ast;

/**
 * Represents an expression of the form id[e] where id is the
 * name of an array and e is an expression whose value selects
 * an index in that array.
 */
public class ArrayReference extends Expression {

    public final Identifier id;
    public final Expression indexExpression;

    public ArrayReference(Identifier id, Expression indexExpression) {
        super(id);

        this.id = id;
        this.indexExpression = indexExpression;
    }

    public <T> T accept(ASTVisitor<T> visitor) throws ASTVisitorException {
        return visitor.visit(this);
    }

}
