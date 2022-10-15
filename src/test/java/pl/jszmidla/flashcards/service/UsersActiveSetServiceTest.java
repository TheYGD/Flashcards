package pl.jszmidla.flashcards.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.jszmidla.flashcards.data.FlashcardSet;
import pl.jszmidla.flashcards.data.User;
import pl.jszmidla.flashcards.data.UsersActiveSet;
import pl.jszmidla.flashcards.data.exception.item.UsersActiveSetNotFoundException;
import pl.jszmidla.flashcards.repository.UsersActiveSetRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsersActiveSetServiceTest {

    @Mock
    UsersActiveSetRepository usersActiveSetRepository;
    @InjectMocks
    UsersActiveSetService usersActiveSetService;

    @Test
    void getUsersActiveSet() {
        String csv = "1,2";
        UsersActiveSet usersActiveSet = createUsersActiveSet(csv);
        when( usersActiveSetRepository.findByUserAndFlashcardSet(any(), any()) ).thenReturn(Optional.of(usersActiveSet));

        UsersActiveSet actualUsersActiveSet = usersActiveSetService.getUsersActiveSet(new FlashcardSet(), new User());
        assertEquals( usersActiveSet, actualUsersActiveSet );
    }

    @Test
    void getOrCreateUsersActiveSetGet() {
        String csv = "1,2";
        UsersActiveSet usersActiveSet = createUsersActiveSet(csv);
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
                usersActiveSetService.getUsersActiveSet(new FlashcardSet(), user) );
        assertEquals( user.getId(), actualUsersActiveSet.getUser().getId() );
    }

    @Test
    void getRememberedFlashcardsIds() {
        String id1 = "1";
        String id2 = "2";
        String givenCsv = id1 + "," + id2;
        UsersActiveSet usersActiveSet = createUsersActiveSet(givenCsv);
        when( usersActiveSetRepository.findByUserAndFlashcardSet(any(), any()) ).thenReturn(Optional.of(usersActiveSet));

        Set<Long> valuesFromCsv = usersActiveSetService.getRememberedFlashcardsIds(any(), any());

        assertTrue( valuesFromCsv.contains(Long.valueOf(id1)) );
        assertTrue( valuesFromCsv.contains(Long.valueOf(id2)) );
    }

    @Test
    void markFlashcardAsRemembered() {
        long flashcardId = 2L;
        UsersActiveSet usersActiveSet = spy( UsersActiveSet.class );
        FlashcardSet flashcardSet = new FlashcardSet();
        when( usersActiveSetRepository.findByUserAndFlashcardSet(any(), any()) ).thenReturn(Optional.of(usersActiveSet));

        usersActiveSetService.markFlashcardAsRemembered(flashcardSet, flashcardId, new User());

        verify( usersActiveSet ).addFlashcardToRemembered(flashcardId);
        verify( usersActiveSetRepository ).save(usersActiveSet);
    }

    @Test
    void markFlashcardAsCompleted() {
        UsersActiveSet usersActiveSet = spy( UsersActiveSet.class );
        FlashcardSet flashcardSet = new FlashcardSet();
        when( usersActiveSetRepository.findByUserAndFlashcardSet(any(), any()) ).thenReturn(Optional.of(usersActiveSet));

        usersActiveSetService.markFlashcardAsCompleted(flashcardSet, new User());

        verify( usersActiveSet ).incrementExpirationInterval();
        verify( usersActiveSetRepository ).save(usersActiveSet);
    }

    @Test
    void reloadSetSooner() {
        UsersActiveSet usersActiveSet = spy( UsersActiveSet.class );
        FlashcardSet flashcardSet = new FlashcardSet();
        when( usersActiveSetRepository.findByUserAndFlashcardSet(any(), any()) ).thenReturn(Optional.of(usersActiveSet));

        usersActiveSetService.reloadSetSooner(flashcardSet, new User());

        verify( usersActiveSet ).adjustExpirationDate();
        verify( usersActiveSet ).setRememberedFlashcardsCSV("");
        verify( usersActiveSetRepository ).save(usersActiveSet);
    }

    UsersActiveSet createUsersActiveSet(String csv) {
        UsersActiveSet usersActiveSet = new UsersActiveSet();
        usersActiveSet.setRememberedFlashcardsCSV(csv);
        usersActiveSet.setExpirationDate(LocalDateTime.now().plusDays(1));
        return usersActiveSet;
    }
}