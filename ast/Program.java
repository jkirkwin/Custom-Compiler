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
     * were defined in. This list *is* mutable to allow the compiler to
     * add functions as they are parsed.
     */
    public final List<Function> functions;

    /**
     * Create a program with a single function.
     */
    public Program(Function function) {
        super(0, 0);
        functions = new ArrayList<Function>();
        functions.add(function);
    }

    /**
     * Create a program with multiple functions.
     */
    public Program(Collection<Function> functionCollection) {
        super(0, 0);
        this.functions = new ArrayList<Function>(functionCollection);
    }
}