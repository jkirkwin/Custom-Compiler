package ir;

import type.Type;

/**
 * A pool of (@Link Temporary} instances. Encapsulates the management
 * of the index space outside of the Temporary classes themselves and 
 * provides mechanisms for acquiring and returning temporaries of each
 * sub-type.
 */
public interface TempPool {

    /**
     * The maximum allowable number of temporaries (of all sub-types) 
     * allowed in the IR. This limit is enforced by the JVM.
     */
    public static final int MAX_TEMPORARIES = 65535; 

    /**
     * Allocates and returns a new temporary in the parameter group
     * with the given type. Adds the provided string to the temporary
     * for diagnostic purposes.
     */
    public ParamTemp acquireParam(Type type, String sourceString) throws TemporaryOverflowException;

    /**
     * Allocates and returns a new temporary in the locals group
     * with the given type. Adds the provided string to the temporary
     * for diagnostic purposes.
     */
    public LocalTemp acquireLocal(Type type, String sourceString) throws TemporaryOverflowException;

    /**
     * Allocates and returns a new temporary which is neither a
     * parameter or local.
     */
    public TrueTemp acquireTemp(Type type) throws TemporaryOverflowException;

    /**
     * Release a temporary. After this, the returned temporary may be 
     * re-acquired.
     */
    public void release(Temporary temp);

    /**
     * Resets the pool to its initial state in which no temporaries have
     * been acquired and no types are known for any temporary id.
     */
    public void clear();
}