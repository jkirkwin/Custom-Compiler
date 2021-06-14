package semantic;

import ast.*;
import type.*;

public class UnprintableTypeException extends SemanticException {

    public UnprintableTypeException(Type t, ASTNode node) {
        super("Cannot print expression of type '" + t.toString() + "'", node);
    }

}
