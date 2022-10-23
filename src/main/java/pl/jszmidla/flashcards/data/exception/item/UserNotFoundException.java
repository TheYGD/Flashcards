package pl.jszmidla.flashcards.data.exception.item;

public class UserNotFoundException extends ItemNotFoundException {

    private static String ITEM_NAME = "User";

    public UserNotFoundException() {
        super(ITEM_NAME);
    }
}
