package pl.jszmidla.flashcards.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import pl.jszmidla.flashcards.data.User;
import pl.jszmidla.flashcards.data.dto.flashcard.ActiveFlashcardSetResponse;
import pl.jszmidla.flashcards.service.ProfileService;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class ProfileControllerTest {

    @Mock
    ProfileService profileService;
    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup( new ProfileController(profileService) ).build();
    }


    @Test
    void showUsersProfile() throws Exception {
        User user = createUser("email@email.com", "username", "pass");
        List<ActiveFlashcardSetResponse> sets = createFlashcardSetList();
        when( profileService.getUser(any()) ).thenReturn(user);
        when( profileService.getUsersActiveSets(any(), any()) ).thenReturn(sets);

        mockMvc.perform( get("/profiles/1") )
                .andExpect( model().attribute("user", user) )
                .andExpect( model().attribute("usersSetList", sets) )
                .andExpect( view().name("profiles/show") );
    }

    private User createUser(String email, String username, String password) {
        User user = new User();
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(password);
        return user;
    }

    private List<ActiveFlashcardSetResponse> createFlashcardSetList() {
        ActiveFlashcardSetResponse set1 = new ActiveFlashcardSetResponse();
        set1.setId(1L);
        ActiveFlashcardSetResponse set2 = new ActiveFlashcardSetResponse();
        set2.setId(2L);
        ActiveFlashcardSetResponse set3 = new ActiveFlashcardSetResponse();
        set3.setId(3L);

        return List.of( set1, set2, set3 );
    }

    @Test
    void showOwnProfile() throws Exception {
        mockMvc.perform( get("/profile") )
                .andExpect( model().attributeExists("user") )
                .andExpect( view().name("profiles/own") )
                .andExpect( status().isOk() );
    }
}