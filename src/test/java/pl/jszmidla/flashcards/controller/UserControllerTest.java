package pl.jszmidla.flashcards.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.ObjectError;
import pl.jszmidla.flashcards.service.UserService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;


@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    UserService registerService;
    MockMvc mockMvc;


    @BeforeEach
    void setUp() {
        UserController controller = new UserController(registerService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @WithMockUser
    void registerPageLoggedIn() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect( view().name("redirect:/") );
    }

    @Test
    void registerPostSuccess() throws Exception {
        String email = "email@email.com";
        String username = "username";
        String password = "password";

        MockHttpServletRequestBuilder requestBuilder = post("/register")
                .param("email", email)
                .param("username", username)
                .param("password", password);

        when( registerService.checkIfDataIsValid(any()) ).thenReturn(null);

        mockMvc.perform(requestBuilder)
                .andExpect( view().name("redirect:/") );
    }

    @Test
    void registerPostFail() throws Exception {
        String email = "email@email.com";
        String username = "username";
        String password = "password";

        MockHttpServletRequestBuilder requestBuilder = post("/register")
                .param("email", email)
                .param("username", username)
                .param("password", password);

        when( registerService.checkIfDataIsValid(any()) ).thenReturn(new ObjectError("", ""));

        mockMvc.perform(requestBuilder)
                .andExpect( view().name("redirect:/register") );
    }
}