import org.antlr.runtime.*;
import java.io.*;
import ast.*;

/*
 * Uses the ANTLR tool to perform lexical analysis and generate
 * a parser based on the grammar for the unnamed language.
 */
public class Compiler {

    // TODO Remove once we add the printing visitor
    /**
     * Simple debugging utility to print the contents of a function.
     */
    public static void printFunction(Function f) {
        var decl = f.declaration;
        String type = decl.typeNode.typeString;
        String id = decl.identifier.value;
        
        StringBuilder builder = new StringBuilder();
        builder.append(type)
            .append(" ")
            .append(id)
            .append("(");

        for (int i = 0; i < decl.formals.size(); ++i) {
            var formal = decl.formals.get(i);
            var formalType = formal.typeNode.typeString;
            var formalName = formal.identifier.value;

            builder.append(formalType).append(" ").append(formalName);
            
            if (i != decl.formals.size() - 1) {
                builder.append(", ");
            }
        }       
        builder.append(") {\n");
        
        builder.append("\t// Declarations:\n");
        var varDecls = f.body.declarations;
        for (VariableDeclaration v : varDecls) {
            var varType = v.typeNode.typeString;
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
                builder.append(s.getClass()); // TODO once we actually have statement subclasses, print some of their internals here?
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
            
            for (Function f : program.functions) {
                printFunction(f);
            }
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
