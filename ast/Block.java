package ast;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Represents a block in the unnamed language, which is a 
 * list of zero or more statements between curly braces.
 */
public class Block extends ASTNode {

    /**
     * An **immutable** view of the list of statements contained
     * in this block
     */
    public final List<Statement> statements;

    public Block(int line, int offset, List<Statement> statements) {
        super(line, offset);

        List<Statement> listCopy = new ArrayList<Statement>(statements);
        this.statements = Collections.unmodifiableList(listCopy);
    }

}
