package ir;

import type.*;

public class NegationOperation {

    private static enum NegationTypes implements IRAssignableExpression {
        BITWISE("!"),
        NUMERIC("-");

        private final String operatorString;

        private NegationTypes(String s) {
            operatorString = s;
        }

        public String toString() {
            return operatorString;
        }
    }
    
    private static boolean isValidType(Type t, NegationTypes negationType) {

        switch(negationType) {
            case BITWISE:
                return type.TypeUtils.isBoolean(t) 
                    || type.TypeUtils.isInt(t);
            
            case NUMERIC:
                return type.TypeUtils.isFloat(t) 
                    || type.TypeUtils.isInt(t);

            default:
                throw new IllegalStateException("Type not supported: " + t.toString());
        }
    }

    /**
     * Factory method to create a  bitwise (!) negation operation for the
     * provided temporary. The type of the operand dictates the type of 
     * the operation.
     */
    public static NegationOperation forBitwiseNegation(Temporary temp) {
        var tempType = temp.type();
        if (!isValidType(tempType, NegationTypes.BITWISE)) {
            throw new IllegalStateException("Bitwise negation operation does not apply to type " + tempType.toString());
        }

        return new NegationOperation(temp, NegationTypes.BITWISE);
    }

    /**
     * Factory method to create a numeric (-) negation operation for the
     * provided temporary. The type of the operand dictates the type of 
     * the operation.
     */
    public static NegationOperation forNumericNegation(Temporary temp) {
        var tempType = temp.type();
        if (!isValidType(tempType, NegationTypes.NUMERIC)) {
            throw new IllegalStateException("Numeric negation operation does not apply to type " + tempType.toString());
        }

        return new NegationOperation(temp, NegationTypes.NUMERIC);
    }

    private final Temporary operand;
    private final Type operandType;
    private final NegationTypes negationType; 

    private NegationOperation(Temporary temp, NegationTypes negType) {
        operand = temp;
        operandType = operand.type();
        negationType = negType;

        assert (isValidType(operandType, negationType));
    }

    public String toString() {
        return operandType.toIRString() + negationType.toString();
    }
}