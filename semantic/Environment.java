package semantic;

import java.util.Stack;
import java.util.NoSuchElementException;

/**
 * Represents an "environment" or "Symbol Table" used to track
 * the currently available keys and their bound values at a given
 * point during the type checking and semantic analysis processes.
 * 
 * @param <K> The Key type used to identify envrionment entries.
 *            This type may be user-defined, but must have the .equals
 *            method implemented appropriately to allow duplicate keys
 *            to be used.
 * 
 * @param <V> The Value type associated with each key.
 */
public class Environment<K, V> {

    // Holds all key-value bindings which have been defined.
    private final Stack<EnvironmentEntry<K, V>> bindingStack;

    private EnvironmentEntry<K, V> getMarker() {
        return EnvironmentEntry.<K, V>getMarker();
    }

    /** 
     * Create a new, empty environment.
     */
    public Environment() {
        bindingStack = new Stack<EnvironmentEntry<K, V>>();
        enterScope();
    }

    /**
     * Answers whether a binding with the provided key exists in the
     * environment
     * 
     * @param key The key to search for. Must be non-null.
     */
    public boolean exists(K key) {
        assert key != null;

        for (var binding : bindingStack) {
            if (!binding.isMarker() && binding.matches(key)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Answers whether a binding with the provided key exists in the
     * deepest active scope of the environment.
     * 
     * @param key The key to search for. Must be non-null.
     */
    public boolean existsInCurrentScope(K key) {
        assert key != null;

        for (var binding : bindingStack) {
            if (binding.isMarker()) {
                break;
            }
            else if (binding.matches(key)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Enter a new scope level.
     */
    public void enterScope() {
        bindingStack.push(getMarker());
    }


    /**
     * Remove all bindings from the deepest active scope.
     */
    public void exitScope() {
        if (bindingStack.empty()) {
            throw new IllegalStateException("No scope to exit");
        }

        // Pop all bindings from the current scope.
        while (!bindingStack.empty() && !bindingStack.peek().isMarker()) {
            bindingStack.pop();
        }

        // No scope marker preceeded the bindings.
        if (bindingStack.empty()) {
            throw new IllegalStateException("Missing scope entry marker");
        }

        // Pop the scope marker.
        bindingStack.pop();
    }

    /**
     * Returns the value of the matching binding found at the deepest active 
     * scope level.
     * @param key The key to search for. Must be non-null.
     */
    public V lookup(K key) {
        assert key != null;

        for (var binding : bindingStack) {
            if (!binding.isMarker() && binding.matches(key)) {
                return binding.getValue();
            }
        }

        throw new NoSuchElementException("No binding exists with key " + key.toString());
    }

    /**
     * Adds a key-value binding in the current scope.
     * @param key The key of the binding. Must be non-null.
     * @param value The value of the binding. Must be non-null.
     */
    public void bind(K key, V value) {
        assert key != null;
        assert value != null;

        bindingStack.push(new EnvironmentEntry<K, V> (key, value));
    }

}