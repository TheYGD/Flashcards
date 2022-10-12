package pl.jszmidla.flashcards.data.exception;

public class FlashcardSetNotFoundException extends ItemNotFoundException {

    static String ITEM_NAME = "Flashcard set";

    public FlashcardSetNotFoundException() {
        super(ITEM_NAME);
    }
}
