package ast;

/**
 * Represents a visitor which traverses the nodes of the
 * abstract syntax tree. T is the type that is returned.
 */
 public interface ASTVisitor<T> {

	public T visit(AddExpression node);

	public T visit(ArrayAssignmentStatement node);

	public T visit(ArrayReference node);

	public T visit(ArrayTypeNode node);

	public T visit(AssignmentStatement node);

	public T visit(ASTNode node); // Probably should never be called

	public T visit(BinaryOperationExpression node);

	public T visit(Block node);

	public T visit(BooleanLiteral node);

	public T visit(CharacterLiteral node);

	public T visit(EqualityExpression node);

	public T visit(Expression node);

	public T visit(ExpressionStatement node);

	public T visit(FloatLiteral node);

	public T visit(FormalParameter node);

	public T visit(FunctionBody node);

	public T visit(FunctionCall node);

	public T visit(FunctionDecl node);

	public T visit(Function node);

	public T visit(Identifier node);

	public T visit(IfStatement node);

	public T visit(IntLiteral node);

	public T visit(LessThanExpression node);

	public T visit(MultiplyExpression node);

	public T visit(ParenExpression node);

	public T visit(PrintlnStatement node);

	public T visit(PrintStatement node);

	public T visit(Program node);

	public T visit(ReturnStatement node);

	public T visit(SimpleTypeNode node);

	public T visit(Statement node);

	public T visit(StringLiteral node);

	public T visit(SubtractExpression node);

	public T visit(TypeNode node);

	public T visit(VariableDeclaration node);

	public T visit(WhileStatement node);
 }
