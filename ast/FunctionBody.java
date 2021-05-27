package ast;

import java.util.List;
import java.util.ArrayList;

/**
 * The body of a function is defined to be a sequence of variable declarations
 * followed by a list of statements.
 */
public class FunctionBody extends ASTNode {


    public final List<VariableDeclaration> declarations;
    public final List<Statement> statements;

    public FunctionBody(List<VariableDeclaration> declarations, List<Statement> statements) { // TODO add a list of statements
        this.declarations = declarations;
        this.statements = statements;
    }
}
