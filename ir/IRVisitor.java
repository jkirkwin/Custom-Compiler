package ir;

import ast.*;
import common.Environment;
import java.util.ArrayList;
import java.util.List;
import type.*;

/**
 * A visitor which traverses the abstract syntax tree and generates
 * an intermediate representation of the source code.
 */
public class IRVisitor implements ASTVisitor<Temporary>  { 
    
    private final IRProgram.Builder programBuilder;
    private final TempPool tempPool;
    private final LabelFactory labelFactory;
    private final Environment<String, Temporary> variableEnv;
    private final Environment<String, Type> functionEnv;
    private IRFunction.Builder currentIRFunctionBuilder;
    private IRMethodType.Builder currentIRFunctionTypeBuilder;

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
        variableEnv = new Environment<String, Temporary>();
        functionEnv = new Environment<String, Type>();
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
        // TODO If we're assigning a constant, need to be careful to avoid infinite recursion
        // Just visit the node's expression to get a temp and then check if the right hand side is an IRConstant of the appropriate type. 
        // If so, then just return that temp. 
        // Otherwise, make your own assignment instruction.
        
        // Alternatively: Don't be weird and just allow the duplicated temporary? Pretty sure we can make this work without any crazy infinite recursion.

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
        String paramName = node.identifier.value;

        // Add the param type to the method type and get a temporary for it
        currentIRFunctionTypeBuilder.addArgumentType(paramType);
        var temp = tempPool.acquireParam(paramType, paramName);
        variableEnv.bind(paramName, temp);

        return temp;
    }

	public Temporary visit(FunctionBody node) throws ASTVisitorException {
        for (var varDecl : node.declarations) {
            varDecl.accept(this);
        }

        for (var stmt : node.statements) {
            stmt.accept(this);
        }
        // TODO Do we need to add a return statement to the end of non-void functions? What if there's already one (or multiple) there?

        return null;
    }

	public Temporary visit(FunctionCall node) throws ASTVisitorException {
        
        // Visit all the arguments expressions and get a list of the temps 
        // that hold their results. Streaming can't be used here unfortunately
        // due to the possible checked exceptions from visiting the sub-nodes.
        List<Temporary> argTemps = new ArrayList<Temporary>();
        for (var argExpression : node.arguments) {
            argTemps.add(argExpression.accept(this));
        }

        var funcName = node.id.value;
        var returnType = functionEnv.lookup(funcName);
        var irFunctionCall = new IRFunctionCall(funcName, argTemps);
        
        if (TypeUtils.isVoid(returnType)) {
            // Generate a simple CALL instruction
            var instruction = new FunctionCallInstruction(irFunctionCall);
            currentIRFunctionBuilder.addInstruction(instruction);
            return null;
        }
        else {
            // Generate a CALL-assignment instruction and return the result
            Temporary temp = tempPool.acquireTemp(returnType);
            var instruction = new TemporaryAssignmentInstruction(temp, irFunctionCall);
            currentIRFunctionBuilder.addInstruction(instruction);
            return temp;
        }
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

        // Visit the function components and build up the function
        variableEnv.enterScope();
        node.declaration.accept(this);
        node.body.accept(this);
        variableEnv.exitScope();

        // Add all the temporaries that were allocated to the function 
        // builder, build the function instance, and add it to the program.
        currentIRFunctionBuilder.withTemps(tempPool.getAllTemps());
        programBuilder.addFunction(currentIRFunctionBuilder.build());

        // Nothing meaningful to return
        return null;
    }

	public Temporary visit(Identifier node) {
        // Just return the temporary that holds the variable.
        return variableEnv.lookup(node.value);
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
        Temporary expressionResult = node.expression.accept(this);
        var instruction = new PrintlnInstruction(expressionResult);
        currentIRFunctionBuilder.addInstruction(instruction);

        return null; // Nothing meaningful to return
    }

	public Temporary visit(PrintStatement node) throws ASTVisitorException {
        Temporary expressionResult = node.expression.accept(this);
        var instruction = new PrintInstruction(expressionResult);
        currentIRFunctionBuilder.addInstruction(instruction);

        return null; // Nothing meaningful to return
    }

	public Temporary visit(Program node) throws ASTVisitorException {
        programBuilder.withName("Foo"); // TODO Set program name
            
        // Add all functions to the environment first, so function-calls 
        // can be generated appropriately
        for (var func : node.functions) {
            String funcName = func.declaration.identifier.value;
            Type returnType = func.declaration.typeNode.type;
            functionEnv.bind(funcName, returnType);
        }

        // Visit each function and generate IR code for it.
        for (var func : node.functions) {
            func.accept(this);
        }

        return null;
    }

	public Temporary visit(ReturnStatement node) throws ASTVisitorException {
        ReturnInstruction returnInstruction; 
        if (node.returnExpression.isPresent()) {
            var temp = node.returnExpression.get().accept(this);
            returnInstruction = new ReturnInstruction(temp);
        }
        else {
            returnInstruction = new ReturnInstruction();
        }

        currentIRFunctionBuilder.addInstruction(returnInstruction);

        return null; // Nothing meaningful to return
    }

	public Temporary visit(SimpleTypeNode node) throws ASTVisitorException {
        throw new UnsupportedOperationException("Unimplemented"); // TODO Implement
    }

	public Temporary visit(Statement node) throws ASTVisitorException {
        // Should never be called for Statement parent class.
        throw new UnsupportedOperationException("Error: Visitor invoked with abstract base class");
    }

	public Temporary visit(StringLiteral node) throws TemporaryOverflowException {
        // Get a temporary to hold the string. This will be returned.
        var temp = tempPool.acquireTemp(StringType.INSTANCE);

        // Generate a constant-assignment instruction and add it to the function
        var stringConstant = IRConstant.forString(node.value);
        var assignmentInstruction = new TemporaryAssignmentInstruction(temp, stringConstant);
        currentIRFunctionBuilder.addInstruction(assignmentInstruction);

        return temp;
    }

	public Temporary visit(SubtractExpression node) throws ASTVisitorException {
        throw new UnsupportedOperationException("Unimplemented"); // TODO Implement
    }

	public Temporary visit(TypeNode node) throws ASTVisitorException {
        // Should never be called for TypeNode parent class.
        throw new UnsupportedOperationException("Error: Visitor invoked with abstract base class");
    }

	public Temporary visit(VariableDeclaration node) throws TemporaryOverflowException {
        Type varType = node.typeNode.type;
        String varName = node.id.value;

        var temp = tempPool.acquireLocal(varType, varName);
        variableEnv.bind(varName, temp);
        return temp;
    }

	public Temporary visit(WhileStatement node) throws ASTVisitorException {
        throw new UnsupportedOperationException("Unimplemented"); // TODO Implement
    }
}