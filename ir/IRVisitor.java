package ir;

import ast.*;
import type.*;

/**
 * A visitor which traverses the abstract syntax tree and generates
 * an intermediate representation of the source code.
 */
public class IRVisitor implements ASTVisitor<Temporary>  { 
    
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

	public Temporary visit(FormalParameter node) throws ASTVisitorException {
        throw new UnsupportedOperationException("Unimplemented"); // TODO Implement
    }

	public Temporary visit(FunctionBody node) throws ASTVisitorException {
        throw new UnsupportedOperationException("Unimplemented"); // TODO Implement
    }

	public Temporary visit(FunctionCall node) throws ASTVisitorException {
        throw new UnsupportedOperationException("Unimplemented"); // TODO Implement
    }

	public Temporary visit(FunctionDecl node) throws ASTVisitorException {
        throw new UnsupportedOperationException("Unimplemented"); // TODO Implement
    }

	public Temporary visit(Function node) throws ASTVisitorException {
        throw new UnsupportedOperationException("Unimplemented"); // TODO Implement
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
        throw new UnsupportedOperationException("Unimplemented"); // TODO Implement
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