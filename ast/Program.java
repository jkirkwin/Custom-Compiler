package ast;

import java.util.*;

/**
 * A Program is the root node in the AST and holds a list of functions,
 * one of which should be called 'main()' according to the language 
 * specification.
 */
public class Program extends ASTNode {

    /**
     * The functions included in the program, listed in the order they 
     * were defined in. This list im mutable.
     */
    public final List<Function> functions;

    /**
     * Create a program with multiple functions.
     */
    public Program(Collection<Function> functionCollection) {
        super(0, 0);
        this.functions = Collections.unmodifiableList(new ArrayList<Function>(functionCollection));
    }
}
