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

	public Void visit(AddExpression node) throws ASTVisitorException {
		node.left.accept(this);
		System.out.print("+");
		node.right.accept(this);

        return null;
	}

	public Void visit(ArrayAssignmentStatement node) throws ASTVisitorException {
		node.arrayId.accept(this);
		System.out.print("[");
		node.indexExpression.accept(this);
		System.out.print("] = ");
		node.valueExpression.accept(this);
		System.out.println(";");

		return null;
	}

	public Void visit(ArrayReference node) throws ASTVisitorException {
		node.id.accept(this);
		System.out.print("[");
		node.indexExpression.accept(this);
	    System.out.print("]");

		return null;
	}

	public Void visit(ArrayTypeNode node) throws ASTVisitorException {
	    System.out.print(node.type.toString());
		return null;
	}

	public Void visit(AssignmentStatement node) throws ASTVisitorException {
	    node.id.accept(this);
	    System.out.print(" = ");
		node.value.accept(this);
		System.out.println(";");
		return null;
	}

    public Void visit(ASTNode node) throws ASTVisitorException {
	    throw new UnsupportedOperationException("Visit called with ASTNodeType type");
	}

	public Void visit(BinaryOperationExpression node) throws ASTVisitorException {
		throw new UnsupportedOperationException("Visit called with BinaryOperationExpression type");
	}
	
	public Void visit(Block node) throws ASTVisitorException {
		printlnWithIndent("{");
		++indentLevel;

		for (Statement stmt : node.statements) {
			printIndent();
			stmt.accept(this);
		}

		--indentLevel;
		printlnWithIndent("}");

		return null;
	}

	public Void visit(BooleanLiteral node) throws ASTVisitorException { 
		System.out.print(node.value);
		return null;
	}

	public Void visit(CharacterLiteral node) throws ASTVisitorException {
		System.out.print("'" + node.value + "'");
		return null;
	}

	public Void visit(EqualityExpression node) throws ASTVisitorException { 
		node.left.accept(this);
		System.out.print("==");
		node.right.accept(this);

		return null;
	}

	public Void visit(ExpressionStatement node) throws ASTVisitorException {
		node.expression.accept(this);
		System.out.println(";");
		return null;
	}

	public Void visit(Expression node) throws ASTVisitorException {
		throw new UnsupportedOperationException("Visit called with Expression type");
	}

	public Void visit(FloatLiteral node) throws ASTVisitorException {
	    System.out.print(node.value);
		return null;
	}

	public Void visit(FormalParameter node) throws ASTVisitorException { 
		node.typeNode.accept(this);
		System.out.print(" ");
		node.identifier.accept(this);
		return null;
	}

	public Void visit(FunctionBody node) throws ASTVisitorException {
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

	public Void visit(FunctionCall node) throws ASTVisitorException { 
		node.id.accept(this);

		System.out.print("(");
		var args = node.arguments;
		for (int i = 0; i < args.size(); ++i) {
			var arg = args.get(i);
			arg.accept(this);

			if (i < args.size() - 1) {
				System.out.print(", ");
			}
		}
		System.out.print(")");
		
		return null;
	}

	public Void visit(FunctionDecl node) throws ASTVisitorException {
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

	public Void visit(Function node) throws ASTVisitorException {
        node.declaration.accept(this);
        node.body.accept(this);
		return null;
	}

	public Void visit(Identifier node) throws ASTVisitorException {
		System.out.print(node.value);
		return null;
	}

	public Void visit(IfStatement node) throws ASTVisitorException { 
		System.out.print("if (");
		node.condition.accept(this);
		System.out.println(")");

		node.ifBlock.accept(this);

		if (node.elseBlock.isPresent()) {
			printlnWithIndent("else");
			node.elseBlock.get().accept(this);
		}

		return null;
	}

	public Void visit(IntLiteral node) throws ASTVisitorException {
		System.out.print(node.value);
		return null;
	}

	public Void visit(LessThanExpression node) throws ASTVisitorException { 
		node.left.accept(this);
		System.out.print("<");
		node.right.accept(this);

		return null;
	}

	public Void visit(MultiplyExpression node) throws ASTVisitorException {
		node.left.accept(this);
		System.out.print("*");
		node.right.accept(this);

		return null;
	}

	public Void visit(ParenExpression node) throws ASTVisitorException {
		System.out.print("(");
		node.expression.accept(this);
		System.out.print(")");
		
		return null;
	}

	public Void visit(PrintlnStatement node) throws ASTVisitorException { 
		System.out.print("println ");
		node.expression.accept(this);
		System.out.println(";");

		return null;
	}

	public Void visit(PrintStatement node) throws ASTVisitorException { 
		System.out.print("print ");
		node.expression.accept(this);
		System.out.println(";");

		return null;
	}

	public Void visit(Program node) throws ASTVisitorException {
		for(var func : node.functions) {
            func.accept(this);
        }
        return null;
	}

	public Void visit(ReturnStatement node) throws ASTVisitorException {
		System.out.print("return");

		if (node.returnExpression.isPresent()) {
			System.out.print(" ");
			node.returnExpression.get().accept(this);
		}

		System.out.println(";");

		return null;
	}

	public Void visit(SimpleTypeNode node) throws ASTVisitorException {
		System.out.print(node.type.toString());
		return null;
	}

	public Void visit(Statement node) throws ASTVisitorException {
		throw new UnsupportedOperationException("Visit called with Statement type");
	}

	public Void visit(StringLiteral node) throws ASTVisitorException {
		System.out.print('"' + node.value + '"');
		return null;
	}

	public Void visit(SubtractExpression node) throws ASTVisitorException {
		node.left.accept(this);
		System.out.print("-");
		node.right.accept(this);

		return null;
	}

	public Void visit(TypeNode node) throws ASTVisitorException {
		throw new UnsupportedOperationException("Visit called with TypeNode type");
	}

	public Void visit(VariableDeclaration node) throws ASTVisitorException {
		node.typeNode.accept(this);
		System.out.print(" ");
		node.id.accept(this);
		System.out.println(";");

		return null;
	}

	public Void visit(WhileStatement node) throws ASTVisitorException {
		System.out.print("while (");
		node.condition.accept(this);
		System.out.println(")");

		node.block.accept(this);

		return null;
	}
}
