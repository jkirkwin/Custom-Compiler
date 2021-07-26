package codegen;

import ir.*;

/**
 * A Visitor for an IRProgram that generates a corresponding 
 * Jasmin program. JasminVisitors are single-use, and should
 * only be used to visit a single IRProgram.
 */
public class JasminVisitor implements IRProgramVisitor<Void> { 

    // TODO Add the necessary state:
    // * Some kind of label factory
    // * Mappings for temporaries to their positions in the stack?
    //  * Local variable table is used for this I think

    private final JasminProgram.Builder programBuilder;
    private JasminMethod.Builder methodBuilder;

    public JasminVisitor() {
        programBuilder = new JasminProgram.Builder();
    }

    // TODO Add method to get the program after visiting.
    
    /**
     * Returns the generated JasminProgram after visiting and IRProgram.
     * This must only be called *after* calling visit(IRProgram).
     */
    public JasminProgram buildJasminProgram() {
        return programBuilder.build();
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

    public Void visit(FunctionCallInstruction irFunctionCall) {
        throw new UnsupportedOperationException("Not implemented"); // TODO
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

    public Void visit(IRFunction irFunction) {
        // We rename the UL's main function to __main for clarity,
        // since the JVM requires a static method of the same name.
        String methodName = irFunction.name.equals("main") ? "__main" : irFunction.name;
        
        methodBuilder = new JasminMethod.Builder()
            .setStatic(true)
            .withMethodName(methodName)
            .withMethodType(irFunction.type);

        // TODO Build up the local variable table and whatever else is needed for the temporaries
        // TODO Visit each of the function's statements

        // Add the function to the program
        programBuilder.addMethod(methodBuilder.build());

        return null;
    }

    public Void visit(IRInstruction irInstruction) {
        throw new UnsupportedOperationException("visit called with IRInstruction parameter");
    }

    public Void visit(IRProgram irProgram) {
        programBuilder.withClassName(irProgram.programName)
                      .withSourceFile(irProgram.programName + ".ir");

        // TODO Add an object initializer method
        // TODO add the "true" main method that calls the renamed static __main() method

        for (var function : irProgram.functions) {
            function.accept(this);
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
        throw new UnsupportedOperationException("Not implemented"); // TODO
    }

    public Void visit(TemporaryAssignmentInstruction irTempAssignmentInstruction) {
        throw new UnsupportedOperationException("Not implemented"); // TODO
    }

    public Void visit(Temporary irTemp) {
        throw new UnsupportedOperationException("visit called with abstract Temporary parameter");
    }

    public Void visit(TrueTemp irTemp) {
        throw new UnsupportedOperationException("Not implemented"); // TODO
    }
}
