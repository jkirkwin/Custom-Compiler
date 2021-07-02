package ir;

import common.TestUtils;
import type.*;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

/**
 * The most basic implementation of {@link TempPool} which 
 * does not support re-using temporaries after the are released.
 * If the release method is called, it does nothing. 
 * 
 * Client code should not call release if operating on a TempFactory
 * directly. It is preferable to access the TempFactory through the
 * TempPool interface.
 * 
 * Since re-use is not supported, it is an error to request a parameter
 * after a local or true temporary, and it is an error to request a local
 * after requesting a ture temporary.
 */
public class TempFactory implements TempPool {

    /**
     * Enumerates the groups of temporaries which each correspond to 
     * a state of the temp factrory. The order of the enumerants must
     * be in increasing order of appearance in the IR as the ordinal 
     * value is used.
     */
    private static enum TempGroups {
        PARAMETERS,
        LOCALS,
        TRUE_TEMPORARIES
    }

    private final Vector<Temporary> temporaries;
    private TempGroups tempGroup;

    // Counters for each group
    private int paramCount = 0;
    private int localCount = 0;
    private int trueTempCount = 0;

    public TempFactory() {
        temporaries = new Vector<Temporary>();
        tempGroup = TempGroups.PARAMETERS;
    }

    public ParamTemp acquireParam(Type type, String sourceString) throws TemporaryOverflowException {
        updateGroup(TempGroups.PARAMETERS);
        throwIfIndexSpaceFull();

        var temp = new ParamTemp(paramCount, type, sourceString);
        ++paramCount;
        temporaries.add(temp);

        return temp;
    }

    public LocalTemp acquireLocal(Type type, String sourceString) throws TemporaryOverflowException {
        updateGroup(TempGroups.LOCALS);
        throwIfIndexSpaceFull();

        var temp = new LocalTemp(localCount, paramCount, type, sourceString);
        ++localCount;
        temporaries.add(temp);

        return temp;
    }

    public TrueTemp acquireTemp(Type type) throws TemporaryOverflowException {
        updateGroup(TempGroups.TRUE_TEMPORARIES);
        throwIfIndexSpaceFull();

        int offset = paramCount + localCount;
        var temp = new TrueTemp(trueTempCount, offset, type);
        ++trueTempCount;
        temporaries.add(temp);

        return temp;
    }

    /**
     * Represents a forward transition in the state machine modelled by 
     * the factory. Called when a request for a temporary is made to 
     * ensure that the request can be met and that the current temporary
     * group is updated if necessary. If this method returns, then the 
     * request is valid.
     */
    private void updateGroup(TempGroups requestedTempType) {
        if (tempGroup.ordinal() <= requestedTempType.ordinal()) {
            tempGroup = requestedTempType;
        }
        else {
            String message = "Cannot supply parameter- or local-temporary " + 
                             "after supplying temporary from subsequent group";
            throw new IllegalStateException(message);
        }
    }

    private void throwIfIndexSpaceFull() throws TemporaryOverflowException {
        if (paramCount + localCount + trueTempCount >= MAX_TEMPORARIES) {
            throw new TemporaryOverflowException();
        }
    }

    public void release(Temporary temp) {
        // No-op. Re-using temporaries is not supported.
    }

    public void clear() {
        temporaries.clear();
        tempGroup = TempGroups.PARAMETERS;
        paramCount = 0;
        localCount = 0;
        trueTempCount = 0;
    }

    public List<Temporary> getAllTemps() {
        return Collections.unmodifiableList(temporaries);
    }

    public static void main(String[] args) throws Exception {
        //  Some simple tests using assertions since we don't have access 
        // to JUnit or Gradle on the UVic test server    
        TestUtils.startTestRun("TempFactory");

        smokeTestFactory();
        testReuseFactory();
        testNoParameters();
        testNoLocals();
        testOnlyTrueTemporaries();

        TestUtils.finishTestRun(true);
    }

