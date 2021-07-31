package codegen;

import ir.*;
import common.*;
import type.*;
import java.io.File;
import java.io.PrintWriter;

/**
 * A Visitor for an IRProgram that generates a corresponding 
 * Jasmin program. JasminVisitors are single-use, and should
 * only be used to visit a single IRProgram.
 * 
 * The generated code is saved in a file with a .j extension.
 */
public class JasminVisitor implements IRProgramVisitor<Void> { 

    private static final String OBJECT_CLASS = "java/lang/Object";
    private static final String UL_MAIN_METHOD = "__main";

    /**
     * Returns the JVM type mnemonic that is used to prefix load,
     * store, etc. instructions. 
     */
    private static String jvmTypeMnemonic(Type t) {
        if (TypeUtils.isReferenceType(t)) {
            return "a";
        }
        else if (TypeUtils.isInt(t) || TypeUtils.isBoolean(t) || TypeUtils.isChar(t)) {
            return "i";
        }
        else if (TypeUtils.isFloat(t)) {
            return "f";
        }
        else {
            throw new IllegalArgumentException("Cannot get mnemonic for type " + t.toString());
        }
    }

    // TODO Add the necessary state:
    // * Mappings for temporaries to their positions in the stack?
    //  * Local variable table is used for this I think

    private PrintWriter fileWriter;
    private String className;
    private final Environment<String, MethodType> functionEnv;


    // private final JasminProgram.Builder programBuilder;

    // Per-function objects
    // private JasminMethod.Builder methodBuilder;
    private LabelFactory labelFactory;

    public JasminVisitor() {
        functionEnv = new Environment<String, MethodType>();
    }
    
    /**
     * Write a label definition to the current function with a partial 
     * indent.
     */
    private void writeLabel(Label label) {
        fileWriter.append("  ").append(label.toJasminString()).println(":");
    }

    /**
     * Uses a load instruction to push the value of a variable onto the 
     * operand stack.
     */
    private void pushVariable(Temporary temp) { // TODO Test this for all types
        // Choose appropriate load instruction based on the type 
        // of the value to be pushed. 
        // E.g. an integer will result in 
        //      iload <temp_number>
        var prefix = jvmTypeMnemonic(temp.type());
        fileWriter.append('\t')
                  .append(prefix)
                  .append("load ")
                  .println(temp.globalIndex()); 
    }

    public Void visit(ArrayAssignmentInstruction irArrayAssignment) {
        throw new UnsupportedOperationException("Not implemented"); // TODO
    }

    public Void visit(BinaryOperation irBinaryOperation) {
        throw new UnsupportedOperationException("Not implemented"); // TODO
    }

    public Void visit(ConditionalJumpInstruction irConditionalJump) {
        throw new UnsupportedOperationException("Not implemented"); // TODO
    }

    private void pushMethodArguments(IRFunctionCall functionCall) {
        for (var arg : functionCall.args) {
            pushVariable(arg);
        }
    }

    public Void visit(FunctionCallInstruction callInstruction) {
        // This is just a function call for which we discard the return value
        // if there is one.

        // TODO Share some of this logic with function-call assignment (TemporaryAssignmentInstruction with IRFunctionCall RHS)

        // Push the arguments onto the stack
        pushMethodArguments(callInstruction.functionCall);

        // Make the actual function call
        var functionName = callInstruction.functionCall.functionName;
        var functionType = functionEnv.lookup(functionName);
        fileWriter.append('\t')
                  .append("invokestatic ")
                  .append(className)
                  .append('/')
                  .append(functionName)
                  .println(functionType.toString());
        
        return null;
    }

    public Void visit(IRArrayAccess irArrayAccess) {
        throw new UnsupportedOperationException("Not implemented"); // TODO
    }

    public Void visit(IRArrayCreation irArrayCreation) {
        throw new UnsupportedOperationException("Not implemented"); // TODO
    }

