package ast;

/**
 * Represents an expression of the form `id = e`
 * where id names some variable and e is an expression
 * whose value should be assigned to id.
 */
public class AssignmentStatement extends Statement {

    public final Identifier id;
    public final Expression value;

    public AssignmentStatement(Identifier id, Expression value) {
        super(id);

        this.id = id;
        this.value = value;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

}
