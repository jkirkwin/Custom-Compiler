package common;

/**
 * A factory which produces labels with increasing indices.
 * Can be cleared to restart at 0. 
 */
public class LabelFactory {

    private int index;

    /**
     * Creates a new LabelFactory where the first label will
     * have index 0.
     */
    public LabelFactory() {
        this(0);
    }

    /**
     * Creates a new LabelFactory where the first label will
     * have index start.
     */
    public LabelFactory(int start) {
        assert start >= 0;
        index = start;
    }

    public Label getLabel(){
        return new Label(index++);
    }

    public void clear() {
        index = 0;
    }

    public void skip(int n) {
        assert n > 0;
        index += n;
    }

}
