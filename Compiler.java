import ast.*;
import codegen.*;
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

	private static String programNameFromFileName(String fileName) throws Exception {
		// Remove leading directory names
		var dirSeparatorIndex = fileName.lastIndexOf(File.separator);
		if (dirSeparatorIndex == fileName.length()) {
			throw new Exception("Input file name should not end in a " + File.separator);
		}
		else if (dirSeparatorIndex >= 0) {
			fileName = fileName.substring(dirSeparatorIndex + 1);
		}

		// Move file extension
		var extIndex = fileName.indexOf('.');
		if (extIndex == -1) {
			// No file extension is present
			return fileName;
		}
		else if (extIndex == 0) {
			throw new Exception("Input file name should not begin with a period.");
		}
		else {
			// Remove the file extension
			return fileName.substring(0, extIndex);			
		}
	}

    public static void main (String[] args) throws IOException {
		
		if (args.length == 0) {
			System.out.println("Usage: Compiler filename.ul");
			return;
		}

		String sourceFileName = args[0];

		ANTLRInputStream input;
		try {
			input = new ANTLRInputStream(new FileInputStream(sourceFileName));	
		}
		catch (FileNotFoundException e) {
			System.out.println(String.format("Could not open file %s", sourceFileName));
			return;
		}

		// The name of the grammar here is "UnnamedLanguage", so ANTLR 
		// generates UnnamedLanguageLexer and UnnamedLanguageParser
		UnnamedLanguageLexer lexer = new UnnamedLanguageLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		UnnamedLanguageParser parser = new UnnamedLanguageParser(tokens);

		try {
			Program astProgram = parser.program();
            
			// Ensure the type system and language semantics are respected.
            TypeCheckVisitor semanticVisitor = new TypeCheckVisitor();
            semanticVisitor.visit(astProgram);

			// Create the intermediate representation.
			IRAstVisitor irVisitor = new IRAstVisitor();
			String programName = programNameFromFileName(sourceFileName);
			IRProgram irProgram = irVisitor.buildIRProgram(astProgram, programName);
			
			// Generate Jasmin code from the IR representation.
			JasminVisitor jasminVisitor = new JasminVisitor();
			jasminVisitor.visit(irProgram);
			JasminProgram jasminProgram = jasminVisitor.buildJasminProgram();

			// TODO Save the jasmin program to a .j file. May want to do something 
			// similar as was done in the IRVisitor.
			
			// TODO See if it's possible to run Jasmin to create the class file
			// as part of this process. Not required for the assignment but would 
			// be nice for testing.
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