    public Void visit(IRAssignableExpression irAssignableExpression) {
        throw new UnsupportedOperationException("visit called with abstract IRAssignableOperation parameter");
    }

    public Void visit(IRConstant irConstant) {
        throw new UnsupportedOperationException("Not implemented"); // TODO
    }

    public Void visit(IRFunctionCall irFunctionCall) {
        throw new UnsupportedOperationException("Not implemented"); // TODO
    }

    // Simple linear search to find all labels in the function and return 
    // the index of the largest one.
    private int getMaxLabelUsed(IRFunction function) {
        int max = -1;
        
        for (var instruction : function.instructions) {
            if (instruction instanceof LabelInstruction) {
                LabelInstruction labelInstruction = (LabelInstruction) instruction;
                int index = labelInstruction.label.index;
                if (index > max) {
                    max = index;
                }
            }
        }

        return max;
    }

    public Void visit(IRFunction irFunction) {
        // We rename the UL's main function to __main for clarity,
        // since the JVM requires a static method of the same name.
        String methodName = irFunction.name.equals("main") ? UL_MAIN_METHOD : irFunction.name;
        var signature = new JasminMethodSignature(true, irFunction.type, methodName); // TODO remove?


        // Write the method signature
        fileWriter.append(".method public static ")
                  .append(methodName)
                  .println(irFunction.type.toJasminString());

        


        // methodBuilder = new JasminMethod.Builder().withSignature(signature); // TODO Maybe remove or repurpose?

        // In case we need to generate extra labels that are not present
        // in the IR, find the maximum label used in the IR so we know
        // where to pick up from.
        int nextLabel = getMaxLabelUsed(irFunction) + 1;
        labelFactory = new LabelFactory(nextLabel);

        // Before processing the variable/temporary declarations, allocate two 
        // new "dummy" labels who will act as beginning and end markers for 
        // all variable scopes. Since all variables share the same scope, this
        // is the simplest way to force the correct behaviour.
        var startLabel = labelFactory.getLabel();
        var endLabel = labelFactory.getLabel();

        // Iterate over each of the function's temporaries and create declaration
        // directives for each one them.
        fileWriter.append("\t.limit locals ").println(irFunction.temps.size());  // It's okay if this is 0.
        for (var temp : irFunction.temps) {
            // methodBuilder.addVariable(new JasminVariableDeclaration(temp, startLabel, endLabel)); // TODO remove
            var declaration = new JasminVariableDeclaration(temp, startLabel, endLabel);
            fileWriter.append('\t').println(declaration.toString());
        }

        // Insert the start label before any of the "real" instructions
        // methodBuilder.addStatement(new JasminLabelInstruction(startLabel)); // TODO Just print the label manually?
        writeLabel(startLabel);

        // Visit each of the function's statements
        for (var instruction : irFunction.instructions) {
            instruction.accept(this);
        }

        // Before completing the function, add the end label.
        // It's okay if this comes after a return instruction
        // because we're never jumping to the label.
        // methodBuilder.addStatement(new JasminLabelInstruction(endLabel)); // TODO Just print the label manually?
        writeLabel(endLabel);

        // Add the function to the program
        // programBuilder.addMethod(methodBuilder.build());

        fileWriter.println(".end method");
        fileWriter.println();

        return null;
    }

    public Void visit(IRInstruction irInstruction) {
        throw new UnsupportedOperationException("visit called with IRInstruction parameter");
    }

    /**
     * Writes the .source, .class, and .super directives to the
     * output file.
     */
    private void writeJasminHeader() {
        fileWriter.append(".source ")
        .append(className)
        .println(".ir");

        fileWriter.append(".class public ")
                .println(className);

        fileWriter.append(".super ")
                .println(OBJECT_CLASS);

        fileWriter.println();
    }

