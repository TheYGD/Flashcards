package pl.jszmidla.flashcards.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.ObjectError;
import pl.jszmidla.flashcards.data.User;
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
    void registerUser() {
        RegisterUserRequest registerUserRequest = createRegisterUserRequest();
        when( userMapper.registerToUser(any()) ).thenReturn(new User());

        registerService.registerUser(registerUserRequest);
    }

    @Test
    void checkDataValid() {
        RegisterUserRequest registerUserRequest = createRegisterUserRequest();
        when( userRepository.existsByUsername(any()) ).thenReturn(false);
        when( userRepository.existsByEmail(any()) ).thenReturn(false);

        ObjectError result = registerService.checkIfDataIsValid(registerUserRequest);

        assertNull(result);
    }

    @Test
    void checkEmailTaken() {
        RegisterUserRequest registerUserRequest = createRegisterUserRequest();
        String objectName = "email";
        when( userRepository.existsByEmail(any()) ).thenReturn(true);

        ObjectError result = registerService.checkIfDataIsValid(registerUserRequest);

        assertEquals(objectName, result.getObjectName());
    }

    @Test
    void checkUsernameTaken() {
        RegisterUserRequest registerUserRequest = createRegisterUserRequest();
        String objectName = "username";
        when( userRepository.existsByEmail(any()) ).thenReturn(false);
        when( userRepository.existsByUsername(any()) ).thenReturn(true);

        ObjectError result = registerService.checkIfDataIsValid(registerUserRequest);

        assertEquals(objectName, result.getObjectName());
    }

    private RegisterUserRequest createRegisterUserRequest() {
        return new RegisterUserRequest("email", "username", "password");
    }
}