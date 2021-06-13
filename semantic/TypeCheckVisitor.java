package semantic;

import ast.*;
import type.*;

/**
 * A visitor which traverses the abstract syntax tree and verifies
 * semantic predicates and type-system conformance.
 */
public class TypeCheckVisitor implements ASTVisitor<Type>  {

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

	public Type visit(Program node) throws SemanticException {
        return null; // TODO
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
