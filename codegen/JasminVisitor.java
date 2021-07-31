package codegen;

import ir.*;
import common.*;
import type.*;
import java.io.File;
import java.io.PrintWriter;
import java.util.List;

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
            return "a"; // TODO Note that this is more complex for array accesses, where we need to use castore, bastore, etc.
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

    /**
     * Map a boolean value to its integer representation. This is
     * used to let us load and store boolean values as integers on
     * the operand stack.
     */
    private static int booleanToInt(boolean b) {
        if (b) {
            return 1;
        }
        else {
            return 0;
        }
    }

    /**
     * Map an integer reperesentation of a boolean back its boolean value
     */
    private static boolean intToBoolean(int i) {
        if (i == 1) {
            return true;
        }
        else if (i == 0) {
            return false;
        }
        else {
            throw new IllegalArgumentException("Cannot convert integer " + i + " to boolean");
        }
    }

    private PrintWriter fileWriter;
    private String className;
    private int stackLimit;
    private final Environment<String, MethodType> functionEnv;
    // private final JasminProgram.Builder programBuilder;
    // private JasminMethod.Builder methodBuilder;
    private LabelFactory labelFactory;

    public JasminVisitor() {
        functionEnv = new Environment<String, MethodType>();
    }
    
    /**
     * Write a label definition to the current function with a partial 
     * indent.
     */
    private void writeLabelDeclaration(Label label) {
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

    /**
     * Uses a store instruction to pop the value off the top of the
     * operand stack into the given variable.
     */
    private void popVariable(Temporary temp) { // TODO Test this for all types
        var prefix = jvmTypeMnemonic(temp.type());
        fileWriter.append('\t')
                  .append(prefix)
                  .append("store ")
                  .println(temp.globalIndex()); 
    }

    public Void visit(ArrayAssignmentInstruction irArrayAssignment) {
        throw new UnsupportedOperationException("Not implemented"); // TODO
    }

    public Void visit(BinaryOperation irBinaryOperation) {
        throw new UnsupportedOperationException("Not implemented"); // TODO
    }

    public Void visit(ConditionalJumpInstruction irConditionalJump) {
        // Push a variable holding the condition onto the operand stack.
        irConditionalJump.condition.accept(this);
        
        // The value on the stack was pushed as an integer. Determine
        // if it corresponds to boolean true or false. ifne compares
        // against 0 (false).
        String labelString = irConditionalJump.label.toJasminString();
        fileWriter.append('\t').append("ifne ").println(labelString);
    
        return null;
    }

    private void pushMethodArguments(IRFunctionCall functionCall) {
        for (var arg : functionCall.args) {
            pushVariable(arg);
        }
    }

    public Void visit(FunctionCallInstruction callInstruction) {
        // This is just a function call for which we discard the return value
        // if there is one.
    
        // Make the actual function call
        callInstruction.functionCall.accept(this);

        // If there is a return value, pop it off the stack to discard it.
        String functionName = callInstruction.functionCall.functionName;
        var functionType = functionEnv.lookup(functionName);
        if (!TypeUtils.isVoid(functionType.returnType)) {
            fileWriter.println("\tpop");
        }
        
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
        // Push the constant onto the operand stack.
        // Booleans and characters need to be converted to
        // integer values.

        fileWriter.print("\tldc ");

        if (TypeUtils.isBoolean(irConstant.type())) {
            var value = irConstant.value();
            assert (value instanceof Boolean);
            
            fileWriter.println(booleanToInt((boolean)value));
        }
        else if (TypeUtils.isChar(irConstant.type())) {
            var value = irConstant.value();
            assert (value instanceof Character);
            char charValue = ((Character)value).charValue();

            fileWriter.println(((int)charValue)); 
        }
        else {
            fileWriter.println(irConstant.toString());
        }

        return null;
    }

    public Void visit(IRFunctionCall irFunctionCall) {
        pushMethodArguments(irFunctionCall);

        // Make the actual function call instruction
        var functionName = irFunctionCall.functionName;
        var functionType = functionEnv.lookup(functionName);
        fileWriter.append('\t')
                  .append("invokestatic ")
                  .append(className)
                  .append('/')
                  .append(functionName)
                  .println(functionType.toString());

        return null;
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
        // var signature = new JasminMethodSignature(true, irFunction.type, methodName); // TODO remove?


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

        // Write the stack limit
        fileWriter.append('\t').append(".limit stack ").println(stackLimit);
        fileWriter.println();

        // Insert the start label before any of the "real" instructions
        // methodBuilder.addStatement(new JasminLabelInstruction(startLabel)); // TODO Just print the label manually?
        writeLabelDeclaration(startLabel);

        // Visit each of the function's statements
        for (var instruction : irFunction.instructions) {
            instruction.accept(this);
        }

        // Before completing the function, add the end label.
        // It's okay if this comes after a return instruction
        // because we're never jumping to the label.
        // methodBuilder.addStatement(new JasminLabelInstruction(endLabel)); // TODO Just print the label manually?
        writeLabelDeclaration(endLabel);

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

    /**
     * Chooses a value for the stack limit that will be sufficient
     * for all functions.
     */
    private void setStackLimit(List<IRFunction> functions) {
        stackLimit = 5; // Default minimum value

        for (var f : functions) {
            int argCount = f.type.argumentTypes.size();
            int minStackLimit = argCount + 2; 

            if (minStackLimit > stackLimit) {
                stackLimit = minStackLimit;
            }
        }
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

            // Add all the functions' types to an environment, and use their 
            // arg counts to set a reasonable stack limit for all functions.
            for (var function : irProgram.functions) {
                functionEnv.bind(function.name, function.type);
            }
            setStackLimit(irProgram.functions);

            // Generate the code for each function
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
        String labelString = irJumpInstruction.label.toJasminString();
        fileWriter.append("\tgoto ").println(labelString);

        return null;
    }

    public Void visit(LabelInstruction irLabelInstruction) {
        writeLabelDeclaration(irLabelInstruction.label);
        return null;
    }

    public Void visit(Label irLabel) {
        throw new UnsupportedOperationException("Code generation not required for label usages"); // TODO Revisit this if necessary
    }

    public Void visit(LocalTemp irTempLocal) {
        // If this is called the temp is being used on the 
        // RHS of an assignment, so we just need to push it
        // onto the operand stack.
        pushVariable(irTempLocal); 

        return null;
    }

    public Void visit(NegationOperation irNegationOperation) {
        if ( !TypeUtils.isBoolean(irNegationOperation.operandType) ) {
            throw new IllegalArgumentException("Code generation for negations is only supported for booleans");
        }

        // Take the XOR of the value to be negated with 1 to flip 0->1 or 1->0
        pushVariable(irNegationOperation.operand);
        fileWriter.println("\tldc 1");
        fileWriter.println("\tixor");

        return null;
    }

    public Void visit(ParamTemp irTempParam) {
        // If this is called the temp is being used on the 
        // RHS of an assignment, so we just need to push it
        // onto the operand stack.
        pushVariable(irTempParam); 

        return null;
    }

    public Void visit(PrintInstruction irPrintInstruction) {
        var temp = irPrintInstruction.temp;

        // Get access to system.out and printstream
        fileWriter.println("\tgetstatic java/lang/System/out Ljava/io/PrintStream;");

        // Push the value to be printed onto the operand stack.
        pushVariable(temp);

        // Call the appropriate print overload
        fileWriter.append('\t')
                  .append("invokevirtual java/io/PrintStream/print(")
                  .append(temp.type().toJasminString())
                  .println(")V");

        return null;
    }

    public Void visit(PrintlnInstruction irPrintlnInstruction) {
        var temp = irPrintlnInstruction.temp;

        // Get access to system.out and printstream
        fileWriter.println("\tgetstatic java/lang/System/out Ljava/io/PrintStream;");

        // Push the value to be printed onto the operand stack.
        pushVariable(temp);

        // Call the appropriate print overload
        fileWriter.append('\t')
                  .append("invokevirtual java/io/PrintStream/println(")
                  .append(temp.type().toJasminString())
                  .println(")V");

        return null;
    }

    public Void visit(ReturnInstruction irReturnInstruction) {
        
        
        if(irReturnInstruction.operand.isPresent()) {
            var temp = irReturnInstruction.operand.get();

            // Push the value onto the operand stack and use the appropriate
            // return instruction for the type of the variable being returned.
            pushVariable(temp);
            
            String returnPrefix = jvmTypeMnemonic(temp.type());
            fileWriter.append('\t')
                      .append(returnPrefix)
                      .println("return");
        }
        else {
            fileWriter.println("\treturn");
        }

        return null;
    }

    public Void visit(TemporaryAssignmentInstruction irTempAssignmentInstruction) {
        // Generate code to compute the value and push it onto
        // the operand stack
        var value = irTempAssignmentInstruction.value;
        value.accept(this);

        // Store the value into the destination
        Temporary dest = irTempAssignmentInstruction.destination;
        popVariable(dest);

        return null;
    }

    public Void visit(Temporary irTemp) {
        throw new UnsupportedOperationException("visit called with abstract Temporary parameter");
    }

    public Void visit(TrueTemp irTemp) {
        // If this is called the temp is being used on the 
        // RHS of an assignment, so we just need to push it
        // onto the operand stack.
        pushVariable(irTemp); 

        return null;
    }
}
