import org.antlr.runtime.*;
import java.io.*;
import ast.*;

/*
 * Uses the ANTLR tool to perform lexical analysis and generate
 * a parser based on the grammar for the unnamed language.
 */
public class Compiler {
	
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
			System.out.println("Found " + program.functions.size() + " functions with types:");
            for (Function f : program.functions) {
                    System.out.println(f.declaration.typeNode.typeString);
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
