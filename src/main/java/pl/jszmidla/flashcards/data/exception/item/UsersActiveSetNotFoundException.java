package pl.jszmidla.flashcards.data.exception.item;

public class UsersActiveSetNotFoundException extends ItemNotFoundException {

    static String ITEM_NAME = "User's recent sets";

    public UsersActiveSetNotFoundException() {
        super(ITEM_NAME);
    }
}