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

    /**
     * Helper function which generates IR code for a binary operation.
     */
    private Temporary visitBinaryExpression(BinaryOperation.Operators operator, 
                                            BinaryOperationExpression node) 
                                            throws ASTVisitorException {
        Temporary left = node.left.accept(this);
        Temporary right = node.right.accept(this);
        assert left.type().equals(right.type());

        var operation = BinaryOperation.getOperation(operator, left, right);
        Temporary destination = tempPool.acquireTemp(operation.resultType());
        var assignInstruction = new TemporaryAssignmentInstruction(destination, operation);
        currentIRFunctionBuilder.addInstruction(assignInstruction);

        return destination;        
    }

    /**
     * Insert a NEWARRAY assignment-instruction to initialize the given 
     * array-typed temporary.
     */
    private void addNewArrayInstruction(Temporary arrayTemp) {
        assert TypeUtils.isArray(arrayTemp.type());

        ArrayType type = (ArrayType)arrayTemp.type();
        var newArrayExpression = new IRArrayCreation(type);

        currentIRFunctionBuilder.addInstruction(
            new TemporaryAssignmentInstruction(arrayTemp, newArrayExpression)
        );
    }

    /**
     * Ensures that the last instruction of the current void-returning
     * function is a RETURN instruction.
     */
    private void addTrailingReturn() {
        var instructions = currentIRFunctionBuilder.getInstructions();

        boolean hasInstructions = !instructions.isEmpty();
        boolean hasReturn = false;
        if (hasInstructions) {
            var lastInstruction = instructions.get(instructions.size() - 1);
            hasReturn = lastInstruction instanceof ReturnInstruction;
        }

        if (!hasReturn) {
            currentIRFunctionBuilder.addInstruction(new ReturnInstruction());
        }
    }

    public Temporary visit(AddExpression node) throws ASTVisitorException {
        return visitBinaryExpression(BinaryOperation.Operators.PLUS, node);
    }

	public Temporary visit(ArrayAssignmentStatement node) throws ASTVisitorException {
        Temporary arrayTemp = variableEnv.lookup(node.arrayId.value);
        Temporary index = node.indexExpression.accept(this);
        Temporary value = node.valueExpression.accept(this);

        var arrayAccess = new IRArrayAccess(arrayTemp, index);
        var assignmentInstruction = new ArrayAssignmentInstruction(arrayAccess, value);
        currentIRFunctionBuilder.addInstruction(assignmentInstruction);

        return null;
    }

	public Temporary visit(ArrayReference node) throws ASTVisitorException {
        // Find the temporary used for the array and the temporary
        // used to house the index expression and put them into an
        // assign-able array access object. 
        Temporary arrayTemp = variableEnv.lookup(node.id.value);
        Temporary index = node.indexExpression.accept(this);
        var arrayAccess = new IRArrayAccess(arrayTemp, index);

        // Find the type of array elements.
        Type arrayTempType = arrayTemp.type();
        assert TypeUtils.isArray(arrayTempType);
        SimpleType elementType = TypeUtils.getArrayElementType(arrayTempType);

        // Get a temporary to store the result and add an assignment
        // instruction to do so.
        Temporary destination = tempPool.acquireTemp(elementType);
        var assignmentInstruction = new TemporaryAssignmentInstruction(destination, arrayAccess);
        currentIRFunctionBuilder.addInstruction(assignmentInstruction);

        return destination;
    }

	public Temporary visit(ArrayTypeNode node) throws ASTVisitorException {
        throw new UnsupportedOperationException("Type node should not be visited during IR generation");
    }

	public Temporary visit(AssignmentStatement node) throws ASTVisitorException {
        String variableName = node.id.value;
        Expression expression = node.value; 

        // Evaluate the expression, likely but not necessarilty creating a
        // new temporary, and add an assignment instruction from that 
        // temporary to the one used for the variable in question.
        // This yields some duplicate assignment instructions that could 
        // be collapsed in some cases, but is very simple and such 
        // optimizations may be done elsewhere.
        Temporary expressionResult = expression.accept(this);
        Temporary destination = variableEnv.lookup(variableName);
        var instruction = new TemporaryAssignmentInstruction(destination, expressionResult);
        currentIRFunctionBuilder.addInstruction(instruction);

        return null; // Nothing meaningful to return here.
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
        for (var stmt : node.statements) {
            stmt.accept(this);
        }

        return null;
    }

	public Temporary visit(BooleanLiteral node) throws TemporaryOverflowException {
        // Get a temporary to hold the string. This will be returned.
        var temp = tempPool.acquireTemp(BooleanType.INSTANCE);

        // Generate a constant-assignment instruction and add it to the function
        var booleanConstant = IRConstant.forBoolean(node.value);
        var assignmentInstruction = new TemporaryAssignmentInstruction(temp, booleanConstant);
        currentIRFunctionBuilder.addInstruction(assignmentInstruction);

        return temp;
    }

	public Temporary visit(CharacterLiteral node) throws TemporaryOverflowException {
        // Get a temporary to hold the string. This will be returned.
        var temp = tempPool.acquireTemp(CharacterType.INSTANCE);

        // Generate a constant-assignment instruction and add it to the function
        var charConstant = IRConstant.forCharacter(node.value);
        var assignmentInstruction = new TemporaryAssignmentInstruction(temp, charConstant);
        currentIRFunctionBuilder.addInstruction(assignmentInstruction);

        return temp;
    }

	public Temporary visit(EqualityExpression node) throws ASTVisitorException {
        return visitBinaryExpression(BinaryOperation.Operators.EQUAL, node);        
    }

	public Temporary visit(Expression node) throws ASTVisitorException {
        // Should never be called for Expression parent class.
        throw new UnsupportedOperationException("Error: Visitor invoked with abstract base class");
    }

	public Temporary visit(ExpressionStatement node) throws ASTVisitorException {
        // Generate code for the expression in case it has side effects, 
        // but there is no need to store or return the result.
        node.expression.accept(this); 
        return null;
    }

	public Temporary visit(FloatLiteral node) throws TemporaryOverflowException {
        // Get a temporary to hold the string. This will be returned.
        var temp = tempPool.acquireTemp(FloatType.INSTANCE);

        // Generate a constant-assignment instruction and add it to the function
        var floatConstant = IRConstant.forFloat(node.value);
        var assignmentInstruction = new TemporaryAssignmentInstruction(temp, floatConstant);
        currentIRFunctionBuilder.addInstruction(assignmentInstruction);

        return temp;
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

        // If the function is of type void and no return was given as the last instruction, add a 
        // return instruction at the end.
        var returnType = currentIRFunctionBuilder.getReturnType(); 
        if (TypeUtils.isVoid(returnType)) {
            addTrailingReturn();
        }

        return null;
    }

	public Temporary visit(FunctionCall node) throws ASTVisitorException {
        
        // Visit all the arguments expressions and get a list of the temps 
        // that hold their results. Streaming can't be used here unfortunately
        // due to the possible checked exceptions from visiting the sub-nodes.
        List<Temporary> argTemps = new ArrayList<Temporary>();
        for (var argExpression : node.arguments) {
            Temporary arg = argExpression.accept(this);
            argTemps.add(arg); 
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
        // The form of the conversion of an if-else statement is shown 
        // in the following example:
        // Source Code:
        //      if (e) {
        //          print "if";
        //      }
        //      else {
        //          print "else";
        //      }
        // IR Pseudo-Code:
        //      T0 := Z! e;
        //      GOTO L0 IF T0; // If negation, go to else block
        //      PRINTU "if"; 
        //      GOTO L1;
        //  L0: ; // Else
        //      PRINTU "else";
        //  L1: ; // Done
        //
        // For simplicity's sake, we add two labels and GOTO's here even 
        // if there is no else block. 

        // Evaluate the condition and negate it
        Temporary condition = node.condition.accept(this);
        Temporary negatedCondition = tempPool.acquireTemp(BooleanType.INSTANCE);
        var negationExpression = NegationOperation.forBitwiseNegation(condition);
        currentIRFunctionBuilder.addInstruction(
            new TemporaryAssignmentInstruction(negatedCondition, negationExpression)
        );

        // Allocate labels
        Label elseLabel = labelFactory.getLabel();
        Label doneLabel = labelFactory.getLabel();

        // Add the else GOTO and follow it with the if-block's contents
        currentIRFunctionBuilder.addInstruction(
            new ConditionalJumpInstruction(negatedCondition, elseLabel)
        );

        node.ifBlock.accept(this);
        
        // At the end of the if-block, add a jump past the else block and
        // then add the else block, including its label
        currentIRFunctionBuilder.addInstructions(
            new JumpInstruction(doneLabel), 
            new LabelInstruction(elseLabel)
        );

        if (node.elseBlock.isPresent()) {
            node.elseBlock.get().accept(this);
        }

        // Add the final jump target used to skip the else block if the 
        // if-condition holds
        currentIRFunctionBuilder.addInstruction(
            new LabelInstruction(doneLabel)
        );
        
        return null;
    }

	public Temporary visit(IntLiteral node) throws TemporaryOverflowException {
        // Get a temporary to hold the string. This will be returned.
        var temp = tempPool.acquireTemp(IntegerType.INSTANCE);

        // Generate a constant-assignment instruction and add it to the function
        var intConstant = IRConstant.forInteger(node.value);
        var assignmentInstruction = new TemporaryAssignmentInstruction(temp, intConstant);
        currentIRFunctionBuilder.addInstruction(assignmentInstruction);

        return temp;
    }

	public Temporary visit(LessThanExpression node) throws ASTVisitorException {
        return visitBinaryExpression(BinaryOperation.Operators.LESS, node);        
    }

	public Temporary visit(MultiplyExpression node) throws ASTVisitorException {
        return visitBinaryExpression(BinaryOperation.Operators.MULTIPLY, node);        
    }

	public Temporary visit(ParenExpression node) throws ASTVisitorException {
        return node.expression.accept(this);
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
        throw new UnsupportedOperationException("Type node should not be visited during IR generation");
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
        return visitBinaryExpression(BinaryOperation.Operators.MINUS, node); 
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
        
        // Make sure any array variables are created
        if (TypeUtils.isArray(varType)) {
            addNewArrayInstruction(temp);
        }
        
        return temp;
    }

	public Temporary visit(WhileStatement node) throws ASTVisitorException {
        // The conversion of a while loop into IR code is
        // shown in the following example:
        //   Input:
        //      while (x) {
        //          /* body */
        //      }
        //   IR:
        //      L0:; // Loop start
        //          T0 := Z! x;
        //          GOTO L1 IF T0; // While condition failed.
        //          /* IR for loop body */
        //          GOTO L0; // Iterate
        //      L1:; // Loop over

        Label loopStartLabel = labelFactory.getLabel();
        Label loopEndLabel = labelFactory.getLabel();

        currentIRFunctionBuilder.addInstruction(
            new LabelInstruction(loopStartLabel)
        );

        // Evaluate the loop condition and negate it
        Temporary condition = node.condition.accept(this);
        var negationOperation = NegationOperation.forBitwiseNegation(condition);
        Temporary negatedCondition = tempPool.acquireTemp(BooleanType.INSTANCE);
        currentIRFunctionBuilder.addInstruction(
            new TemporaryAssignmentInstruction(negatedCondition, negationOperation)
        );

        // Exit the loop if the condition fails
        currentIRFunctionBuilder.addInstruction(
            new ConditionalJumpInstruction(negatedCondition, loopEndLabel)
        );

        // Add the loop body's statements and jump back to the start
        node.block.accept(this);
        currentIRFunctionBuilder.addInstruction(
            new JumpInstruction(loopStartLabel)
        );

        // Define the the end-of-loop jump target
        currentIRFunctionBuilder.addInstruction(
            new LabelInstruction(loopEndLabel)
        );

        return null;
    }
}