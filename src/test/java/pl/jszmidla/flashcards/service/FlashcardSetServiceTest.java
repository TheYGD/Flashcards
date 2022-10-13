package pl.jszmidla.flashcards.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.jszmidla.flashcards.data.Flashcard;
import pl.jszmidla.flashcards.data.FlashcardSet;
import pl.jszmidla.flashcards.data.User;
import pl.jszmidla.flashcards.data.dto.FlashcardRequest;
import pl.jszmidla.flashcards.data.dto.FlashcardSetRequest;
import pl.jszmidla.flashcards.data.dto.FlashcardSetResponse;
import pl.jszmidla.flashcards.data.exception.ForbiddenException;
import pl.jszmidla.flashcards.data.mapper.FlashcardMapper;
import pl.jszmidla.flashcards.data.mapper.FlashcardSetMapper;
import pl.jszmidla.flashcards.repository.FlashcardRepository;
import pl.jszmidla.flashcards.repository.FlashcardSetRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FlashcardSetServiceTest {

    @Mock
    FlashcardRepository flashcardRepository;
    @Mock
    FlashcardSetRepository flashcardSetRepository;
    FlashcardSetMapper flashcardSetMapper = new FlashcardSetMapper(new FlashcardMapper());
    @InjectMocks
    FlashcardSetService flashcardSetService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        flashcardSetService = new FlashcardSetService(flashcardSetRepository, flashcardRepository, flashcardSetMapper);
    }


    @Test
    void findById() {
        when( flashcardSetRepository.findById(any()) ).thenReturn( Optional.of(new FlashcardSet()) );
        flashcardSetService.findById(1L);
    }

    @Test
    void createSet() {
        FlashcardSetRequest flashcardSetRequest = createFlashcardSetDto();
        FlashcardSet flashcardSet = createFlashcardSet();
        User author = flashcardSet.getAuthor();

        Long setId = flashcardSetService.createSet(flashcardSetRequest, author);

        assertNull(setId);
    }

    @Test
    void deleteSetSuccess() {
        User user = create_user(1);
        FlashcardSet flashcardSet = Mockito.spy(FlashcardSet.class);
        when( flashcardSet.getAuthor() ).thenReturn( user );
        when( flashcardSetRepository.findById(any()) ).thenReturn( Optional.of(flashcardSet) );

        flashcardSetService.deleteSet(flashcardSet.getId(), user);
    }

    @Test
    void deleteSetFail() {
        User user = create_user(1);
        User otherUser = create_user(2);
        FlashcardSet flashcardSet = Mockito.spy(FlashcardSet.class);
        when( flashcardSet.getAuthor() ).thenReturn( otherUser );
        when( flashcardSetRepository.findById(any()) ).thenReturn( Optional.of(flashcardSet) );

        assertThrows( ForbiddenException.class, () -> flashcardSetService.deleteSet(flashcardSet.getId(), user) );
    }

    @Test
    void findSetsByQuery() {
        String query = "someQuery";
        FlashcardSet flashcardSet = createFlashcardSet();
        when( flashcardSetRepository.findAllByNameContaining(any()) ).thenReturn( List.of(flashcardSet) );

        flashcardSetService.findSetsByQuery(query);
    }


    private User create_user(long id) {
        User user = new User();
        user.setId(id);
        return user;
    }

    private FlashcardSetRequest createFlashcardSetDto() {
        FlashcardSetRequest flashcardSetRequest = new FlashcardSetRequest();
        flashcardSetRequest.setName("name");
        flashcardSetRequest.setDescription("desc");

        FlashcardRequest flashcardRequest1 = create_flashcard_dto("front1", "back1");
        FlashcardRequest flashcardRequest2 = create_flashcard_dto("front2", "back2");
        FlashcardRequest flashcardRequest3 = create_flashcard_dto("front3", "back3");

        flashcardSetRequest.setFlashcardList( List.of(flashcardRequest1, flashcardRequest2, flashcardRequest3) );
        return flashcardSetRequest;
    }

    private FlashcardRequest create_flashcard_dto(String front, String back) {
        FlashcardRequest flashcardRequest = new FlashcardRequest();
        flashcardRequest.setFront(front);
        flashcardRequest.setBack(back);
        return flashcardRequest;
    }

    @Test
    void findResponseById() {
        FlashcardSet flashcardSet = createFlashcardSet();
        when( flashcardSetRepository.findById(any()) ).thenReturn(Optional.of(flashcardSet));

        FlashcardSetResponse flashcardSetResponse = flashcardSetService.findResponseById(any());

        assertEquals(flashcardSet.getId(), flashcardSetResponse.getId());
        assertEquals(flashcardSet.getName(), flashcardSetResponse.getName());
        assertEquals(flashcardSet.getDescription(), flashcardSetResponse.getDescription());
        assertEquals(flashcardSet.getAuthor().getId(), flashcardSetResponse.getAuthorId());
        assertEquals(flashcardSet.getAuthor().getUsername(), flashcardSetResponse.getAuthorName());
    }

    private FlashcardSet createFlashcardSet() {
        User author = create_user(1);
        author.setUsername("arturitto");

        FlashcardSet flashcardSet = new FlashcardSet();
        flashcardSet.setName("name");
        flashcardSet.setDescription("desc");
        flashcardSet.setAuthor(author);

        return flashcardSet;
    }
}