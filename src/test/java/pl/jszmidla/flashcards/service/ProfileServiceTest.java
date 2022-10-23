package pl.jszmidla.flashcards.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import pl.jszmidla.flashcards.data.FlashcardSet;
import pl.jszmidla.flashcards.data.User;
import pl.jszmidla.flashcards.data.dto.StringResponse;
import pl.jszmidla.flashcards.data.dto.account.ChangeBioRequest;
import pl.jszmidla.flashcards.data.dto.flashcard.ActiveFlashcardSetResponse;
import pl.jszmidla.flashcards.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProfileServiceTest {

    @Mock
    UserRepository userRepository;
    @Mock
    UserService userService;
    @Mock
    FlashcardSetService flashcardSetService;
    @Mock
    UsersActiveSetService usersActiveSetService;
    @InjectMocks
    ProfileService profileService;


    @Test
    void getUsersSets() {
        List<FlashcardSet> sets = createFlashcardSetList() ;
        List<ActiveFlashcardSetResponse> activeSets = createActiveFlashcardSetList(sets);
        when( usersActiveSetService.getActiveSetsResponsesFromSets(any(), any()) ).thenReturn(activeSets);

        List<ActiveFlashcardSetResponse> actualSets = profileService.getUsersActiveSets(new User(), new User());

        assertEquals(activeSets, actualSets);
    }

    @Test
    void getUser() {
        User user = createUser("email@email.com", "username", "pass");
        when( userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        User actualUser = profileService.getUser(anyLong());

        assertEquals(user, actualUser);
    }

    private User createUser(String email, String username, String password) {
        User user = new User();
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(password);
        return user;
    }

    private List<FlashcardSet> createFlashcardSetList() {
        FlashcardSet set1 = new FlashcardSet();
        set1.setId(1L);
        FlashcardSet set2 = new FlashcardSet();
        set2.setId(2L);
        FlashcardSet set3 = new FlashcardSet();
        set3.setId(3L);

        return List.of(set1, set2, set3);
    }

    private List<ActiveFlashcardSetResponse> createActiveFlashcardSetList(List<FlashcardSet> flashcardSetList) {
        return flashcardSetList.stream().map( set -> {
            ActiveFlashcardSetResponse response = new ActiveFlashcardSetResponse();
            response.setId( set.getId() );
            return  response;
        })
        .toList();
    }

    @Test
    void getUsersActiveSets() {
        List<FlashcardSet> sets = createFlashcardSetList();
        List<ActiveFlashcardSetResponse> activeSets = createActiveFlashcardSetList(sets);
        when( flashcardSetService.getUsersSets( any()) ).thenReturn( sets );
        when( usersActiveSetService.getActiveSetsResponsesFromSets( any(), any()) ).thenReturn( activeSets );

        List<ActiveFlashcardSetResponse> actualSets = profileService.getUsersActiveSets(new User(), new User());

        assertEquals( activeSets.size(), actualSets.size() );
        for (int i = 0; i < activeSets.size(); i++) {
            assertEquals( activeSets.get(i).getId(), actualSets.get(i).getId() );
        }
    }

    @Test
    void updateBio() {
        String message = "Changed bio successfully.";
        int status = HttpStatus.OK.value();
        ChangeBioRequest changeBioRequest = new ChangeBioRequest();
        User user = new User();

        StringResponse response = profileService.updateBio( changeBioRequest, user );

        assertEquals( message, response.getBody() );
        assertEquals( status, response.getStatusCode() );
    }
}