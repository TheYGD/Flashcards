package pl.jszmidla.flashcards.data.exception;

public class FlashcardSetNotFoundException extends ItemNotFoundException {

    static String itemName = "Flashcard set";

    public FlashcardSetNotFoundException() {
        super(itemName);
    }
}
