package ast;

/**
 * Functions are the second level of the AST. Functions can only exist as
 * direct children of a {@link Program}.
 */
public class Function extends ASTNode {

    public final FunctionDecl declaration;
    public final FunctionBody body;

    /**
     * Construct a new Function with the specified declaration and body.
     */
    public Function(FunctionDecl decl, FunctionBody body) {
        super(decl); // Copy the line and offset of the declaration
        this.declaration = decl;
        this.body = body;
    }
 
    public <T> T accept(ASTVisitor<T> visitor) throws ASTVisitorException {
        return visitor.visit(this);
    }

}
