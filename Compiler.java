import org.antlr.runtime.*;
import java.io.*;
import ast.*;

/*
 * Uses the ANTLR tool to perform lexical analysis and generate
 * a parser based on the grammar for the unnamed language.
 */
public class Compiler {
    
    public static String stmtToString(Statement s) {
        // Disgusting, but works for debugging until we have a visitor pattern
        if (s instanceof ReturnStatement) {
            return returnStmtToString((ReturnStatement)s);
        }
        else if (s instanceof PrintStatement) {
            PrintStatement p = (PrintStatement)s;
            return "print " + p.expression.getClass();
        }
        else if (s instanceof PrintlnStatement) {
            PrintlnStatement p = (PrintlnStatement)s;
            return "print " + p.expression.getClass();
        }
        else if (s instanceof AssignmentStatement) {
            AssignmentStatement a = (AssignmentStatement)s;
            return a.id.value + " = " + a.value.getClass();
        }
        else if (s instanceof ArrayAssignmentStatement) {
            ArrayAssignmentStatement a = (ArrayAssignmentStatement)s;
            return a.arrayId.value + "[" + a.indexExpression.getClass() + "] = " + a.valueExpression.getClass();
        }
        else if (s instanceof WhileStatement) {
            WhileStatement w = (WhileStatement)s;
            return "while (" + w.condition.getClass() + ") { " + w.block.getClass() + " }";
        }
        else if (s instanceof IfStatement) {
            IfStatement i = (IfStatement)s;
            String str = "if (" + i.condition.getClass() + ") { " + i.ifBlock.getClass() + " }";
            if (i.elseBlock.isPresent()) {
                str += " else { " + i.elseBlock.get().getClass()  + " } ";
            }
            return str;
        }
        else {
            return s.getClass().toString();    
        }
    }

    public static String returnStmtToString(ReturnStatement s) {
        if (s.returnExpression.isPresent()) {
            return "return " + s.returnExpression.get().getClass().toString() + ";";
        }
        else {
            return "return;";
        }
    }

    // TODO Remove once we add the printing visitor
    /**
     * Simple debugging utility to print the contents of a function.
     */
    public static void printFunction(Function f) {
        var decl = f.declaration;
        String type = decl.typeNode.type.getClass().toString();
        String id = decl.identifier.value;
        
        StringBuilder builder = new StringBuilder();
        builder.append(type)
            .append(" ")
            .append(id)
            .append("(");

        for (int i = 0; i < decl.formals.size(); ++i) {
            var formal = decl.formals.get(i);
            var formalType = formal.typeNode.type;
            var formalName = formal.identifier.value;

            builder.append(formalType.getClass()).append(" ").append(formalName);
            
            if (i != decl.formals.size() - 1) {
                builder.append(", ");
            }
        }       
        builder.append(") {\n");
        
        builder.append("\t// Declarations:\n");
        var varDecls = f.body.declarations;
        for (VariableDeclaration v : varDecls) {
            var varType = v.typeNode.type.getClass();
            var varName = v.id.value;
            
            builder.append("\t")
                .append(varType)
                .append(" ")
                .append(varName)
                .append(";\n");
        }

        builder.append("\t// Statements:\n");
        var stmts = f.body.statements;
        for (Statement s : stmts) {
            builder.append("\t");
            if (s == null) {
                builder.append("== null! ==");
            }
            else {
                builder.append(stmtToString(s));
            }
            builder.append("\n");
        }
        

        builder.append("}\n");
        System.out.println(builder.toString());
    }

	public static void main (String[] args) throws IOException {
		
		if (args.length == 0 ) {
			System.out.println("Usage: Compiler filename.ul");
			return;
		}

		String fileName = args[0];

		ANTLRInputStream input;
		try {
			input = new ANTLRInputStream(new FileInputStream(fileName));	
		}
		catch (FileNotFoundException e) {
			System.out.println(String.format("Could not open file %s", fileName));
			return;
		}

		// The name of the grammar here is "UnnamedLanguage", so ANTLR 
		// generates UnnamedLanguageLexer and UnnamedLanguageParser
		UnnamedLanguageLexer lexer = new UnnamedLanguageLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		UnnamedLanguageParser parser = new UnnamedLanguageParser(tokens);

		try {
			// TODO why is this not throwing an exception when parsing identifiers with special characters?
			Program program = parser.program();
            
            PrettyPrintVisitor printVisitor = new PrettyPrintVisitor();
            printVisitor.visit(program);

            //for (Function f : program.functions) {
            //    printFunction(f);
            //}
        }
        catch (RecognitionException e )	{
    		// A lexical or parsing error occured.
	    	// ANTLR will have already printed information about the error
		    // to System.err.

		    // Allows error code checking to automate testing.
		    System.exit(1); 
		}
		catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();

			// Allow error code checking to automate testing.
			System.exit(1);
		}
	}
}
