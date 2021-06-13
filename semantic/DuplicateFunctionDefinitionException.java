package semantic;

import ast.*;

public class DuplicateFunctionDefinitionException extends SemanticException {

    private FunctionDecl first;
    private FunctionDecl second;

    public DuplicateFunctionDefinitionException(FunctionDecl first, FunctionDecl second) {
        super ("Duplicate function name '" + first.identifier.value + "'", first.identifier, second.identifier);  
    }
}