import ast.*;
import ir.*;
import java.io.*;
import org.antlr.runtime.*;
import semantic.*;

/*
 * Uses the ANTLR tool to perform lexical analysis and generate
 * a parser based on the grammar for the unnamed language.
 */
public class Compiler {
    
	private static void handleCompilationError() {
		// Allows error code checking to automate testing.
		System.exit(1); 
	}

    public static void main (String[] args) throws IOException {
		
		if (args.length == 0) {
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
			Program program = parser.program();
            
			// Ensure the type system and language semantics are respected.
            TypeCheckVisitor semanticVisitor = new TypeCheckVisitor();
            semanticVisitor.visit(program);

			// Create the intermediate representation.
			IRVisitor irVisitor = new IRVisitor();
			irVisitor.visit(program);

			System.out.println(irVisitor.buildProgram());

			// TODO create a .ir file and use jasmin to create a class file
        }
        catch (RecognitionException e )	{
    		// A lexical or parsing error occured.
	    	// ANTLR will have already printed information about the error
		    // to System.err.

			handleCompilationError();
		}
		catch(SemanticException e) {
			// Print out the cause of the error and positional information 
			// if appropriate.
			System.err.println(e.getMessageWithPosition());

			handleCompilationError();
		}
		catch (Exception e) {
			System.err.println(e);
			e.printStackTrace();

			handleCompilationError();
		}
	}
}
