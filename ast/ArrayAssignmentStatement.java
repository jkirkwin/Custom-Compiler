package ast;

/**
 * Reprents an assignment operation where the LHS is a position
 * in an array.
 */
public class ArrayAssignmentStatement extends Statement {

    /**
     * The identifier of the array being assigned to
     */
    public final Identifier arrayId;

    /**
     * An expression whose value is the index at which to assign another value.
     */
    public final Expression indexExpression;

    /** 
     * An expression whose value should be assigned to the array at 
     * the specified index
     */
    public final Expression valueExpression;

    public ArrayAssignmentStatement(Identifier arrayId, Expression indexExpression, Expression valueExpression) {
        super(arrayId);
       
        this.arrayId = arrayId;
        this.indexExpression = indexExpression;
        this.valueExpression = valueExpression;
    }

}
