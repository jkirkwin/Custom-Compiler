package semantic;

import ast.*;
import type.*;

public class NonArrayIndexException extends SemanticException {
    
    public NonArrayIndexException(Identifier id, Type type) {
        super ("Cannot index into non-array '" + id.value + "' of type '" + type.toString() + "'", id);
    }

}
