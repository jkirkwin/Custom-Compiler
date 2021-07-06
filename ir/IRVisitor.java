package ir;

import ast.*;
import common.Environment;
import type.*;

/**
 * A visitor which traverses the abstract syntax tree and generates
 * an intermediate representation of the source code.
 */
public class IRVisitor implements ASTVisitor<Temporary>  { 
    
    private final IRProgram.Builder programBuilder;
    private final TempPool tempPool;
    private final LabelFactory labelFactory;
    private final Environment<Identifier, Temporary> variableEnv;
    private IRFunction.Builder currentIRFunctionBuilder;
    private IRMethodType.Builder currentIRFunctionTypeBuilder;

    // TODO Do we need a function environment?

    /**
     * Returns a textual representation of the program which has been built.
     * Must be called after visiting an AST.
     */
    public String buildProgram() {
        return programBuilder.build().toString(); // TODO Instead, use a filewriter and all the associated stuff to create a .ir file.
    }

    public IRVisitor() {
        programBuilder = new IRProgram.Builder();
        tempPool = new TempFactory();
        labelFactory = new LabelFactory();
        variableEnv = new Environment<Identifier, Temporary>();
    }

    public Temporary visit(AddExpression node) throws ASTVisitorException {
        throw new UnsupportedOperationException("Unimplemented"); // TODO Implement
    }

	public Temporary visit(ArrayAssignmentStatement node) throws ASTVisitorException {
        throw new UnsupportedOperationException("Unimplemented"); // TODO Implement
    }

	public Temporary visit(ArrayReference node) throws ASTVisitorException {
        throw new UnsupportedOperationException("Unimplemented"); // TODO Implement
    }

	public Temporary visit(ArrayTypeNode node) throws ASTVisitorException {
        throw new UnsupportedOperationException("Unimplemented"); // TODO Implement
    }

	public Temporary visit(AssignmentStatement node) throws ASTVisitorException {
        throw new UnsupportedOperationException("Unimplemented"); // TODO Implement
    }

	public Temporary visit(ASTNode node) throws ASTVisitorException {
        // Should never be called for ASTNode parent class.
        throw new UnsupportedOperationException("Error: Visitor invoked with abstract base class");
    }

	public Temporary visit(BinaryOperationExpression node) throws ASTVisitorException {
        // Should never be called for BinaryOperationExpression parent class.
        throw new UnsupportedOperationException("Error: Visitor invoked with abstract base class");
    }

	public Temporary visit(Block node) throws ASTVisitorException {
        throw new UnsupportedOperationException("Unimplemented"); // TODO Implement
    }

	public Temporary visit(BooleanLiteral node) throws ASTVisitorException {
        throw new UnsupportedOperationException("Unimplemented"); // TODO Implement
    }

	public Temporary visit(CharacterLiteral node) throws ASTVisitorException {
        throw new UnsupportedOperationException("Unimplemented"); // TODO Implement
    }

	public Temporary visit(EqualityExpression node) throws ASTVisitorException {
        throw new UnsupportedOperationException("Unimplemented"); // TODO Implement
    }

	public Temporary visit(Expression node) throws ASTVisitorException {
        // Should never be called for Expression parent class.
        throw new UnsupportedOperationException("Error: Visitor invoked with abstract base class");
    }

	public Temporary visit(ExpressionStatement node) throws ASTVisitorException {
        throw new UnsupportedOperationException("Unimplemented"); // TODO Implement
    }

	public Temporary visit(FloatLiteral node) throws ASTVisitorException {
        throw new UnsupportedOperationException("Unimplemented"); // TODO Implement
    }

	public Temporary visit(FormalParameter node) throws TemporaryOverflowException {
        Type paramType = node.typeNode.type;
        Identifier id = node.identifier;
        String paramName = node.identifier.value;

        // Add the param type to the method type and get a temporary for it
        currentIRFunctionTypeBuilder.addArgumentType(paramType);
        var temp = tempPool.acquireParam(paramType, paramName);
        variableEnv.bind(id, temp);

        return temp;
    }

	public Temporary visit(FunctionBody node) throws ASTVisitorException {
        return null; // TODO Implement. Exception omitted so we can actually run the code.
    }

