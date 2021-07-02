package ir;

/**
 * A factory which produces labels with increasing indices.
 * Can be cleared to restart at 0.
 */
public class LabelFactory {

    private static int index;

    public LabelFactory() {
        index = 0;
    }

    public Label getLabel(){
        return new Label(index++);
    }

    public void clear() {
        index = 0;
    }

}
