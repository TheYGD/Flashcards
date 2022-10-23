package pl.jszmidla.flashcards.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.jszmidla.flashcards.data.dto.flashcard.ActiveFlashcardSetResponse;
import pl.jszmidla.flashcards.service.UsersActiveSetService;

import java.util.LinkedList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith(MockitoExtension.class)
class ActiveSetControllerTest {

    @Mock
    UsersActiveSetService usersActiveSetService;
    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup( new ActiveSetController(usersActiveSetService) ).build();
    }

    @Test
    void searchForSets() throws Exception {
        List<ActiveFlashcardSetResponse> activeFlashcardSetResponses = new LinkedList<>();
        String query = "search";
        when( usersActiveSetService.findSetsByQuery(any(), any()) ).thenReturn(activeFlashcardSetResponses);

        mockMvc.perform( get("/sets/search?query=" + query) )
                .andExpect( model().attribute("setList", activeFlashcardSetResponses) )
                .andExpect( view().name("flashcard-set/search") );
    }
}