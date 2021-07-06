package common;

/**
 * Represents a key-value binding present in an @link{Environment}. Each entry
 * can also be "marked", signalling that it is the first binding in a new, nested
 * scope level in the environment.
 * 
 * @param <K> The Key type used to identify this entry.
 * @param <V> The Value type associated with this entry.
 */
public class EnvironmentEntry<K, V> {
    
    /**
     * Creates a dummy marker node to indicate the beginning of a new scope level.
     * This is used in place of the (possibly) more memory-efficient alternatives of 
     * storing scope-level state in each node for two reasons:
     *  (1) The alternative introduces per-element overhead which may greatly exceed 
     *      the cost of dummy nodes if there are few nested scope levels as we expect
     *      for the unnamed language
     *  (2) It does not let us explicitly model nested scopes where there is no binding
     *      before entering the second scope level. This can be worked-around with
     *      the introduction of counters, but is a much uglier solution than this. 
     */
    public static <K, V> EnvironmentEntry<K, V> getMarker() {
        return new EnvironmentEntry<K, V>();
    }

    // Create a new marker entry.
    private EnvironmentEntry() {
        key = null;
        value = null;
    }

    /**
     * Create an EnvironmentEntry with the given key and value.
     * 
     * @param key The key for the entry. Must be non-null.
     * @param value The value for the entry. Must be non-null.
     */
    public EnvironmentEntry(K key, V value) {
        assert key != null;
        assert value != null;

        this.key = key;
        this.value = value;
    }

    private final K key;
    private final V value;
    
    /**
     * Answers whether this entry is a dummy node which marks the beginning 
     * of a new scope level. If true, the other querying operations are 
     * unsupported.
     */
    public boolean isMarker() {
        return key == null;
    }

    /**
     * Answers whether this entry had the provided key.
     * Only supported for non-marker nodes.
     */ 
    public boolean matches(K otherKey) {
        if (isMarker()) {
            throw new UnsupportedOperationException("Cannot match against marker node");
        }

        return this.key.equals(otherKey); 
    }

    /**
     * Returns the value stored in this entry.
     * Only supported for non-marker nodes.
     */
    public V getValue() {
        if (isMarker()) {
            throw new UnsupportedOperationException("Cannot query value of marker node");
        }
        
        return value;
    }

}
