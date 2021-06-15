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

    private static boolean isInt(Type type) {
        return IntegerType.INSTANCE.equals(type);
    }

    private static boolean isFloat(Type type) {
        return FloatType.INSTANCE.equals(type);
    }

    private static boolean isChar(Type type) {
        return CharacterType.INSTANCE.equals(type);
    }

    private static boolean isString(Type type) {
        return StringType.INSTANCE.equals(type);
    }

    private static boolean isBoolean(Type type) {
        return BooleanType.INSTANCE.equals(type);
    }

    private static boolean isArray(Type type) {
        return type instanceof ArrayType;
    }

    private final Environment<String, Type> variableEnv;
    private final Environment<String, FunctionDecl> functionEnv;

    public TypeCheckVisitor() {
        variableEnv = new Environment<String, Type>();
        functionEnv = new Environment<String, FunctionDecl>();
    }

    public Type visit(AddExpression node) throws ASTVisitorException {
        Type leftType = node.left.accept(this);
        Type rightType = node.right.accept(this);
        
        // Addition supported for simple types except boolean and void
        if (isVoid(leftType) || isBoolean(leftType) || isArray(leftType)) {
            throw new InvalidOperandTypeException(leftType, "+", node.left);
        }
        else if (!leftType.equals(rightType)) {
            throw new TypeMismatchException(leftType, rightType, node.right);
        }

        return leftType;
    }

	public Type visit(ArrayAssignmentStatement node) throws ASTVisitorException {
        // Check the array id exists and get its underlying type
        Type arrayType = node.arrayId.accept(this);
        assert isArray(arrayType);
        SimpleType expectedType = ((ArrayType)arrayType).simpleType;

        // Check that the index expression yields an integer
        Type indexType = node.indexExpression.accept(this);
        if (!isInt(indexType)) {
            throw new SemanticException("Cannot use '" + indexType.toString() + "' as array index", node.indexExpression);
        }

        // Check the type of the RHS of the assignment statement and ensure that
        // it matches the array component type.
        Type actualType = node.valueExpression.accept(this);
        if (!expectedType.equals(actualType)) {
            throw new TypeMismatchException(expectedType, actualType, node.valueExpression);
        }

        // Nothing meaningful to return here
        return null;
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

	public Type visit(AssignmentStatement node) throws ASTVisitorException {
        Type expectedType = node.id.accept(this);
        Type actualType = node.value.accept(this);

        if (!expectedType.equals(actualType)) {
            throw new TypeMismatchException(expectedType, actualType, node.value);
        }

        // Nothing meaningful to return here
        return null;
    }

	public Type visit(ASTNode node) throws SemanticException {
        // This should never execute. 
        // Only ASTNode subclasses should be involved in the double dispatch.
        throw new UnsupportedOperationException("Error: Visitor invoked with abstract base class");
    }

	public Type visit(BinaryOperationExpression node) throws SemanticException {
        // This should never execute. 
        // Only BinaryOperationExpression subclasses should be involved in the double dispatch.
        throw new UnsupportedOperationException("Error: Visitor invoked with abstract base class");
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

	public Type visit(EqualityExpression node) throws ASTVisitorException {
        Type leftType = node.left.accept(this);
        Type rightType = node.right.accept(this);
        
        // Equality supported for simple, non-void types
        if (isVoid(leftType) || isArray(leftType)) {  // TODO Should we allow array equality comparisons?
            throw new InvalidOperandTypeException(leftType, "==", node.left);
        }
        else if (!leftType.equals(rightType)) {
            throw new TypeMismatchException(leftType, rightType, node.right);
        }

        return BooleanType.INSTANCE;
    }

	public Type visit(Expression node) {
        // This should never execute. 
        // Only Expression subclasses should be involved in the double dispatch.
        throw new UnsupportedOperationException("Error: Visitor invoked with abstract base class");
    }

	public Type visit(ExpressionStatement node) throws ASTVisitorException {
        return node.expression.accept(this);
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

	public Type visit(FunctionCall node) throws ASTVisitorException {
        String functionName = node.id.value;
        var args = node.arguments;

        // The function must exist
        if (!functionEnv.exists(functionName)) {
            throw new SemanticException("No function named '" + functionName + "' is defined", node);
        }

        FunctionDecl functionDeclaration = functionEnv.lookup(functionName); 
        var formals = functionDeclaration.formals;

        // Check that the correct number of arguments were provided
        int expectedArgCount = formals.size();
        int actualArgCount = args.size();
        if (expectedArgCount != actualArgCount) {
            String message = "Function '" + functionName + "' requires " + expectedArgCount + 
                             "arguments but was called with " + actualArgCount;
            throw new SemanticException(message, node);
        }
        
        // Check that each argument matches the type we expect it to have
        for (int i = 0; i < expectedArgCount; ++i) {
            FormalParameter formal = formals.get(i);
            Type expectedType = formal.typeNode.type;

            Expression argExpression = args.get(i); 
            Type actualType = argExpression.accept(this);

            if (!expectedType.equals(actualType)) {
                throw new TypeMismatchException(expectedType, actualType, argExpression);
            }
        }

        return functionDeclaration.typeNode.type;
    }

	public Type visit(FunctionDecl node) throws ASTVisitorException {

        for (var formal : node.formals) {
            formal.accept(this);
        }

        return node.typeNode.accept(this);
    }

	public Type visit(Function node) throws ASTVisitorException {

        // TODO Check that the declaration type matches the return type - this should be done when we hit a return statement.
        //      May need to keep some state to indicate which function we're currently in. That would also help with checking 
        //      parameters vs local variables.

        variableEnv.enterScope();

        Type functionReturnType = node.declaration.accept(this);
        node.body.accept(this);

        variableEnv.exitScope();

        return functionReturnType;
    }

	public Type visit(Identifier node) throws SemanticException {
        // This should only be called when evaluating the identifier 
        // as an expression. E.g. in the following statements
        //          print(foo) // id=foo
        //          i = 1; // id=i
        //
        // This should not be called when type checking anything else,
        // including variable declarations, function calls, etc.

        String name = node.value;
        if (!variableEnv.exists(name)) {
            throw new SemanticException("Undefined reference to variable '" + name + "'", node);
        }

        return variableEnv.lookup(name);
    }

	public Type visit(IfStatement node) throws ASTVisitorException {
        // The type of the condition expression must be a boolean
        Type conditionType = node.condition.accept(this);
        if (!isBoolean(conditionType)) {
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

	public Type visit(LessThanExpression node) throws ASTVisitorException {
        Type leftType = node.left.accept(this);
        Type rightType = node.right.accept(this);
        
        // Comparison supported for simple types except boolean and void
        if (isVoid(leftType) || isBoolean(leftType) || isArray(leftType)) { 
            throw new InvalidOperandTypeException(leftType, "<", node.left);
        }
        else if (!leftType.equals(rightType)) {
            throw new TypeMismatchException(leftType, rightType, node.right);
        }

        return BooleanType.INSTANCE;
    }

	public Type visit(MultiplyExpression node) throws ASTVisitorException {
        Type leftType = node.left.accept(this);
        Type rightType = node.right.accept(this);
        
        // Multiplication supported only for integers and floats
        if (!isInt(leftType) && !isFloat(leftType)) {
            throw new InvalidOperandTypeException(leftType, "*", node.left);
        }
        else if (!leftType.equals(rightType)) {
            throw new TypeMismatchException(leftType, rightType, node.right);
        }

        return leftType;
    }

	public Type visit(ParenExpression node) throws ASTVisitorException {
        return node.expression.accept(this);
    }

    private boolean isPrintableType(Type t) {
        // We allow printing of all simple, non-void types.
        return t != null &&
               !isVoid(t) &&
               !isArray(t);
    }

	public Type visit(PrintlnStatement node) throws ASTVisitorException {
        Type expressionType = node.expression.accept(this);

        if (!isPrintableType(expressionType)) {
            throw new UnprintableTypeException(expressionType, node.expression);
        }

        // Nothing meaningful to return 
        return null;
    }

	public Type visit(PrintStatement node) throws ASTVisitorException {
        Type expressionType = node.expression.accept(this);

        if (!isPrintableType(expressionType)) {
            throw new UnprintableTypeException(expressionType, node.expression);
        }

        // Nothing meaningful to return 
        return null;
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
        // This should never execute. 
        // Only Statement subclasses should be involved in the double dispatch.
        throw new UnsupportedOperationException("Error: Visitor invoked with abstract base class");
    }

	public Type visit(StringLiteral node) {
        return StringType.INSTANCE;
    }

	public Type visit(SubtractExpression node) throws ASTVisitorException {
        Type leftType = node.left.accept(this);
        Type rightType = node.right.accept(this);
        
        // Subtraction supported for integers, floats, and character types
        if (!isInt(leftType) && !isFloat(leftType) && !isChar(leftType)) {
            throw new InvalidOperandTypeException(leftType, "-", node.left);
        }
        else if (!leftType.equals(rightType)) {
            throw new TypeMismatchException(leftType, rightType, node.right);
        }

        return leftType;
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
        if (!isBoolean(conditionType)) {
            throw new TypeMismatchException(BooleanType.INSTANCE, conditionType, node.condition);
        }

        // Run semantic checks on the block
        node.block.accept(this);
        
        // Nothing meaningful to return
        return null;
    }
}
