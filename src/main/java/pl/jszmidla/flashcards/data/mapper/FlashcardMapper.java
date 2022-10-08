package pl.jszmidla.flashcards.data.mapper;

import org.springframework.stereotype.Component;
import pl.jszmidla.flashcards.data.Flashcard;
import pl.jszmidla.flashcards.data.dto.FlashcardDto;

@Component
public class FlashcardMapper {

    public Flashcard dto_to_entity(FlashcardDto flashcardDto) {
        Flashcard flashcard = new Flashcard();
        flashcard.setFront(flashcardDto.getFront());
        flashcard.setBack(flashcardDto.getBack());
        return flashcard;
    }

    public FlashcardDto entity_to_dto(Flashcard flashcard) {
        FlashcardDto flashcardDto = new FlashcardDto();
        flashcardDto.setFront(flashcard.getFront());
        flashcardDto.setBack(flashcard.getBack());
        return flashcardDto;
    }
}
