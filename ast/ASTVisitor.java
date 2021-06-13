package ast;

/**
 * Represents a visitor which traverses the nodes of the
 * abstract syntax tree. T is the type that is returned.
 */
 public interface ASTVisitor<T> {

	public T visit(AddExpression node) throws ASTVisitorException;

	public T visit(ArrayAssignmentStatement node) throws ASTVisitorException;

	public T visit(ArrayReference node) throws ASTVisitorException;

	public T visit(ArrayTypeNode node) throws ASTVisitorException;

	public T visit(AssignmentStatement node) throws ASTVisitorException;

	public T visit(ASTNode node) throws ASTVisitorException; // Probably should never be called

	public T visit(BinaryOperationExpression node) throws ASTVisitorException;

	public T visit(Block node) throws ASTVisitorException;

	public T visit(BooleanLiteral node) throws ASTVisitorException;

	public T visit(CharacterLiteral node) throws ASTVisitorException;

	public T visit(EqualityExpression node) throws ASTVisitorException;

	public T visit(Expression node) throws ASTVisitorException;

	public T visit(ExpressionStatement node) throws ASTVisitorException;

	public T visit(FloatLiteral node) throws ASTVisitorException;

	public T visit(FormalParameter node) throws ASTVisitorException;

	public T visit(FunctionBody node) throws ASTVisitorException;

	public T visit(FunctionCall node) throws ASTVisitorException;

	public T visit(FunctionDecl node) throws ASTVisitorException;

	public T visit(Function node) throws ASTVisitorException;

	public T visit(Identifier node) throws ASTVisitorException;

	public T visit(IfStatement node) throws ASTVisitorException;

	public T visit(IntLiteral node) throws ASTVisitorException;

	public T visit(LessThanExpression node) throws ASTVisitorException;

	public T visit(MultiplyExpression node) throws ASTVisitorException;

	public T visit(ParenExpression node) throws ASTVisitorException;

	public T visit(PrintlnStatement node) throws ASTVisitorException;

	public T visit(PrintStatement node) throws ASTVisitorException;

	public T visit(Program node) throws ASTVisitorException;

	public T visit(ReturnStatement node) throws ASTVisitorException;

	public T visit(SimpleTypeNode node) throws ASTVisitorException;

	public T visit(Statement node) throws ASTVisitorException;

	public T visit(StringLiteral node) throws ASTVisitorException;

	public T visit(SubtractExpression node) throws ASTVisitorException;

	public T visit(TypeNode node) throws ASTVisitorException;

	public T visit(VariableDeclaration node) throws ASTVisitorException;

	public T visit(WhileStatement node) throws ASTVisitorException;
 }
