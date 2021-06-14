package semantic;

import ast.*;
import type.*;

/**
 * A visitor which traverses the abstract syntax tree and verifies
 * semantic predicates and type-system conformance.
 */
public class TypeCheckVisitor implements ASTVisitor<Type>  {

    private static boolean isVoid(Type type) {
        return VoidType.INSTANCE.equals(type);
    }

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
        ArrayType type = node.getArrayType();

        if (isVoid(type.simpleType)) {
            throw new SemanticException("Invalid array type definition: underlying type must not be 'void'", node);
        }

        return type;
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

	public Type visit(BooleanLiteral node) {
        return BooleanType.INSTANCE;
    }

	public Type visit(CharacterLiteral node) {
        return CharacterType.INSTANCE;
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

	public Type visit(FloatLiteral node) {
        return FloatType.INSTANCE;
    }

	public Type visit(FormalParameter node) throws ASTVisitorException {

        var id = node.identifier;
        String name = id.value;

        // Formal parameters must not have void type
        Type type = node.typeNode.accept(this);
        if (isVoid(type)) {
            throw new SemanticException("Formal parameter '" + name + "' must not be of type 'void'", node);
        }

        // Formals must have unique names.
        if (variableEnv.existsInCurrentScope(name)) {
            throw new SemanticException("Duplicate formal parameter name '" + name + "' in function declaration", id);
        }

        // Add the formal parameter to the current scope and return its
        // (valid) type.
        variableEnv.bind(name, type);
        return type;
    }

	public Type visit(FunctionBody node) throws ASTVisitorException {
        for (var varDecl : node.declarations) {
            varDecl.accept(this);
        }

        for (var statement : node.statements) {
            statement.accept(this);
        }

        // Nothing meaningful to return here
        return null;
    }

	public Type visit(FunctionCall node) throws SemanticException {
        return null; // TODO
    }

	public Type visit(FunctionDecl node) throws ASTVisitorException {

        variableEnv.enterScope();

        for (var formal : node.formals) {
            formal.accept(this);
        }

        return node.typeNode.accept(this);
    }

	public Type visit(Function node) throws ASTVisitorException {

        // TODO Check that variables are defined before being used

        // TODO Check that the declaration type matches the return type - this should be done when we hit a return statement.
        //      May need to keep some state to indicate which function we're currently in. That would also help with checking 
        //      parameters vs local variables.

        Type returnType = node.declaration.accept(this);
        node.body.accept(this);

        return returnType;
    }

	public Type visit(Identifier node) throws SemanticException {
        return null; // TODO
    }

	public Type visit(IfStatement node) throws ASTVisitorException {
        // The type of the condition expression must be a boolean
        Type conditionType = node.condition.accept(this);
        if (!BooleanType.INSTANCE.equals(conditionType)) {
            throw new TypeMismatchException(BooleanType.INSTANCE, conditionType, node.condition);
        }

        // Check the if and else blocks
        node.ifBlock.accept(this);
        if (node.elseBlock.isPresent()) {
            node.elseBlock.get().accept(this);
        }

        // Nothing meaningful to return
        return null;
    }

	public Type visit(IntLiteral node) {
        return IntegerType.INSTANCE;
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
                throw new DuplicateFunctionDefinitionException(functionDecl);
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
            if (!isVoid(mainType)) {
                throw new SemanticException("Invalid main() declaration: return type '" + mainType.toString() + "' but expected 'void'", mainDecl);
            }

            if (!mainDecl.formals.isEmpty()) {
                throw new SemanticException("Invalid main() declaration: main() must not take formal parameters", mainDecl);
            }
        }

        // Main exists and is well formed. Now type check each function.
        for (Function function : node.functions) {
            function.accept(this);
        }

        // Nothing to return
        return null;
    }

	public Type visit(ReturnStatement node) throws SemanticException {
        return null; // TODO
    }

	public Type visit(SimpleTypeNode node) throws SemanticException {
        return node.type;
    }

	public Type visit(Statement node) throws SemanticException {
        return null; // TODO
    }

	public Type visit(StringLiteral node) {
        return StringType.INSTANCE;
    }

	public Type visit(SubtractExpression node) throws SemanticException {
        return null; // TODO
    }

	public Type visit(TypeNode node) {
        // This should never execute. 
        // Only TypeNode's subclasses should be involved in the double dispatch.
        throw new UnsupportedOperationException("Error: Visitor invoked with abstract base class");
    }

	public Type visit(VariableDeclaration node) throws ASTVisitorException {

        var id = node.id;
        var name = id.value;

        // Variables must have a non-void type.
        Type type = node.typeNode.accept(this);
        if (isVoid(type)) {
            throw new SemanticException("Variable '" + name + "' must not be of type 'void'", node);
        }

        // Variables are not allowed to shadow function parameters or other variables.
        if (variableEnv.existsInCurrentScope(name)) {
            throw new SemanticException("Duplicate variable name '" + name + "' encountered", id);
        }
        
        variableEnv.bind(name, type);

        return type;
    }

	public Type visit(WhileStatement node) throws ASTVisitorException {
        // The type of the condition expression must be a boolean
        Type conditionType = node.condition.accept(this);
        if (!BooleanType.INSTANCE.equals(conditionType)) {
            throw new TypeMismatchException(BooleanType.INSTANCE, conditionType, node.condition);
        }

        // Run semantic checks on the block
        node.block.accept(this);
        
        // Nothing meaningful to return
        return null;
    }
}