    /**
     * Writes the boilerplate object initializer method to the output file.
     */
    private void writeObjectInitializer() {
        // TODO Replace this with a shared function for when we're visiting an irFunction?
        // TODO This is super gross but if it works, it works...
        fileWriter.println(".method public <init>()V");
        fileWriter.append('\t').println("aload_0");
        fileWriter.append('\t').println("invokenonvirtual java/lang/Object/<init>()V");
        fileWriter.append('\t').println("return");
        fileWriter.println(".end method");
    }

    /**
     * Writes the boilerplate main method which calls the UL's renamed main
     * method.
     */
    private void writeMainMethod() {
        // TODO Factor some of this out. We at least want a printMethodSignature function.
        // TODO May even want to make a method builder that handles indentation etc for us.
        fileWriter.println("; Java main method to call the UL main method (" + UL_MAIN_METHOD + ")");
        fileWriter.println(".method public static main([Ljava/lang/String;)V");
        fileWriter.append('\t').println(".limit locals 1");
        fileWriter.append('\t').println(".limit stack 4");
        fileWriter.append('\t').append("invokestatic ").append(className).println("/__main()V");
        fileWriter.append('\t').println("return");
        fileWriter.println(".end method");
    }

    public Void visit(IRProgram irProgram) throws Exception {
        // Open the output file 
        className = irProgram.programName;
        String outputFileName = className + ".j";
        File outputFile = new File(outputFileName);
        try (PrintWriter w = new PrintWriter(outputFile)) {
            // Store the reference so the rest of the visitor can use it
            fileWriter = w; 

            // Write the header, main method, and object initializer boilerplate.
            writeJasminHeader();
            writeObjectInitializer();
            fileWriter.println();
            writeMainMethod();
            fileWriter.println();

            // Add all the functions' types to an environment
            for (var function : irProgram.functions) {
                functionEnv.bind(function.name, function.type);
            }

            for (var function : irProgram.functions) {
                function.accept(this);
            }
        }
        catch (Exception e) {
            fileWriter = null; // Clear the PrintWriter reference on error
            throw e;
        }
    
        return null;
    }

    public Void visit(JumpInstruction irJumpInstruction) {
        throw new UnsupportedOperationException("Not implemented"); // TODO
    }

    public Void visit(LabelInstruction irLabelInstruction) {
        throw new UnsupportedOperationException("Not implemented"); // TODO
    }

    public Void visit(Label irLabel) {
        throw new UnsupportedOperationException("Not implemented"); // TODO
    }

    public Void visit(LocalTemp irLocalTemp) {
        throw new UnsupportedOperationException("Not implemented"); // TODO
    }

    public Void visit(NegationOperation irNegationOperation) {
        throw new UnsupportedOperationException("Not implemented"); // TODO
    }

    public Void visit(ParamTemp irTempParam) {
        throw new UnsupportedOperationException("Not implemented"); // TODO
    }

    public Void visit(PrintInstruction irPrintInstruction) {
        throw new UnsupportedOperationException("Not implemented"); // TODO
    }

    public Void visit(PrintlnInstruction irPrintlnInstruction) {
        throw new UnsupportedOperationException("Not implemented"); // TODO
    }

    public Void visit(ReturnInstruction irReturnInstruction) {
        if(irReturnInstruction.operand.isPresent()) {
            // TODO I think we need to use ireturn etc. here after loading the return value.
            // Check the behaviour for chars, booleans, and other types to see which to use.
            throw new UnsupportedOperationException("Not implemented");
        }
        else {
            fileWriter.println("\treturn");
        }

        return null;
    }

    public Void visit(TemporaryAssignmentInstruction irTempAssignmentInstruction) {
        throw new UnsupportedOperationException("Not implemented"); // TODO NEXT
    }

    public Void visit(Temporary irTemp) {
        throw new UnsupportedOperationException("visit called with abstract Temporary parameter");
    }

    public Void visit(TrueTemp irTemp) {
        throw new UnsupportedOperationException("Not implemented"); // TODO
    }
}
