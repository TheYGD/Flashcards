package pl.jszmidla.flashcards.data.exception.item;

import pl.jszmidla.flashcards.data.exception.item.ItemNotFoundException;

public class FlashcardSetNotFoundException extends ItemNotFoundException {

    static String ITEM_NAME = "Flashcard set";

    public FlashcardSetNotFoundException() {
        super(ITEM_NAME);
    }
}
