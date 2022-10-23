package pl.jszmidla.flashcards.data.mapper;

import org.junit.jupiter.api.Test;
import pl.jszmidla.flashcards.data.FlashcardSet;
import pl.jszmidla.flashcards.data.dto.flashcard.FlashcardRequest;
import pl.jszmidla.flashcards.data.dto.flashcard.FlashcardSetRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FlashcardSetMapperTest {
    
    FlashcardSetMapper flashcardSetMapper = new FlashcardSetMapper(new FlashcardMapper());

    @Test
    void requestToEntity() {
        String setName = "name";
        String setDescription = "desc";
        FlashcardRequest flashcard1 = createFlashcardRequest("fl1", "bc1");
        FlashcardRequest flashcard2 = createFlashcardRequest("fl2", "bc2");
        FlashcardSetRequest flashcardSetRequest = createFlashcardSetRequest(setName,
                setDescription, List.of(flashcard1, flashcard2));

        FlashcardSet flashcardSet = flashcardSetMapper.requestToEntity(flashcardSetRequest);

        assertEquals(setName, flashcardSet.getName());
        assertEquals(setDescription, flashcardSet.getDescription());

        assertEquals(flashcard1.getFront(), flashcardSet.getFlashcards().get(0).getFront());
        assertEquals(flashcard1.getBack(), flashcardSet.getFlashcards().get(0).getBack());
        assertEquals(flashcard2.getFront(), flashcardSet.getFlashcards().get(1).getFront());
        assertEquals(flashcard2.getBack(), flashcardSet.getFlashcards().get(1).getBack());
    }

    FlashcardRequest createFlashcardRequest(String front, String back) {
        FlashcardRequest flashcardRequest = new FlashcardRequest();
        flashcardRequest.setFront(front);
        flashcardRequest.setBack(back);
        return flashcardRequest;
    }

    FlashcardSetRequest createFlashcardSetRequest(String name, String description,
                                                  List<FlashcardRequest> flashcardRequestList) {
        FlashcardSetRequest flashcardSetRequest = new FlashcardSetRequest();
        flashcardSetRequest.setName(name);
        flashcardSetRequest.setDescription(description);
        flashcardSetRequest.setFlashcardList(flashcardRequestList);
        return flashcardSetRequest;
    }
}