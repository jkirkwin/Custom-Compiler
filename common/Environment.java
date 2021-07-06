package common;

import java.util.Deque;
import java.util.LinkedList;
import java.util.NoSuchElementException;

/**
 * Represents an "environment" or "Symbol Table" used to track
 * the currently available keys and their bound values at a given
 * point during the type checking/semantic analysis and IR generation.
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
    private final Deque<EnvironmentEntry<K, V>> bindingStack;

    private EnvironmentEntry<K, V> getMarker() {
        return EnvironmentEntry.<K, V>getMarker();
    }

    /** 
     * Create a new, empty environment with an empty global scope.
     */
    public Environment() {
        bindingStack = new LinkedList<EnvironmentEntry<K, V>>();
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
        bindingStack.addFirst(getMarker());
    }


    /**
     * Remove all bindings from the deepest active scope.
     */
    public void exitScope() {
        if (bindingStack.isEmpty()) {
            throw new IllegalStateException("No scope to exit");
        }

        // Pop all bindings from the current scope.
        while (!bindingStack.isEmpty() && !bindingStack.getFirst().isMarker()) {
            bindingStack.removeFirst();
        }

        // No scope marker preceeded the bindings.
        if (bindingStack.isEmpty()) {
            throw new IllegalStateException("Missing scope entry marker");
        }

        // Pop the scope marker.
        assert bindingStack.getFirst().isMarker();
        bindingStack.removeFirst();
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

    public static void main(String[] args) {
        // Unfortunately the UVic linux servers don't have JUnit installed so we're going
        // to test just using assertions for now.

        TestUtils.startTestRun("Environment");

        Environment<String, Integer> env = new Environment<String, Integer>();

        // Check initial state
        assert !env.exists("Foo");
        assert !env.existsInCurrentScope("Foo");
        try {
            env.lookup("Foo");
            assert false;
        }
        catch (Exception e) {
            // Nothing to do here.
        }

        // Insert a single binding into the current scope.
        env.bind("Foo", 1);
        assert env.exists("Foo");
        assert env.existsInCurrentScope("Foo");
        assert env.lookup("Foo") == 1;

        // Insert a second binding into the top-level scope
        env.bind("Bar", 2);
        assert env.exists("Bar");
        assert env.existsInCurrentScope("Bar");
        assert env.lookup("Bar") == 2;

        // Foo should still be accessible
        assert env.exists("Foo");
        assert env.existsInCurrentScope("Foo");
        assert env.lookup("Foo") == 1;

        // Enter a new scope level. Both foo and bar should still
        // be accessible.
        env.enterScope();

        assert env.exists("Foo");
        assert ! env.existsInCurrentScope("Foo");
        assert env.lookup("Foo") == 1;

        assert env.exists("Bar");
        assert ! env.existsInCurrentScope("Bar");
        assert env.lookup("Bar") == 2;

        // Add a new binding that shadows Foo
        env.bind("Foo", 10);
        assert env.exists("Foo");
        assert env.existsInCurrentScope("Foo");
        assert env.lookup("Foo") == 10;

        // Add another new binding in the nested scope
        env.bind("Baz", 42);
        assert env.exists("Baz");
        assert env.existsInCurrentScope("Baz");
        assert env.lookup("Baz") == 42;

        // Exit the scope and confirm the nested items have been removed
        env.exitScope();
        assert env.exists("Foo");
        assert env.exists("Bar");
        assert ! env.exists("Baz");
        
        assert env.existsInCurrentScope("Foo");
        assert env.existsInCurrentScope("Bar");

        assert env.lookup("Foo") == 1; // Original value should be restored
        assert env.lookup("Bar") == 2;

        TestUtils.finishTestRun(true);
    }
}