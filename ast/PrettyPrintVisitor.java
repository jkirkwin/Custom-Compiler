package ast;

public class PrettyPrintVisitor implements ASTVisitor<Void> {

    private static final String INDENT = "    ";

    private int indentLevel;

    public PrettyPrintVisitor() {
        indentLevel = 0;
    }

    private void printIndent() {
        for (int i = 0; i < indentLevel; ++i) {
            System.out.print(INDENT);
        }
    }

    private void printWithIndent(String s) {
        printIndent();
		System.out.print(s);
    }

    private void printlnWithIndent(String s) {
        printIndent();
		System.out.println(s);
    }

	public Void visit(AddExpression node) {
	    System.out.println("Add"); // TODO
        return null;
	}

	public Void visit(ArrayAssignmentStatement node) {
		node.arrayId.accept(this);
		System.out.print("[");
		node.indexExpression.accept(this);
		System.out.print("] = ");
		node.valueExpression.accept(this);
		System.out.println(";");

		return null;
	}

	public Void visit(ArrayReference node) {
	    System.out.println("Array reference"); // TODO
		return null;
	}

	public Void visit(ArrayTypeNode node) {
	    System.out.print(node.type.toShortString());
		return null;
	}

	public Void visit(AssignmentStatement node) {
	    node.id.accept(this);
	    System.out.print(" = ");
		node.value.accept(this);
		System.out.println(";");
		return null;
	}

    public Void visit(ASTNode node) {
	    throw new UnsupportedOperationException("Visit called with ASTNodeType type");
	}

	public Void visit(BinaryOperationExpression node) {
		throw new UnsupportedOperationException("Visit called with BinaryOperationExpression type");
	}
	
	public Void visit(Block node) { // TODO
		return null;
	}

	public Void visit(BooleanLiteral node) { 
		System.out.print(node.value);
		return null;
	}

	public Void visit(CharacterLiteral node) {
		System.out.print("'" + node.value + "'");
		return null;
	}

	public Void visit(EqualityExpression node) { // TODO
		return null;
	}

	public Void visit(ExpressionStatement node) { // TODO
		return null;
	}

	public Void visit(Expression node) {
		throw new UnsupportedOperationException("Visit called with Expression type");
	}

	public Void visit(FloatLiteral node) {
	    System.out.print(node.value);
		return null;
	}

	public Void visit(FormalParameter node) { // TODO
		node.typeNode.accept(this);
		System.out.print(" ");
		node.identifier.accept(this);
		return null;
	}

	public Void visit(FunctionBody node) {
		System.out.println("{");
        ++indentLevel;
       
        for(var varDecl : node.declarations) {
			printIndent();
            varDecl.accept(this);
        }

		if (!node.declarations.isEmpty() && !node.statements.isEmpty()) {
			System.out.println();
		}

        for(var stmt : node.statements) {
			printIndent();
            stmt.accept(this);
        }

        --indentLevel;
        System.out.println("}");
        System.out.println("");

        return null;
	}

	public Void visit(FunctionCall node) { // TODO
		return null;
	}

	public Void visit(FunctionDecl node) {
		node.typeNode.accept(this);
        System.out.print(" ");
        node.identifier.accept(this);

        System.out.print(" (");
		var formals = node.formals;
		for (int i = 0; i < formals.size(); ++i) {
			FormalParameter formal = formals.get(i);
			formal.accept(this);
			
			if (i != formals.size() - 1) {
				System.out.print(", ");
			}
		}
        System.out.println(")");

        return null;
	}

	public Void visit(Function node) {
        node.declaration.accept(this);
        node.body.accept(this);
		return null;
	}

	public Void visit(Identifier node) {
		System.out.print(node.value);
		return null;
	}

	public Void visit(IfStatement node) { // TODO
		return null;
	}

	public Void visit(IntLiteral node) {
		System.out.print(node.value);
		return null;
	}

	public Void visit(LessThanExpression node) { // TODO
		return null;
	}

	public Void visit(MultiplyExpression node) { // TODO
		return null;
	}

	public Void visit(ParenExpression node) { // TODO
		return null;
	}

	public Void visit(PrintlnStatement node) { // TODO
		return null;
	}

	public Void visit(PrintStatement node) { // TODO
		return null;
	}

	public Void visit(Program node) {
		for(var func : node.functions) {
            func.accept(this);
        }
        return null;
	}

	public Void visit(ReturnStatement node) { // TODO
		return null;
	}

	public Void visit(SimpleTypeNode node) {
		System.out.print(node.type.toShortString());
		return null;
	}

	public Void visit(Statement node) {
		throw new UnsupportedOperationException("Visit called with Statement type");
	}

	public Void visit(StringLiteral node) {
		System.out.print('"' + node.value + '"');
		return null;
	}

	public Void visit(SubtractExpression node) { // TODO
		return null;
	}

	public Void visit(TypeNode node) {
		throw new UnsupportedOperationException("Visit called with TypeNode type");
	}

	public Void visit(VariableDeclaration node) {
		node.typeNode.accept(this);
		System.out.print(" ");
		node.id.accept(this);
		System.out.println(";");

		return null;
	}

	public Void visit(WhileStatement node) { // TODO
		return null;
	}
}
