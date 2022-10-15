package pl.jszmidla.flashcards.data.exception.item;

public class UsersRecentSetNotFoundException extends ItemNotFoundException {

    static String ITEM_NAME = "User's recent sets";

    public UsersRecentSetNotFoundException() {
        super(ITEM_NAME);
    }
}
