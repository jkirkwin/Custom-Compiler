package semantic;

import ast.*;
import type.*;

public class InvalidOperandTypeException extends SemanticException {
    public InvalidOperandTypeException(Type type, String operatorString, ASTNode node) {
        super("Cannot apply " + operatorString + " operator to type '" + type.toString() + "'", node);
    }
}
