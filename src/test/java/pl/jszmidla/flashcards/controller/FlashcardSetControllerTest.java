package pl.jszmidla.flashcards.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.jszmidla.flashcards.data.Flashcard;
import pl.jszmidla.flashcards.data.FlashcardSet;
import pl.jszmidla.flashcards.service.FlashcardSetService;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.List;

@ExtendWith({MockitoExtension.class})
class FlashcardSetControllerTest {

    @Mock
    FlashcardSetService flashcardSetService;
    MockMvc mockMvc;


    @BeforeEach
    void setUp() {
        FlashcardSetController flashcardSetController = new FlashcardSetController(flashcardSetService);
        mockMvc = MockMvcBuilders.standaloneSetup( flashcardSetController ).build();
    }

    @Test
    void show_by_id() throws Exception {
        Long id = 1L;

        mockMvc.perform( get("/sets/" + id) )
                .andExpect( view().name("flashcard-set/show") );
    }

    @Test
    void create_set_page() throws Exception {
        mockMvc.perform( get("/sets/create") )
                .andExpect( view().name("flashcard-set/create") );
    }

    @Test
    void create_set_post() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = create_set_request_builder();
        Long id = 1L;
        when( flashcardSetService.create_set(any(), any()) ).thenReturn(id);

        mockMvc.perform( requestBuilder )
                .andExpect( view().name("redirect:/sets/" + id) );
    }

    private MockHttpServletRequestBuilder create_set_request_builder() {
        FlashcardSet flashcardSet = create_set();

        MockHttpServletRequestBuilder requestBuilder = post("/sets/create")
                .param("authorId", "1")
                .param("authorName", "name")
                .param("name", flashcardSet.getName())
                .param("description", flashcardSet.getDescription());
//                .param("flashcardList", "[{\"front\": \"front\", \"back\": \"back\"}]");

        return requestBuilder;
    }


    private FlashcardSet create_set() {
        FlashcardSet flashcardSet = new FlashcardSet();
        flashcardSet.setName("name");
        flashcardSet.setDescription("desc");

        Flashcard flashcardDto1 = create_flashcard("front1", "back1");
        Flashcard flashcardDto2 = create_flashcard("front2", "back2");
        Flashcard flashcardDto3= create_flashcard("front3", "back3");

        flashcardSet.setFlashcards( List.of(flashcardDto1, flashcardDto2, flashcardDto3) );
        return flashcardSet;
    }

    private Flashcard create_flashcard(String front, String back) {
        Flashcard flashcard = new Flashcard();
        flashcard.setFront(front);
        flashcard.setBack(back);
        return flashcard;
    }
}