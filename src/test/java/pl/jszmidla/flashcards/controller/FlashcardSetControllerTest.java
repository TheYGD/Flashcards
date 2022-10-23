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
import pl.jszmidla.flashcards.ObjectToJsonMapper;
import pl.jszmidla.flashcards.data.dto.flashcard.FlashcardRequest;
import pl.jszmidla.flashcards.data.dto.flashcard.FlashcardSetRequest;
import pl.jszmidla.flashcards.data.dto.flashcard.FlashcardSetResponse;
import pl.jszmidla.flashcards.data.dto.flashcard.edit.FlashcardSetChangeResponse;
import pl.jszmidla.flashcards.service.FlashcardSetService;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

@ExtendWith({MockitoExtension.class})
class FlashcardSetControllerTest {

    @Mock
    FlashcardSetService flashcardSetService;
    MockMvc mockMvc;
    ObjectToJsonMapper jsonMapper = new ObjectToJsonMapper();


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
    void createSetPage() throws Exception {
        mockMvc.perform( get("/sets/create") )
                .andExpect( view().name("flashcard-set/create-edit") );
    }

    @Test
    void createSetPost() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = createSetRequestBuilder();
        Long id = 1L;
        when( flashcardSetService.createSet(any(), any()) ).thenReturn(id);

        mockMvc.perform( requestBuilder )
                .andExpect( status().is3xxRedirection() )
                .andExpect( view().name("redirect:/sets/" + id) );
    }

    @Test
    void createSetPostNoBody() throws Exception {
        mockMvc.perform( post("/sets/create") )
                .andExpect( status().isBadRequest() );
    }

    private MockHttpServletRequestBuilder createSetRequestBuilder() {
        FlashcardSetRequest flashcardSet = createSet();
        String flashcardSetJson = jsonMapper.toJson(flashcardSet);

        MockHttpServletRequestBuilder requestBuilder = post("/sets/create")
                .contentType("application/json")
                .content(flashcardSetJson);

        return requestBuilder;
    }

    private FlashcardSetRequest createSet() {
        FlashcardSetRequest flashcardSet = new FlashcardSetRequest();
        flashcardSet.setName("namee");
        flashcardSet.setDescription("descc");

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

    @Test
    void learnSet() throws Exception {
        long id = 1L;
        when( flashcardSetService.showSetToUser(anyLong()) ).thenReturn( new FlashcardSetResponse() );

        mockMvc.perform(get("/sets/learn/" + id))
                .andExpect( model().attributeExists("set") )
                .andExpect( view().name("flashcard-set/learn") )
                .andExpect( status().isOk() );
    }

    @Test
    void editPage() throws Exception {
        FlashcardSetChangeResponse set = new FlashcardSetChangeResponse();
        when( flashcardSetService.getSetChangeResponse(anyLong(), any()) ).thenReturn(set);

        mockMvc.perform( get("/sets/1/edit") )
                .andExpect( model().attributeExists("set") )
                .andExpect( view().name("flashcard-set/create-edit") );
    }
}