package pl.jszmidla.flashcards.data.mapper;

import org.junit.jupiter.api.Test;
import pl.jszmidla.flashcards.data.Flashcard;
import pl.jszmidla.flashcards.data.dto.FlashcardRequest;
import pl.jszmidla.flashcards.data.dto.FlashcardResponse;

import static org.junit.jupiter.api.Assertions.*;

class FlashcardMapperTest {

    private FlashcardMapper flashcardMapper = new FlashcardMapper();

    @Test
    void requestToEntity() {
        String front = "front";
        String back = "back";
        FlashcardRequest flashcardRequest = createFlashcardRequest(front, back);
        Flashcard flashcard = flashcardMapper.requestToEntity(flashcardRequest);

        assertEquals(front, flashcard.getFront());
        assertEquals(back, flashcard.getBack());
    }

    @Test
    void entityToResponse() {
        long id = 1;
        String front = "front";
        String back = "back";
        Flashcard flashcard = createFlashcard(id, front, back);
        FlashcardResponse flashcardResponse = flashcardMapper.entityToResponse(flashcard);

        assertEquals(id, flashcardResponse.getId());
        assertEquals(front, flashcardResponse.getFront());
        assertEquals(back, flashcardResponse.getBack());
    }

    private Flashcard createFlashcard(Long id, String front, String back) {
        Flashcard flashcard = new Flashcard();
        flashcard.setId(id);
        flashcard.setFront(front);
        flashcard.setBack(back);
        return flashcard;
    }

    private FlashcardRequest createFlashcardRequest(String front, String back) {
        FlashcardRequest flashcardRequest = new FlashcardRequest();
        flashcardRequest.setFront(front);
        flashcardRequest.setBack(back);
        return flashcardRequest;
    }
}