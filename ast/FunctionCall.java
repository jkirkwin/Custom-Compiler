package ast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents the invokation of a function with a list of zero or more arguments.
 */
public class FunctionCall extends Expression {

    /**
     * The name of the function
     */
    public final Identifier id;

    /**
     * An **immutable** view of the list of argument expressions.
     */
    public final List<Expression> arguments;

    public FunctionCall(Identifier id, List<Expression> arguments) {
        super(id);

        this.id = id;

        // Store an unmodifiable view of a copy of the provided list of arguments.
        var listCopy = new ArrayList<Expression>(arguments);
        this.arguments = Collections.unmodifiableList(listCopy);
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

}
