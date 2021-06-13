package ast;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

/**
 * The body of a function is defined to be a sequence of variable declarations
 * followed by a list of statements.
 */
public class FunctionBody extends ASTNode {

    /**
     * An unmodifiable view of the list of declarations.
     */
    public final List<VariableDeclaration> declarations;
    
    /**
     * An unmodifiable view of the list of statements.
     */
    public final List<Statement> statements;

    public FunctionBody(int line, int offset, List<VariableDeclaration> declarations, List<Statement> statements) {
        super(line, offset);

        this.declarations = Collections.unmodifiableList(new ArrayList<VariableDeclaration>(declarations));
        this.statements = Collections.unmodifiableList(new ArrayList<Statement>(statements));
    }

    public <T> T accept(ASTVisitor<T> visitor) throws ASTVisitorException {
        return visitor.visit(this);
    }


}
