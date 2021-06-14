package semantic;

import ast.*;

public class DuplicateFunctionDefinitionException extends SemanticException {

    public DuplicateFunctionDefinitionException(FunctionDecl functionDecl) {
        super ("Duplicate function name '" + functionDecl.identifier.value + "'", functionDecl.identifier);
    }
}