package pl.jszmidla.flashcards.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.jszmidla.flashcards.data.FlashcardSet;
import pl.jszmidla.flashcards.data.User;
import pl.jszmidla.flashcards.data.dto.flashcard.ActiveFlashcardSetResponse;
import pl.jszmidla.flashcards.data.mapper.ActiveSetMapper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HomeServiceTest {

    @Mock
    UsersRecentSetService usersRecentSetService;
    @Mock
    UsersActiveSetService usersActiveSetService;
    ActiveSetMapper flashcardSetMapper = new ActiveSetMapper();
    HomeService homeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        homeService = new HomeService(usersRecentSetService, usersActiveSetService, flashcardSetMapper);
    }


    @Test
    void getUsersRecentSets() {
        List<FlashcardSet> expectedFlashcardSetList = createFlashcardSetList();
        when( usersRecentSetService.getUsersRecentSetListOrNull(any()) ).thenReturn(expectedFlashcardSetList);

        List<ActiveFlashcardSetResponse> actualFlashcardSetList = homeService.getUsersRecentSets(any());

        for (int i = 0; i < actualFlashcardSetList.size(); i++) {
            assertEquals( expectedFlashcardSetList.get(i).getName(), actualFlashcardSetList.get(i).getName() );
        }
    }

    private List<FlashcardSet> createFlashcardSetList() {
        FlashcardSet flashcardSet1 = createFlashcardSet(1);
        FlashcardSet flashcardSet2 = createFlashcardSet(2);

        List<FlashcardSet> flashcardSetList = List.of( flashcardSet1, flashcardSet2 );
        return flashcardSetList;
    }

    private FlashcardSet createFlashcardSet(long id) {
        FlashcardSet flashcardSet = new FlashcardSet();
        flashcardSet.setAuthor(new User());
        flashcardSet.setFlashcards(List.of());
        return flashcardSet;
    }
}