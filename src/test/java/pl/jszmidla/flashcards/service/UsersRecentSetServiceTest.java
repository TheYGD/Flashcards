package pl.jszmidla.flashcards.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.jszmidla.flashcards.data.FlashcardSet;
import pl.jszmidla.flashcards.data.User;
import pl.jszmidla.flashcards.data.UsersRecentSet;
import pl.jszmidla.flashcards.repository.UsersRecentSetRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsersRecentSetServiceTest {

    @Mock
    UsersRecentSetRepository usersRecentSetRepository;
    @InjectMocks
    UsersRecentSetService usersRecentSetService;


    @Test
    void findByUser() {
        UsersRecentSet usersRecentSet = new UsersRecentSet();
        when( usersRecentSetRepository.findByUser(any()) ).thenReturn(Optional.of(usersRecentSet));

        UsersRecentSet actualUsersActiveSet = usersRecentSetService.findByUser(any());

        assertEquals(usersRecentSet, actualUsersActiveSet);
    }

    @Test
    void getUsersRecentSetListLogged() {
        UsersRecentSet usersRecentSet = new UsersRecentSet();
        List<FlashcardSet> usersRecentSetList = createUsersRecentSetList();
        usersRecentSet.setFlashcardSetList(usersRecentSetList);
        when( usersRecentSetRepository.findByUser(any()) ).thenReturn(Optional.of(usersRecentSet));

        List<FlashcardSet> flashcardSetList = usersRecentSetService.getUsersRecentSetListOrNull(any());

        assertEquals(usersRecentSet.getFlashcardSetList(), flashcardSetList);

    }

    @Test
    void getUsersRecentSetListNotLogged() {
        when( usersRecentSetRepository.findByUser(any()) ).thenReturn(Optional.empty());

        List<FlashcardSet> flashcardSetList = usersRecentSetService.getUsersRecentSetListOrNull(any());

        assertNull(flashcardSetList);

    }

    private List<FlashcardSet> createUsersRecentSetList() {
        FlashcardSet usersRecentSet = new FlashcardSet();
        return List.of(usersRecentSet, usersRecentSet);
    }

    @Test
    void addRecentSetUnder5() {
        int sizeUnder5 = 4;
        List<FlashcardSet> flashcardSetList = Mockito.spy(List.class);
        UsersRecentSet usersRecentSet = new UsersRecentSet();
        usersRecentSet.setFlashcardSetList(flashcardSetList);
        when( usersRecentSetRepository.findByUser(any()) ).thenReturn(Optional.of(usersRecentSet));
        when( flashcardSetList.size() ).thenReturn(sizeUnder5);

        usersRecentSetService.addRecentSetIfLogged(new User(), new FlashcardSet());

        verify( flashcardSetList ).size();
        verify( flashcardSetList, times(0) ).remove(any());
    }

    @Test
    void addRecentSetOver5() {
        int size = 5;
        List<FlashcardSet> flashcardSetList = Mockito.spy(List.class);
        UsersRecentSet usersRecentSet = new UsersRecentSet();
        usersRecentSet.setFlashcardSetList(flashcardSetList);
        when( usersRecentSetRepository.findByUser(any()) ).thenReturn(Optional.of(usersRecentSet));
        when( flashcardSetList.size() ).thenReturn(size);

        usersRecentSetService.addRecentSetIfLogged(new User(), new FlashcardSet());

        verify( flashcardSetList ).size();
        verify( flashcardSetList ).remove(0);
    }

    @Test
    void addRecentNotLogged() {
        Optional<UsersRecentSet> usersRecentSetOptional = Optional.empty();
        when( usersRecentSetRepository.findByUser(any()) ).thenReturn(usersRecentSetOptional);

        usersRecentSetService.addRecentSetIfLogged(new User(), new FlashcardSet());

        verify( usersRecentSetRepository, times(0) ).save(any());
    }
}