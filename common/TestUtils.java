package common;

/**
 * Utilities for the hacky, assertion-based testing we're 
 * doing because the test server doesn't have Gradle or 
 * Junit installed.
 */
public class TestUtils {
    
    private static boolean checkAssertsEnabled() {
        boolean assertsEnabled = false;
        assert assertsEnabled = true; // Intentional side effect
        return assertsEnabled;
    }

    private static void enforceAssertionsEnabled() {
        if (!checkAssertsEnabled()) {
            System.err.println("ERROR: Assertions are disabled.");
            System.exit(1);
        }
    }

    public static void startTestRun(String name) {
        System.out.println("\nRunning smoke tests for " + name);
        enforceAssertionsEnabled();
    }

    public static void finishTestRun(boolean success) {
        if (success) {
            System.out.println("Tests passed.\n");
        }
        else {
            System.out.println("Tests FAILED.\n");
        }
    }

}
