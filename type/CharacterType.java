package type;

public class CharacterType extends SimpleType {

    public static final CharacterType INSTANCE = new CharacterType();

    private CharacterType() {
    }

    @Override
    public String toString() {
        return "char";
    }

}
