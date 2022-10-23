package pl.jszmidla.flashcards.data.exception.item;

public class UsersActiveSetNotFoundException extends ItemNotFoundException {

    static String ITEM_NAME = "User's active sets";

    public UsersActiveSetNotFoundException() {
        super(ITEM_NAME);
    }
}