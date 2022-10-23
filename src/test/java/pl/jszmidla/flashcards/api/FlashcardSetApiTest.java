package pl.jszmidla.flashcards.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.jszmidla.flashcards.ObjectToJsonMapper;
import pl.jszmidla.flashcards.data.dto.StringResponse;
import pl.jszmidla.flashcards.data.dto.flashcard.FlashcardSetResponse;
import pl.jszmidla.flashcards.data.dto.flashcard.edit.FlashcardSetChangeRequest;
import pl.jszmidla.flashcards.data.mapper.StringResponseMapper;
import pl.jszmidla.flashcards.service.FlashcardSetService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class FlashcardSetApiTest {

    @Mock
    FlashcardSetService flashcardSetService;
    StringResponseMapper stringResponseMapper = new StringResponseMapper();
    ObjectToJsonMapper jsonMapper = new ObjectToJsonMapper();
    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup( new FlashcardSetApi(flashcardSetService, stringResponseMapper) )
                .build();
    }

    @Test
    void getOwnSets() throws Exception {
        FlashcardSetResponse setResponse1 = createFlashcardSetResponse(1, "name1", "desc1");
        FlashcardSetResponse setResponse2 = createFlashcardSetResponse(2, "name2", "desc2");
        FlashcardSetResponse setResponse3 = createFlashcardSetResponse(3, "name3", "desc3");
        List<FlashcardSetResponse> flashcardSetResponseList = List.of(setResponse1, setResponse2, setResponse3);
        String expectedJson = jsonMapper.toJson(flashcardSetResponseList);
        when( flashcardSetService.getUsersSetsResponses(any()) ).thenReturn(flashcardSetResponseList);

        MvcResult result = mockMvc.perform( get("/api/v1/your-sets") )
                .andExpect( status().isOk() )
                .andReturn();
        String actualJson = result.getResponse().getContentAsString();

        assertEquals( expectedJson, actualJson );
    }

    private FlashcardSetResponse createFlashcardSetResponse(long id, String name, String description) {
        FlashcardSetResponse response = new FlashcardSetResponse();
        response.setId(id);
        response.setName(name);
        response.setDescription(description);
        return response;
    }

    @Test
    void deleteSet() throws Exception {
        long id = 1L;
        String message = "Set deleted.";
        StringResponse stringResponse = new StringResponse( HttpStatus.OK.value(), message );
        when( flashcardSetService.deleteSet(anyLong(), any()) ).thenReturn(stringResponse);

        MvcResult result = mockMvc.perform( delete("/api/v1/your-sets/" + id) )
                .andExpect( status().isOk() )
                .andReturn();

        assertEquals( message, result.getResponse().getContentAsString() );
    }

    @Test
    void deleteSetWrongId() throws Exception {
        long id = 1L;
        String message = "Error. Try again later.";
        StringResponse stringResponse = new StringResponse( HttpStatus.BAD_REQUEST.value(), message );
        when( flashcardSetService.deleteSet(anyLong(), any()) ).thenReturn(stringResponse);

        MvcResult result = mockMvc.perform( delete("/api/v1/your-sets/" + id) )
                .andExpect( status().isBadRequest() )
                .andReturn();

        assertEquals( message, result.getResponse().getContentAsString() );
    }

    @Test
    void editSet() throws Exception {
        String message = "Set edited.";
        StringResponse stringResponse = new StringResponse( HttpStatus.OK.value(), message );
        FlashcardSetChangeRequest changeRequest = createFlashcardSetChangeRequest();
        String changeRequestJson = jsonMapper.toJson(changeRequest);
        when( flashcardSetService.editSet(any(), any()) ).thenReturn(stringResponse);

        MockHttpServletRequestBuilder requestBuilder = put("/api/v1/your-sets/")
                .contentType("application/json")
                .content(changeRequestJson);

        MvcResult result = mockMvc.perform( requestBuilder )
                .andExpect( status().isOk() )
                .andReturn();

        assertEquals( message, result.getResponse().getContentAsString() );
    }

    private FlashcardSetChangeRequest createFlashcardSetChangeRequest() {
        FlashcardSetChangeRequest changeRequest = new FlashcardSetChangeRequest();
        changeRequest.setId(1L);
        changeRequest.setName("namee");
        changeRequest.setDescription("description");
        return changeRequest;
    }

    @Test
    void editSetWrongId() throws Exception {
        String message = "Error. Try again later.";
        StringResponse stringResponse = new StringResponse( HttpStatus.BAD_REQUEST.value(), message );
        FlashcardSetChangeRequest changeRequest = createFlashcardSetChangeRequest();
        String changeRequestJson = jsonMapper.toJson(changeRequest);
        when( flashcardSetService.editSet(any(), any()) ).thenReturn(stringResponse);

        MockHttpServletRequestBuilder requestBuilder = put("/api/v1/your-sets/")
                .contentType("application/json")
                .content(changeRequestJson);

        MvcResult result = mockMvc.perform( requestBuilder )
                .andExpect( status().isBadRequest() )
                .andReturn();

        assertEquals( message, result.getResponse().getContentAsString() );
    }

    @Test
    void editSetNoBody() throws Exception {
        mockMvc.perform( put("/api/v1/your-sets/") )
                .andExpect( status().isBadRequest() )
                .andReturn();
    }
}