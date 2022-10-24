package pl.jszmidla.flashcards.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.jszmidla.flashcards.ObjectToJsonMapper;
import pl.jszmidla.flashcards.data.dto.flashcard.FlashcardResponse;
import pl.jszmidla.flashcards.data.dto.flashcard.RememberedAndUnrememberedFlashcardsSplitted;
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
    ObjectToJsonMapper jsonMapper = new ObjectToJsonMapper();


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
        String responseJson = jsonMapper.toJson(flashcardsSplitted);
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

    @Test
    void getSetExpirationDate() throws Exception {
        LocalDateTime date = LocalDateTime.now();
        Object[] expectedDate = Arrays.stream(date.toString().split(":|-|T|\\."))
                .map(Integer::valueOf)
                .toArray();
        String expectedString = jsonMapper.toJson(expectedDate);

        when( usersActiveSetService.getSetReloadDate(anyLong(), any()) ).thenReturn(date);
        MvcResult result = mockMvc.perform(get("/api/v1/active-sets/1/expire"))
                .andReturn();
        String stringResult = result.getResponse().getContentAsString();

        // removing excessive 0 from nanoseconds
        while (expectedString.charAt( expectedString.length() - 2 ) == '0') {
            expectedString = expectedString.substring(0, expectedString.length() - 2) + ']';
        }
        while (stringResult.charAt( stringResult.length() - 2 ) == '0') {
            stringResult = stringResult.substring(0, stringResult.length() - 2) + ']';
        }

        assertEquals( expectedString, stringResult );
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
}