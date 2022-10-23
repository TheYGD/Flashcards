package pl.jszmidla.flashcards.data.mapper;

import org.springframework.stereotype.Component;
import pl.jszmidla.flashcards.data.Flashcard;
import pl.jszmidla.flashcards.data.dto.flashcard.FlashcardRequest;
import pl.jszmidla.flashcards.data.dto.flashcard.FlashcardResponse;

@Component
public class FlashcardMapper {

    public Flashcard requestToEntity(FlashcardRequest flashcardRequest) {
        Flashcard flashcard = new Flashcard();
        flashcard.setFront(flashcardRequest.getFront());
        flashcard.setBack(flashcardRequest.getBack());
        return flashcard;
    }

    public FlashcardResponse entityToResponse(Flashcard flashcard) {
        FlashcardResponse flashcardResponse = new FlashcardResponse();
        flashcardResponse.setId(flashcard.getId());
        flashcardResponse.setFront(flashcard.getFront());
        flashcardResponse.setBack(flashcard.getBack());
        return flashcardResponse;
    }
}
