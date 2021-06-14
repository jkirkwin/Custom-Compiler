package type;

/**
 * A marker interface used to differentiate between simple
 * and compound (array) types.
 */
public abstract class SimpleType extends Type {

    @Override
    public boolean equals(Object other) {
        // Since simple types are all implemented using singletons, we
        // can use identity comparison here.
        return this == other;
    }

}