	public Temporary visit(FunctionCall node) throws ASTVisitorException {
        throw new UnsupportedOperationException("Unimplemented"); // TODO Implement
    }

	public Temporary visit(FunctionDecl node) throws ASTVisitorException {
        String name = node.identifier.value;
        Type returnType = node.typeNode.type;
        
        currentIRFunctionTypeBuilder = new IRMethodType.Builder();
        currentIRFunctionTypeBuilder.withReturnType(returnType);

        // Visit the formals and build up the return type
        for (var formal : node.formals) {
            formal.accept(this);
        }
        
        currentIRFunctionBuilder
            .withName(name)
            .withMethodType(currentIRFunctionTypeBuilder.build());

        return null;
    }

	public Temporary visit(Function node) throws ASTVisitorException {
        // Reset the per-function state
        tempPool.clear();
        currentIRFunctionBuilder = new IRFunction.Builder();

        // Visit the function components and add the resulting function to 
        // the program
        variableEnv.enterScope();
        node.declaration.accept(this);
        node.body.accept(this);
        variableEnv.exitScope();

        programBuilder.addFunction(currentIRFunctionBuilder.build());

        // Nothing meaningful to return
        return null;
    }

	public Temporary visit(Identifier node) throws ASTVisitorException {
        throw new UnsupportedOperationException("Unimplemented"); // TODO Implement
    }

	public Temporary visit(IfStatement node) throws ASTVisitorException {
        throw new UnsupportedOperationException("Unimplemented"); // TODO Implement
    }

	public Temporary visit(IntLiteral node) throws ASTVisitorException {
        throw new UnsupportedOperationException("Unimplemented"); // TODO Implement
    }

	public Temporary visit(LessThanExpression node) throws ASTVisitorException {
        throw new UnsupportedOperationException("Unimplemented"); // TODO Implement
    }

	public Temporary visit(MultiplyExpression node) throws ASTVisitorException {
        throw new UnsupportedOperationException("Unimplemented"); // TODO Implement
    }

	public Temporary visit(ParenExpression node) throws ASTVisitorException {
        throw new UnsupportedOperationException("Unimplemented"); // TODO Implement
    }

	public Temporary visit(PrintlnStatement node) throws ASTVisitorException {
        throw new UnsupportedOperationException("Unimplemented"); // TODO Implement
    }

	public Temporary visit(PrintStatement node) throws ASTVisitorException {
        throw new UnsupportedOperationException("Unimplemented"); // TODO Implement
    }

	public Temporary visit(Program node) throws ASTVisitorException {
        programBuilder.withName("Foo"); // TODO Set program name

        for (var func : node.functions) {
            func.accept(this);
        }

        return null;
    }

	public Temporary visit(ReturnStatement node) throws ASTVisitorException {
        throw new UnsupportedOperationException("Unimplemented"); // TODO Implement
    }

	public Temporary visit(SimpleTypeNode node) throws ASTVisitorException {
        throw new UnsupportedOperationException("Unimplemented"); // TODO Implement
    }

	public Temporary visit(Statement node) throws ASTVisitorException {
        // Should never be called for Statement parent class.
        throw new UnsupportedOperationException("Error: Visitor invoked with abstract base class");
    }

	public Temporary visit(StringLiteral node) throws ASTVisitorException {
        throw new UnsupportedOperationException("Unimplemented"); // TODO Implement
    }

	public Temporary visit(SubtractExpression node) throws ASTVisitorException {
        throw new UnsupportedOperationException("Unimplemented"); // TODO Implement
    }

	public Temporary visit(TypeNode node) throws ASTVisitorException {
        // Should never be called for TypeNode parent class.
        throw new UnsupportedOperationException("Error: Visitor invoked with abstract base class");
    }

	public Temporary visit(VariableDeclaration node) throws ASTVisitorException {
        throw new UnsupportedOperationException("Unimplemented"); // TODO Implement
    }

	public Temporary visit(WhileStatement node) throws ASTVisitorException {
        throw new UnsupportedOperationException("Unimplemented"); // TODO Implement
    }
}