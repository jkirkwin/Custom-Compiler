package ast;

import java.util.List;
import java.util.ArrayList;

/**
 * Represents a block in the unnamed language, which is a 
 * list of zero or more statements between curly braces.
 */
public class Block extends ASTNode {

    public final List<Statement> statements;

    public Block(int line, int offset, List<Statement> statements) {
        super(line, offset);
        this.statements = statements;
    }

}
