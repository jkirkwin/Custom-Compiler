package ir;

import common.Label;

/**
 * A Visitor for an IRProgram and all its components.
 * Different visitors may use different return types 
 * for their visit methods. Void may be used if nothing
 * needs to be passed between visit calls, or Object can
 * be used with casting in the case that the visit methods
 * need to return multiple types.
 */
public interface IRProgramVisitor <T> { 

    public T visit(ArrayAssignmentInstruction irArrayAssignment);

    public T visit(BinaryOperation irBinaryOperation);

    public T visit(ConditionalJumpInstruction irConditionalJump);

    public T visit(FunctionCallInstruction irFunctionCall);

    public T visit(IRArrayAccess irArrayAccess);

    public T visit(IRArrayCreation irArrayCreation);

    public T visit(IRAssignableExpression irAssignableExpression);

    public T visit(IRConstant irConstant);

    public T visit(IRFunctionCall irFunctionCall);

    public T visit(IRFunction irFunction);

    public T visit(IRInstruction irInstruction);

    public T visit(IRProgram irProgram); 
    
    public T visit(JumpInstruction irJumpInstruction);

    public T visit(LabelInstruction irLabelInstruction);

    public T visit(Label irLabel);

    public T visit(LocalTemp irLocalTemp);

    public T visit(NegationOperation irNegationOperation);

    public T visit(ParamTemp irTempParam);

    public T visit(PrintInstruction irPrintInstruction);

    public T visit(PrintlnInstruction irPrintlnInstruction);

    public T visit(ReturnInstruction irReturnInstruction);

    public T visit(TemporaryAssignmentInstruction irTempAssignmentInstruction);

    public T visit(Temporary irTemp);

    public T visit(TrueTemp irTemp);
}
