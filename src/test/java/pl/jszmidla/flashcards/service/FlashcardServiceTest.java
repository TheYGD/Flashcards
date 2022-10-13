package pl.jszmidla.flashcards.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.jszmidla.flashcards.data.Flashcard;
import pl.jszmidla.flashcards.data.FlashcardSet;
import pl.jszmidla.flashcards.data.User;
import pl.jszmidla.flashcards.data.dto.FlashcardResponse;
import pl.jszmidla.flashcards.data.mapper.FlashcardMapper;
import pl.jszmidla.flashcards.repository.FlashcardRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FlashcardServiceTest {

    @Mock
    FlashcardSetService flashcardSetService;
    FlashcardMapper flashcardMapper = new FlashcardMapper();
    FlashcardService flashcardService;

    @BeforeEach
    void setUp() {
        flashcardService = new FlashcardService(flashcardSetService, flashcardMapper);
    }

    @Test
    void getFlashcardsFromSet() {
        Flashcard flashcard1 = createFlashcard(1, "fl1", "bc1");
        Flashcard flashcard2 = createFlashcard(2, "fl2", "bc2");
        FlashcardSet flashcardSet = createFlashcardSet( List.of(flashcard1, flashcard2) );
        when( flashcardSetService.findById(any()) ).thenReturn(flashcardSet);

        List<FlashcardResponse> flashcardResponseList = flashcardService.getFlashcardsFromSet(any());

        for (int i = 0; i < flashcardSet.getFlashcards().size(); i++)  {
            assertEquals( flashcardSet.getFlashcards().get(i).getId(), flashcardResponseList.get(i).getId() );
            assertEquals( flashcardSet.getFlashcards().get(i).getFront(), flashcardResponseList.get(i).getFront() );
            assertEquals( flashcardSet.getFlashcards().get(i).getBack(), flashcardResponseList.get(i).getBack() );
        }
    }

    @Test
    void markFlashcardAsRemembered() {
        throw new RuntimeException();
    }

    private Flashcard createFlashcard(long id, String front, String back) {
        Flashcard flashcard = new Flashcard();
        flashcard.setId(id);
        flashcard.setFront(front);
        flashcard.setBack(back);
        return flashcard;
    }

    private FlashcardSet createFlashcardSet(List<Flashcard> flashcardList) {
        FlashcardSet flashcardSet = new FlashcardSet();
        flashcardSet.setFlashcards(flashcardList);
        return flashcardSet;
    }
}