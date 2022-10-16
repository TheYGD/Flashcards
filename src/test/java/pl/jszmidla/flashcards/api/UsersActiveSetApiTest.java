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
import pl.jszmidla.flashcards.data.dto.RememberedAndUnrememberedFlashcardsSplitted;
import pl.jszmidla.flashcards.service.UsersActiveSetService;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

class UsersActiveSetApiTest {

    @Mock
    UsersActiveSetService usersActiveSetService;
    MockMvc mockMvc;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup( new UsersActiveSetApi(usersActiveSetService) ).build();
    }


    @Test
    void getSplittedFlashcardsFromSet() throws Exception {
        FlashcardResponse flashcardResponse = createFlashCardResponse(1, "fr1", "bc1");
        RememberedAndUnrememberedFlashcardsSplitted flashcardsSplitted = new RememberedAndUnrememberedFlashcardsSplitted();
        flashcardsSplitted.setRememberedFlashcardList(List.of(flashcardResponse));
        String responseJson = parseObjectToJson(flashcardsSplitted);
        when( usersActiveSetService.showSplittedFlashcardsToUser(any(), any()) ).thenReturn( flashcardsSplitted );

        MvcResult result = mockMvc.perform(get("/api/v1/active-sets/" + 1))
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

    private String parseObjectToJson(Object flashcardResponseList) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(flashcardResponseList);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getSetExpirationDate() throws Exception {
        LocalDateTime date = LocalDateTime.now();
        Object[] splittedDate = Arrays.stream(date.toString().split(":|-|T|\\."))
                .map(Long::valueOf).toArray();
        String expectedResult = parseObjectToJson(splittedDate);

        when( usersActiveSetService.getSetExpirationDate(anyLong(), any()) ).thenReturn(date);
        MvcResult result = mockMvc.perform(get("/api/v1/active-sets/1/expire"))
                .andReturn();

        assertEquals(expectedResult, result.getResponse().getContentAsString());
    }

    @Test
    void reloadSetSooner() throws Exception {
        String expectedResult = "Success";

        MvcResult result = mockMvc.perform(post("/api/v1/active-sets/1/reload"))
                .andReturn();

        assertEquals(expectedResult, result.getResponse().getContentAsString());
    }

    @Test
    void markFlashcardFromSetAsRemembered() throws Exception {
        String expectedResult = "Success";

        MvcResult result = mockMvc.perform(post("/api/v1/active-sets/1/remembered/1"))
                .andReturn();

        assertEquals(expectedResult, result.getResponse().getContentAsString());
    }

    @Test
    void markSetAsCompleted() throws Exception {
        String expectedResult = "Success";

        MvcResult result = mockMvc.perform(post("/api/v1/active-sets/1/completed"))
                .andReturn();

        assertEquals(expectedResult, result.getResponse().getContentAsString());
    }
}