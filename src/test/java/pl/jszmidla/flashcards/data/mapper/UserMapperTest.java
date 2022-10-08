package pl.jszmidla.flashcards.data.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.jszmidla.flashcards.data.User;
import pl.jszmidla.flashcards.data.dto.RegisterUserRequest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserMapperTest {

    @Mock
    PasswordEncoder passwordEncoder;
    @InjectMocks
    UserMapper userMapper;

    @Test
    void register_to_user() {
        String email = "email@email.com";
        String username = "username";
        String password = "password";
        RegisterUserRequest registerUserRequest = new RegisterUserRequest(email, username, password);
        when( passwordEncoder.encode(password) ).thenReturn(password);

        User user = userMapper.register_to_user(registerUserRequest);

        assertEquals(email, user.getEmail());
        assertEquals(username, user.getUsername());
        assertEquals(password, user.getPassword());
    }
}