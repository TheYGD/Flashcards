package pl.jszmidla.flashcards.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.jszmidla.flashcards.data.FlashcardSet;
import pl.jszmidla.flashcards.data.dto.FlashcardRequest;
import pl.jszmidla.flashcards.data.dto.FlashcardSetRequest;
import pl.jszmidla.flashcards.data.dto.FlashcardSetResponse;
import pl.jszmidla.flashcards.service.FlashcardSetService;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.LinkedList;
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
    void showSet() throws Exception {
        long id = 1L;
        mockMvc.perform( get("/sets/" + id) )
                .andExpect( view().name("flashcard-set/show") );
    }

    @Test
    void searchForSets() throws Exception {
        List<FlashcardSetResponse> flashcardSetResponseList = new LinkedList<>();
        String query = "search";
        when( flashcardSetService.findSetsByQuery(any()) ).thenReturn(flashcardSetResponseList);

        mockMvc.perform( get("/sets/search?query=" + query) )
                .andExpect( model().attribute("setList", flashcardSetResponseList) )
                .andExpect( view().name("flashcard-set/search") );
    }

    @Test
    void createSetPage() throws Exception {
        mockMvc.perform( get("/sets/create") )
                .andExpect( view().name("flashcard-set/create") );
    }

    @Test
    void createSetPost() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = createSetRequestBuilder();
        Long id = 1L;
        when( flashcardSetService.createSet(any(), any()) ).thenReturn(id);

        mockMvc.perform( requestBuilder )
                .andExpect( view().name("redirect:/sets/" + id) );
    }

    private MockHttpServletRequestBuilder createSetRequestBuilder() {
        FlashcardSetRequest flashcardSet = createSet();
        String flashcardSetJson = convertObjectToJson(flashcardSet);

        MockHttpServletRequestBuilder requestBuilder = post("/sets/create")
                .contentType("application/json")
                .content(flashcardSetJson);

        return requestBuilder;
    }


    private FlashcardSetRequest createSet() {
        FlashcardSetRequest flashcardSet = new FlashcardSetRequest();
        flashcardSet.setName("name");
        flashcardSet.setDescription("desc");

        FlashcardRequest flashcardDto1 = createFlashcard("front1", "back1");
        FlashcardRequest flashcardDto2 = createFlashcard("front2", "back2");
        FlashcardRequest flashcardDto3= createFlashcard("front3", "back3");

        flashcardSet.setFlashcardList( List.of(flashcardDto1, flashcardDto2, flashcardDto3) );
        return flashcardSet;
    }

    private FlashcardRequest createFlashcard(String front, String back) {
        FlashcardRequest flashcard = new FlashcardRequest();
        flashcard.setFront(front);
        flashcard.setBack(back);
        return flashcard;
    }

    private String convertObjectToJson(Object object) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void deleteSet() throws Exception {
        long id = 1;
        mockMvc.perform(delete("/sets/delete/" + id))
                .andExpect( view().name("redirect:/profile/sets") );
    }

    @Test
    void learnSet() {

    }
}