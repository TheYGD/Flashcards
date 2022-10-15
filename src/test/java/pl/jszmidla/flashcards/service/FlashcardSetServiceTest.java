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
import pl.jszmidla.flashcards.data.dto.*;
import pl.jszmidla.flashcards.data.exception.ForbiddenException;
import pl.jszmidla.flashcards.data.mapper.FlashcardMapper;
import pl.jszmidla.flashcards.data.mapper.FlashcardSetMapper;
import pl.jszmidla.flashcards.repository.FlashcardRepository;
import pl.jszmidla.flashcards.repository.FlashcardSetRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FlashcardSetServiceTest {

    @Mock
    FlashcardRepository flashcardRepository;
    @Mock
    FlashcardSetRepository flashcardSetRepository;
    FlashcardMapper flashcardMapper = new FlashcardMapper();
    FlashcardSetMapper flashcardSetMapper = new FlashcardSetMapper(flashcardMapper);
    @Mock
    UsersActiveSetService usersActiveSetService;
    @Mock
    UsersRecentSetService usersRecentSetService;
    @InjectMocks
    FlashcardSetService flashcardSetService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        flashcardSetService = new FlashcardSetService(flashcardSetRepository, flashcardRepository, flashcardSetMapper,
                flashcardMapper, usersActiveSetService, usersRecentSetService);
    }


    @Test
    void findById() {
        when( flashcardSetRepository.findById(any()) ).thenReturn( Optional.of(new FlashcardSet()) );

        flashcardSetService.findById(1L);
    }

    @Test
    void createSet() {
        FlashcardSetRequest flashcardSetRequest = createFlashcardSetDto();
        FlashcardSet flashcardSet = createFlashcardSet(List.of());
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
        FlashcardSet flashcardSet = createFlashcardSet(List.of());
        when( flashcardSetRepository.findAllByNameContainingIgnoreCase(any()) ).thenReturn( List.of(flashcardSet) );

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
    void showSetToUser() {
        FlashcardSet flashcardSet = createFlashcardSet(List.of());
        when( flashcardSetRepository.findById(any()) ).thenReturn(Optional.of(flashcardSet));

        FlashcardSetResponse flashcardSetResponse = flashcardSetService.showSetToUser(1L, new User());

        assertEquals(flashcardSet.getId(), flashcardSetResponse.getId());
        assertEquals(flashcardSet.getName(), flashcardSetResponse.getName());
        assertEquals(flashcardSet.getDescription(), flashcardSetResponse.getDescription());
        assertEquals(flashcardSet.getAuthor().getId(), flashcardSetResponse.getAuthorId());
        assertEquals(flashcardSet.getAuthor().getUsername(), flashcardSetResponse.getAuthorName());
    }

    private FlashcardSet createFlashcardSet(List<Flashcard> flashcardList) {
        User author = create_user(1);
        author.setUsername("arturitto");

        FlashcardSet flashcardSet = new FlashcardSet();
        flashcardSet.setName("name");
        flashcardSet.setDescription("desc");
        flashcardSet.setAuthor(author);
        flashcardSet.setFlashcards(flashcardList);

        return flashcardSet;
    }

    @Test
    void getSplittedFlashcardsFromSet() {
        Flashcard flashcard1 = createFlashcard(1, "fl1", "bc1"); // remembered
        Flashcard flashcard2 = createFlashcard(2, "fl2", "bc2"); // unremembered
        Set<Long> rememberedSet = Set.of(1L);
        FlashcardSet flashcardSet = createFlashcardSet( List.of(flashcard1, flashcard2) );
        when( flashcardSetRepository.findById(any()) ).thenReturn(Optional.of(flashcardSet));
        when( usersActiveSetService.getRememberedFlashcardsIds(any(), any()) ).thenReturn(rememberedSet);

        RememberedAndUnrememberedFlashcardsSplitted flashcardResponseSplitted =
                flashcardSetService.getSplittedFlashcardsFromSet(1L, new User());


        assertEquals(1, flashcardResponseSplitted.getRememberedFlashcardList().size());
        assertEquals(1, flashcardResponseSplitted.getUnrememberedFlashcardList().size());
        // remembered
        FlashcardResponse rememberedFlashcard = flashcardResponseSplitted.getRememberedFlashcardList().get(0);
        assertEquals( flashcardSet.getFlashcards().get(0).getId(), rememberedFlashcard.getId() );
        assertEquals( flashcardSet.getFlashcards().get(0).getFront(), rememberedFlashcard.getFront() );
        assertEquals( flashcardSet.getFlashcards().get(0).getBack(), rememberedFlashcard.getBack() );
        // unremembered
        FlashcardResponse unrememberedFlashcard = flashcardResponseSplitted.getUnrememberedFlashcardList().get(0);
        assertEquals( flashcardSet.getFlashcards().get(1).getId(), unrememberedFlashcard.getId() );
        assertEquals( flashcardSet.getFlashcards().get(1).getFront(), unrememberedFlashcard.getFront() );
        assertEquals( flashcardSet.getFlashcards().get(1).getBack(), unrememberedFlashcard.getBack() );
    }

    @Test
    void markFlashcardFromSetAsRemembered() {
        long id = 2;
        when( flashcardSetRepository.findById(any()) ).thenReturn( Optional.of(new FlashcardSet()) );

        flashcardSetService.markFlashcardFromSetAsRemembered(id, id, new User());
    }

    @Test
    void markSetAsCompleted() {
        long id = 2;
        when( flashcardSetRepository.findById(any()) ).thenReturn( Optional.of(new FlashcardSet()) );

        flashcardSetService.markSetAsCompleted(id, new User());
    }

    private Flashcard createFlashcard(long id, String front, String back) {
        Flashcard flashcard = new Flashcard();
        flashcard.setId(id);
        flashcard.setFront(front);
        flashcard.setBack(back);
        return flashcard;
    }

    @Test
    void reloadSetSooner() {
        long id = 2;
        LocalDateTime date = LocalDateTime.now();
        when( flashcardSetRepository.findById(any()) ).thenReturn( Optional.of(new FlashcardSet()) );
        when( usersActiveSetService.getSetExpirationDate(any(), any()) ).thenReturn( date );

        LocalDateTime actualDate = flashcardSetService.getSetExpirationDate(id, new User());

        assertEquals(date, actualDate);
    }
}