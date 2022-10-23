package pl.jszmidla.flashcards.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.jszmidla.flashcards.data.Flashcard;
import pl.jszmidla.flashcards.data.FlashcardSet;
import pl.jszmidla.flashcards.data.User;
import pl.jszmidla.flashcards.data.UsersActiveSet;
import pl.jszmidla.flashcards.data.dto.flashcard.FlashcardResponse;
import pl.jszmidla.flashcards.data.dto.flashcard.RememberedAndUnrememberedFlashcardsSplitted;
import pl.jszmidla.flashcards.data.exception.item.UsersActiveSetNotFoundException;
import pl.jszmidla.flashcards.data.mapper.ActiveSetMapper;
import pl.jszmidla.flashcards.data.mapper.FlashcardMapper;
import pl.jszmidla.flashcards.repository.UsersActiveSetRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UsersActiveSetServiceTest {

    @Mock
    UsersActiveSetRepository usersActiveSetRepository;
    @Mock
    FlashcardSetService flashcardSetService;
    @Mock
    UsersRecentSetService usersRecentSetService;
    FlashcardMapper flashcardMapper = new FlashcardMapper();
    ActiveSetMapper activeSetMapper = new ActiveSetMapper();
    UsersActiveSetService usersActiveSetService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        usersActiveSetService = new UsersActiveSetService(usersActiveSetRepository, flashcardSetService,
                usersRecentSetService, flashcardMapper, activeSetMapper);
    }


    @Test
    void getUsersActiveSet() {
        String csv = "1,2";
        UsersActiveSet usersActiveSet = createUsersActiveSet(csv, new FlashcardSet());
        when( usersActiveSetRepository.findByUserAndFlashcardSet(any(), any()) ).thenReturn(Optional.of(usersActiveSet));

        UsersActiveSet actualUsersActiveSet = usersActiveSetService.getUsersActiveSet(new User(), new FlashcardSet());
        assertEquals( usersActiveSet, actualUsersActiveSet );
    }

    @Test
    void getOrCreateUsersActiveSetGet() {
        String csv = "1,2";
        UsersActiveSet usersActiveSet = createUsersActiveSet(csv, new FlashcardSet());
        when( usersActiveSetRepository.findByUserAndFlashcardSet(any(), any()) ).thenReturn(Optional.of(usersActiveSet));

        UsersActiveSet actualUsersActiveSet = usersActiveSetService.getOrCreateUsersActiveSet(new FlashcardSet(), new User());
        assertEquals( usersActiveSet, actualUsersActiveSet );
    }

    @Test
    void getOrCreateUsersActiveSetCreate() {
        User user = new User();
        user.setId(1L);
        when( usersActiveSetRepository.findByUserAndFlashcardSet(any(), any()) ).thenReturn(Optional.empty());

        UsersActiveSet actualUsersActiveSet = usersActiveSetService.getOrCreateUsersActiveSet(new FlashcardSet(), user);

        assertThrows( UsersActiveSetNotFoundException.class, () ->
                usersActiveSetService.getUsersActiveSet(user, new FlashcardSet()) );
        assertEquals( user.getId(), actualUsersActiveSet.getUser().getId() );
    }

    @Test
    void getRememberedFlashcardsIds() {
        String id1 = "1";
        String id2 = "2";
        String givenCsv = id1 + "," + id2;
        UsersActiveSet usersActiveSet = createUsersActiveSet(givenCsv, new FlashcardSet());
        when( usersActiveSetRepository.findByUserAndFlashcardSet(any(), any()) ).thenReturn(Optional.of(usersActiveSet));

        Set<Long> valuesFromCsv = usersActiveSetService.getRememberedFlashcardsIds(any(), any());

        assertTrue( valuesFromCsv.contains(Long.valueOf(id1)) );
        assertTrue( valuesFromCsv.contains(Long.valueOf(id2)) );
    }

    @Test
    void markFlashcardAsRemembered() {
        long id = 2L;
        FlashcardSet flashcardSet = createFlashcardSet(List.of(new Flashcard(), new Flashcard()));
        UsersActiveSet usersActiveSet = spy( UsersActiveSet.class );
        usersActiveSet.setFlashcardSet(flashcardSet);
        when( usersActiveSetRepository.findByUserAndFlashcardSet(any(), any()) ).thenReturn(Optional.of(usersActiveSet));

        usersActiveSetService.markFlashcardAsRemembered(id, id, new User());

        verify( usersActiveSet ).addFlashcardToRemembered(id);
        verify( usersActiveSetRepository ).save(usersActiveSet);
    }


    @Test
    void reloadSetSooner() {
        UsersActiveSet usersActiveSet = spy( UsersActiveSet.class );
        when( usersActiveSetRepository.findByUserAndFlashcardSet(any(), any()) ).thenReturn(Optional.of(usersActiveSet));
        when( flashcardSetService.findById(any()) ).thenReturn(new FlashcardSet());

        usersActiveSetService.reloadSetSooner(1L, new User());

        verify( usersActiveSet ).clearRememberedFlashcards();
        verify( usersActiveSetRepository ).save(usersActiveSet);
    }

    UsersActiveSet createUsersActiveSet(String csv, FlashcardSet flashcardSet) {
        UsersActiveSet usersActiveSet = new UsersActiveSet();
        usersActiveSet.setRememberedFlashcardsCSV(csv);
        usersActiveSet.setReloadDate(LocalDateTime.now().plusDays(1));
        usersActiveSet.setFlashcardSet(flashcardSet);
        return usersActiveSet;
    }

    @Test
    void getSetExpirationDate() {
        LocalDateTime date = LocalDateTime.now();
        UsersActiveSet usersActiveSet = new UsersActiveSet();
        usersActiveSet.setReloadDate(date);
        when( flashcardSetService.findById( any()) ).thenReturn( new FlashcardSet() );
        when( usersActiveSetRepository.findByUserAndFlashcardSet(any(), any()) ).thenReturn(Optional.of(usersActiveSet));

        LocalDateTime actualDate = usersActiveSetService.getSetReloadDate(anyLong(), new User());

        assertEquals(date, actualDate);
    }

    @Test
    void showSplittedFlashcardsFromSet() {
        Flashcard flashcard1 = createFlashcard(1, "fl1", "bc1"); // remembered
        Flashcard flashcard2 = createFlashcard(2, "fl2", "bc2"); // unremembered
        String rememberedCSV = "1,";
        FlashcardSet flashcardSet = createFlashcardSet( List.of(flashcard1, flashcard2) );
        UsersActiveSet usersActiveSet = createUsersActiveSet(rememberedCSV, flashcardSet);
        when( flashcardSetService.findById(any()) ).thenReturn(flashcardSet);
        when( usersActiveSetRepository.findByUserAndFlashcardSet(any(), any()) ).thenReturn(Optional.of(usersActiveSet));

        RememberedAndUnrememberedFlashcardsSplitted flashcardResponseSplitted =
                usersActiveSetService.showSplittedFlashcardsToUser(1L, new User());


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

    private FlashcardSet createFlashcardSet(List<Flashcard> flashcardList) {
        FlashcardSet flashcardSet = new FlashcardSet();
        flashcardSet.setName("name");
        flashcardSet.setDescription("desc");
        flashcardSet.setFlashcards(flashcardList);

        return flashcardSet;
    }

    private Flashcard createFlashcard(long id, String front, String back) {
        Flashcard flashcard = new Flashcard();
        flashcard.setId(id);
        flashcard.setFront(front);
        flashcard.setBack(back);
        return flashcard;
    }
}