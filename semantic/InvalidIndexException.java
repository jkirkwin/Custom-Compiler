package semantic;

import ast.*;
import type.*;

public class InvalidIndexException extends SemanticException {
    
    public InvalidIndexException(Type indexType, ASTNode node) {
        super("Cannot use '" + indexType.toString() + "' as array index", node);
    }

}
