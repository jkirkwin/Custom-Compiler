package ir;

import type.Type;

/**
 * A pool of (@Link Temporary} instances.
 * TODO Improve docstring once interface is fleshed out
 */
public interface TempPool {

    /**
     * The maximum allowable number of temporaries (of all sub-types) 
     * allowed in the IR. This limit is enforced by the JVM.
     */
    public static final int MAX_TEMPORARIES = 65535; 

    /**
     * Allocates and returns a new temporary in the 
     * parameter group with the given type.
     */
    public ParamTemp acquireTemporaryParam(Type type) throws TemporaryOverflowException;

    /**
     * Allocates and returns a new temporary in the 
     * locals group with the given type.
     */
    public LocalTemp acquireTemporaryLocal(Type type) throws TemporaryOverflowException;

    /**
     * Allocates and returns a new temporary which is neither a
     * parameter or local.
     */
    public TrueTemp acquireTemporary(Type type) throws TemporaryOverflowException;

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