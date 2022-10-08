package pl.jszmidla.flashcards.service;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.ObjectError;
import pl.jszmidla.flashcards.data.dto.RegisterUserRequest;
import pl.jszmidla.flashcards.data.mapper.UserMapper;
import pl.jszmidla.flashcards.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegisterServiceTest {

    @Mock
    UserMapper userMapper;
    @Mock
    UserRepository userRepository;
    @InjectMocks
    RegisterService registerService;


    @Test
    void register_user() {
        RegisterUserRequest registerUserRequest = create_register_user_request();
        registerService.register_user(registerUserRequest);
    }

    @Test
    void check_data_valid() {
        RegisterUserRequest registerUserRequest = create_register_user_request();
        when( userRepository.existsByUsername(any()) ).thenReturn(false);
        when( userRepository.existsByEmail(any()) ).thenReturn(false);

        ObjectError result = registerService.check_if_data_is_valid(registerUserRequest);

        assertNull(result);
    }

    @Test
    void check_email_taken() {
        RegisterUserRequest registerUserRequest = create_register_user_request();
        String objectName = "email";
        when( userRepository.existsByEmail(any()) ).thenReturn(true);

        ObjectError result = registerService.check_if_data_is_valid(registerUserRequest);

        assertEquals(objectName, result.getObjectName());
    }

    @Test
    void check_username_taken() {
        RegisterUserRequest registerUserRequest = create_register_user_request();
        String objectName = "username";
        when( userRepository.existsByEmail(any()) ).thenReturn(false);
        when( userRepository.existsByUsername(any()) ).thenReturn(true);

        ObjectError result = registerService.check_if_data_is_valid(registerUserRequest);

        assertEquals(objectName, result.getObjectName());
    }

    private RegisterUserRequest create_register_user_request() {
        return new RegisterUserRequest("email", "username", "password");
    }
}