    private static void smokeTestFactory() throws Exception {
        // Add a few parameter, a few locals, and a few temps
        TempFactory factory = new TempFactory();
        assert factory.paramCount == 0;
        assert factory.localCount == 0;
        assert factory.trueTempCount == 0;

        var intType = IntegerType.INSTANCE;
        var boolType = BooleanType.INSTANCE;
        
        var p1 = factory.acquireParam(intType, "P1");
        assert factory.paramCount == 1;
        assert p1.type() == intType;
        assert p1.localIndex() == 0;
        assert p1.globalIndex() == 0;
        assert p1.sourceId().equals("P1");

        var p2 = factory.acquireParam(boolType, "Param 2");
        assert factory.paramCount == 2;
        assert factory.localCount == 0;
        assert p2.type() == boolType;
        assert p2.localIndex() == 1;
        assert p2.globalIndex() == 1;
        assert p2.sourceId().equals("Param 2");

        var l1 = factory.acquireLocal(boolType, "Local");
        assert factory.paramCount == 2;
        assert factory.localCount == 1;
        assert l1.type() == boolType;
        assert l1.localIndex() == 0;
        assert l1.globalIndex() == 2;
        assert l1.sourceId().equals("Local");

        var t1 = factory.acquireTemp(intType);
        assert factory.paramCount == 2;
        assert factory.localCount == 1;
        assert factory.trueTempCount == 1;
        assert t1.type() == intType;
        assert t1.localIndex() == 0;
        assert t1.globalIndex() == 3;
    }

    private static void testReuseFactory() throws Exception {
        TempFactory factory = new TempFactory();
        
        var p1 = factory.acquireParam(IntegerType.INSTANCE, "p");
        var l1 = factory.acquireLocal(FloatType.INSTANCE, "l");
        var t1 = factory.acquireTemp(StringType.INSTANCE);
        assert factory.paramCount == 1;
        assert factory.localCount == 1;
        assert factory.trueTempCount == 1;
        assert factory.temporaries.size() == 3;
        assert factory.tempGroup == TempGroups.TRUE_TEMPORARIES;

        factory.clear();
        assert factory.paramCount == 0;
        assert factory.localCount == 0;
        assert factory.trueTempCount == 0;
        assert factory.temporaries.size() == 0;
        assert factory.tempGroup == TempGroups.PARAMETERS;
    
        var p2 = factory.acquireParam(FloatType.INSTANCE, "p2");
        var l2 = factory.acquireLocal(CharacterType.INSTANCE, "l2");
        var t2 = factory.acquireTemp(BooleanType.INSTANCE);
    
        assert p1 != p2;
        assert l1 != l2;
        assert t1 != t2;

        assert factory.paramCount == 1;
        assert factory.localCount == 1;
        assert factory.trueTempCount == 1;
        assert factory.temporaries.size() == 3;
        assert factory.tempGroup == TempGroups.TRUE_TEMPORARIES;
    }

    private static void testNoParameters() throws Exception {
        TempFactory factory = new TempFactory();
        factory.acquireLocal(CharacterType.INSTANCE, "l");
        assert factory.paramCount == 0;
        assert factory.localCount == 1;
        assert factory.trueTempCount == 0;
        assert factory.temporaries.size() == 1;
        assert factory.tempGroup == TempGroups.LOCALS;
    }

    private static void testNoLocals() throws Exception {
        TempFactory factory = new TempFactory();
        factory.acquireParam(StringType.INSTANCE, "p");
        factory.acquireTemp(StringType.INSTANCE);
        assert factory.paramCount == 1;
        assert factory.localCount == 0;
        assert factory.trueTempCount == 1;
        assert factory.temporaries.size() == 2;
        assert factory.tempGroup == TempGroups.TRUE_TEMPORARIES;
    }

    private static void testOnlyTrueTemporaries() throws Exception {
        TempFactory factory = new TempFactory();
        factory.acquireTemp(StringType.INSTANCE);
        assert factory.paramCount == 0;
        assert factory.localCount == 0;
        assert factory.trueTempCount == 1;
        assert factory.temporaries.size() == 1;
        assert factory.tempGroup == TempGroups.TRUE_TEMPORARIES;
    }
}
