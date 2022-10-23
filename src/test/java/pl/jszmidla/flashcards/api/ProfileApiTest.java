package pl.jszmidla.flashcards.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.jszmidla.flashcards.ObjectToJsonMapper;
import pl.jszmidla.flashcards.data.dto.StringResponse;
import pl.jszmidla.flashcards.data.mapper.StringResponseMapper;
import pl.jszmidla.flashcards.service.ProfileService;

import javax.validation.Validation;
import javax.validation.Validator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProfileApiTest {

    @Mock
    ProfileService profileService;
    StringResponseMapper stringResponseMapper = new StringResponseMapper();
    MockMvc mockMvc;
    ObjectToJsonMapper jsonMapper = new ObjectToJsonMapper();
    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup( new ProfileApi(profileService, stringResponseMapper) ).build();
    }

    @Test
    void updateBio() throws Exception {
        StringResponse serviceResponse = new StringResponse( HttpStatus.OK.value(), "Changed bio successfully.");
        String expectedJson = jsonMapper.toJson( serviceResponse.getBody() );
        when( profileService.updateBio(any(), any()) ).thenReturn( serviceResponse );
        MockHttpServletRequestBuilder requestBuilder = put("/profile/bio")
                .param("bio", "some bio");

        MvcResult result = mockMvc.perform( requestBuilder )
                .andExpect( status().isOk() )
                .andReturn();
        String response = result.getResponse().getContentAsString();

        assertEquals( expectedJson.substring(1, expectedJson.length() - 1), response );
    }

    @Test
    void updateBioNoBody() throws Exception {
        mockMvc.perform( put("/profile/bio") )
                .andExpect( status().isBadRequest() );
    }

    @Test
    void changePasswordSuccess() throws Exception {
        StringResponse serviceResponse = new StringResponse( HttpStatus.OK.value(),"Changed password successfully.");
        String expectedJson = jsonMapper.toJson( serviceResponse.getBody() );
        when( profileService.changePassword(any(), any()) ).thenReturn( serviceResponse );
        MockHttpServletRequestBuilder requestBuilder = put("/profile/password")
                .param("oldPassword", "secret123")
                .param("newPassword", "hehePass");

        MvcResult result = mockMvc.perform( requestBuilder )
                .andExpect( status().isOk() )
                .andReturn();
        String response = result.getResponse().getContentAsString();

        assertEquals( expectedJson.substring(1, expectedJson.length() - 1), response );
    }

    @Test
    void changePasswordNoBody() throws Exception {
        mockMvc.perform( put("/profile/password") )
                .andExpect( status().isBadRequest() );
    }
}