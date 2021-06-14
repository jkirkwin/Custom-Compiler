package semantic;

import ast.*;
import type.*;

/**
 * Represents an error condition in which an innapropriate type was
 * specified in the source file.
 */
public class TypeMismatchException extends SemanticException {
    
    public TypeMismatchException(Type expected, Type actual, ASTNode node) {
        super("Expected type '" + expected.toString() + "' but found type '" + actual.toString() + "'", node);
    }
}
