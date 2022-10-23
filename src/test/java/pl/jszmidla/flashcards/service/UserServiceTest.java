package pl.jszmidla.flashcards.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.ObjectError;
import pl.jszmidla.flashcards.data.User;
import pl.jszmidla.flashcards.data.dto.account.RegisterUserRequest;
import pl.jszmidla.flashcards.data.mapper.UserMapper;
import pl.jszmidla.flashcards.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserMapper userMapper;
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    UserRepository userRepository;
    @InjectMocks
    UserService userService;


    @Test
    void findById() {
        User user = createUser("email@email.com", "username", "pass");
        when( userRepository.findById(any()) ).thenReturn(Optional.of(user));

        User actualUser = userService.findById(anyLong());

        assertEquals(user.getEmail(), actualUser.getEmail());
        assertEquals(user.getUsername(), actualUser.getUsername());
        assertEquals(user.getPassword(), actualUser.getPassword());
    }

    private User createUser(String email, String username, String password) {
        User user = new User();
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(password);
        return user;
    }

    @Test
    void registerUser() {
        RegisterUserRequest registerUserRequest = createRegisterUserRequest();
        when( userMapper.registerToUser(any()) ).thenReturn(new User());

        userService.registerUser(registerUserRequest);
    }

    @Test
    void checkDataValid() {
        RegisterUserRequest registerUserRequest = createRegisterUserRequest();
        when( userRepository.existsByUsername(any()) ).thenReturn(false);
        when( userRepository.existsByEmail(any()) ).thenReturn(false);

        ObjectError result = userService.checkIfDataIsValid(registerUserRequest);

        assertNull(result);
    }

    @Test
    void checkEmailTaken() {
        RegisterUserRequest registerUserRequest = createRegisterUserRequest();
        String objectName = "email";
        when( userRepository.existsByEmail(any()) ).thenReturn(true);

        ObjectError result = userService.checkIfDataIsValid(registerUserRequest);

        assertEquals(objectName, result.getObjectName());
    }

    @Test
    void checkUsernameTaken() {
        RegisterUserRequest registerUserRequest = createRegisterUserRequest();
        String objectName = "username";
        when( userRepository.existsByEmail(any()) ).thenReturn(false);
        when( userRepository.existsByUsername(any()) ).thenReturn(true);

        ObjectError result = userService.checkIfDataIsValid(registerUserRequest);

        assertEquals(objectName, result.getObjectName());
    }

    private RegisterUserRequest createRegisterUserRequest() {
        return new RegisterUserRequest("email", "username", "password");
    }
}