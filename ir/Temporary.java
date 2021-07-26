package ir;

import type.Type;

/**
 * Represents a "temporary" in the IR language. A temporary is 
 * the basic unit of the IR. Each one has a unique integral identifier
 * (or "index") and a Type.
 * 
 * The temporary with index i is denoted T_i.
 * 
 * There are three sub-types of temporaries: parameters, locals, and
 * "true" temporaries. These three groups partition the common index 
 * space shared by all temporaries into three contiguous groups. 
 * 
 *  1.  Indices from 0 to p are used by the parameters, and they
 *      are denoted P_i, but can also be refered to as T_i.
 * 
 *  2.  Second, all indices from p to p + l are used by the locals, 
 *      and they are denoted L_i, but can also be referred to as 
 *      T_j where j = i + p.
 * 
 *  3.  Finally, the remaining "true" temporaries take up the index 
 *      space above p + l, and they are denoted T_i.
 */
public abstract class Temporary implements IRAssignableExpression {

    private final int localId;
    private final int groupOffset;
    private final Type type;

    public Temporary(int localId, int groupOffset, Type type) {
        this.localId = localId;
        this.groupOffset = groupOffset;
        this.type = type;
    }

    /**
     * The index of the temporary within the index space containing all 
     * sub-types. If the group to which this temporary belongs begins at
     * index i and the local_index() result is j, this will return i + j.
     */
    public int globalIndex() {
        return groupOffset + localId;
    }

    /**
     * The index of the temporary within its sub-type group space.
     */
    public int localIndex() {
        return localId;
    }

    /**
     * The associated {@link Type} of the temporary.
     */
    public Type type() {
        return type;
    }

    /**
     * The textual representation of a reference to the temporary global 
     * indexing.
     */
    public abstract String toString();

    /**
     * The full text to be used for a temporary declaration.
     */
    public String declarationString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\tTEMP ")
          .append(globalIndex())
          .append(':')
          .append(type().toIRString());

        if (hasAlias()) {
            sb.append(" [")
              .append(getAlias())
              .append(']');
        }
              
        return sb.toString();
    }

    /**
     * Answers whether this temporary has a corresponding
     * alias. Temporaries used to represent named entities
     * (e.g. parameters, local variables) have such and alias.
     */
    public abstract boolean hasAlias();

    /**
     * Returns the alias for the temporary, if one exists.
     * If hasAlias() is false, this will throw an exception.
     */
    public abstract String getAlias();

    /**
     * Similar to getAlias(), but returns just the identifier
     * used in the source code and not the prefix indicating 
     * the "type" of the source entity (e.g. parameter, local
     * variable, etc.).
     */
    public abstract String getRawAlias();

}
