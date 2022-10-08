package pl.jszmidla.flashcards.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.ObjectError;
import pl.jszmidla.flashcards.service.RegisterService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;


@ExtendWith(MockitoExtension.class)
class RegisterControllerTest {

    @Mock
    RegisterService registerService;
    MockMvc mockMvc;


    @BeforeEach
    void setUp() {
        RegisterController controller = new RegisterController(registerService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }


    @Test
    void register_page() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect( view().name("user/register") );
    }

    @Test
    void register_post_success() throws Exception {
        String email = "email@email.com";
        String username = "username";
        String password = "password";

        MockHttpServletRequestBuilder requestBuilder = post("/register")
                .param("email", email)
                .param("username", username)
                .param("password", password);

        when( registerService.check_if_data_is_valid(any()) ).thenReturn(null);

        mockMvc.perform(requestBuilder)
                .andExpect( view().name("redirect:/") );
    }

    @Test
    void register_post_fail() throws Exception {
        String email = "email@email.com";
        String username = "username";
        String password = "password";

        MockHttpServletRequestBuilder requestBuilder = post("/register")
                .param("email", email)
                .param("username", username)
                .param("password", password);

        when( registerService.check_if_data_is_valid(any()) ).thenReturn(new ObjectError("", ""));

        mockMvc.perform(requestBuilder)
                .andExpect( view().name("redirect:/register") );
    }
}