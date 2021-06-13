package semantic;

import ast.*;
import type.*;

/**
 * A visitor which traverses the abstract syntax tree and verifies
 * semantic predicates and type-system conformance.
 */
public class TypeCheckVisitor implements ASTVisitor<Type>  {

    private final Environment<String, Type> variableEnv;
    private final Environment<String, FunctionDecl> functionEnv;

    public TypeCheckVisitor() {
        variableEnv = new Environment<String, Type>();
        functionEnv = new Environment<String, FunctionDecl>();
    }

    public Type visit(AddExpression node) throws SemanticException {
        return null; // TODO
    }

	public Type visit(ArrayAssignmentStatement node) throws SemanticException {
        return null; // TODO
    }

	public Type visit(ArrayReference node) throws SemanticException {
        return null; // TODO
    }

	public Type visit(ArrayTypeNode node) throws SemanticException {
        return null; // TODO
    }

	public Type visit(AssignmentStatement node) throws SemanticException {
        return null; // TODO
    }

	public Type visit(ASTNode node) throws SemanticException {
        return null; // TODO
    }

	public Type visit(BinaryOperationExpression node) throws SemanticException {
        return null; // TODO
    }

	public Type visit(Block node) throws SemanticException {
        return null; // TODO
    }

	public Type visit(BooleanLiteral node) throws SemanticException {
        return null; // TODO
    }

	public Type visit(CharacterLiteral node) throws SemanticException {
        return null; // TODO
    }

	public Type visit(EqualityExpression node) throws SemanticException {
        return null; // TODO
    }

	public Type visit(Expression node) throws SemanticException {
        return null; // TODO
    }

	public Type visit(ExpressionStatement node) throws SemanticException {
        return null; // TODO
    }

	public Type visit(FloatLiteral node) throws SemanticException {
        return null; // TODO
    }

	public Type visit(FormalParameter node) throws SemanticException {
        return null; // TODO
    }

	public Type visit(FunctionBody node) throws SemanticException {
        return null; // TODO
    }

	public Type visit(FunctionCall node) throws SemanticException {
        return null; // TODO
    }

	public Type visit(FunctionDecl node) throws SemanticException {
        return null; // TODO
    }

	public Type visit(Function node) throws SemanticException {
        return null; // TODO
    }

	public Type visit(Identifier node) throws SemanticException {
        return null; // TODO
    }

	public Type visit(IfStatement node) throws SemanticException {
        return null; // TODO
    }

	public Type visit(IntLiteral node) throws SemanticException {
        return null; // TODO
    }

	public Type visit(LessThanExpression node) throws SemanticException {
        return null; // TODO
    }

	public Type visit(MultiplyExpression node) throws SemanticException {
        return null; // TODO
    }

	public Type visit(ParenExpression node) throws SemanticException {
        return null; // TODO
    }

	public Type visit(PrintlnStatement node) throws SemanticException {
        return null; // TODO
    }

	public Type visit(PrintStatement node) throws SemanticException {
        return null; // TODO
    }

	public Type visit(Program node) throws ASTVisitorException {
        // Add all functions to the symbol table so they are all accessible
        // when type checking each one.
        for (var function : node.functions) {
            var functionDecl = function.declaration;
            String functionName = functionDecl.identifier.value;

            // Prevent the re-use of a function name
            if (functionEnv.exists(functionName)) {
                var duplicate = functionEnv.lookup(functionName);
                throw new DuplicateFunctionDefinitionException(duplicate, functionDecl);
            }

            functionEnv.bind(functionName, functionDecl);
        }

        // Check for a main() function with the appropriate signature
        if (! functionEnv.exists("main")) {
            throw new SemanticException("Missing main() function");
        }
        else {
            var mainDecl = functionEnv.lookup("main");

            Type mainType = mainDecl.typeNode.type;
            if (! mainType.equals(VoidType.INSTANCE)) {
                throw new SemanticException("Invalid main() declaration: return type '" + mainType.toString() + "' but expected 'void'", mainDecl);
            }

            if (! mainDecl.formals.isEmpty()) {
                throw new SemanticException("Invalid main() declaration: main() must not take formal parameters", mainDecl);
            }
        }

        // Main exists and is well formed. Now type check each function.
        for (Function function : node.functions) {
            node.accept(this);
        }

        // Nothing to return
        return null;
    }

	public Type visit(ReturnStatement node) throws SemanticException {
        return null; // TODO
    }

	public Type visit(SimpleTypeNode node) throws SemanticException {
        return null; // TODO
    }

	public Type visit(Statement node) throws SemanticException {
        return null; // TODO
    }

	public Type visit(StringLiteral node) throws SemanticException {
        return null; // TODO
    }

	public Type visit(SubtractExpression node) throws SemanticException {
        return null; // TODO
    }

	public Type visit(TypeNode node) throws SemanticException {
        return null; // TODO
    }

	public Type visit(VariableDeclaration node) throws SemanticException {
        return null; // TODO
    }

	public Type visit(WhileStatement node) throws SemanticException {
        return null; // TODO
    }


}
