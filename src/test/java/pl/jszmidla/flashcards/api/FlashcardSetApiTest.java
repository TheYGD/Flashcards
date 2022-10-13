package pl.jszmidla.flashcards.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.jszmidla.flashcards.data.dto.FlashcardResponse;
import pl.jszmidla.flashcards.service.FlashcardService;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

class FlashcardSetApiTest {

    @Mock
    FlashcardService flashcardService;
    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup( new FlashcardSetApi(flashcardService) ).build();
    }

    @Test
    void getFlashcardsFromSet() throws Exception {
        FlashcardResponse flashcardResponse = createFlashCardResponse(1, "fr1", "bc1");
        List<FlashcardResponse> flashcardResponseList = List.of(flashcardResponse);
        String responseJson = parseObjectToJson(flashcardResponseList);
        when( flashcardService.getFlashcardsFromSet(any()) ).thenReturn( flashcardResponseList );

        MvcResult result = mockMvc.perform(get("/api/v1/flashcard-sets/" + 1))
                .andReturn();

        assertEquals(responseJson, result.getResponse().getContentAsString());
    }

    FlashcardResponse createFlashCardResponse(long id, String front, String back) {
        FlashcardResponse flashcardResponse = new FlashcardResponse();
        flashcardResponse.setId(id);
        flashcardResponse.setFront(front);
        flashcardResponse.setBack(back);
        return flashcardResponse;
    }

    @Test
    void markFlashcardAsRemembered() throws Exception {
        String expectedResult = "Success";

        MvcResult result = mockMvc.perform(post("/api/v1/flashcard-sets/1/remembered/1"))
                .andReturn();

        assertEquals(expectedResult, result.getResponse().getContentAsString());
    }

    private String parseObjectToJson(Object flashcardResponseList) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(flashcardResponseList);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}