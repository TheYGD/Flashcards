package pl.jszmidla.flashcards.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.ObjectError;
import pl.jszmidla.flashcards.data.User;
import pl.jszmidla.flashcards.data.dto.RegisterUserRequest;
import pl.jszmidla.flashcards.data.mapper.UserMapper;
import pl.jszmidla.flashcards.repository.UserRepository;

@Service
@AllArgsConstructor
public class RegisterService {

    private UserRepository userRepository;
    private UserMapper userMapper;

    public void register_user(RegisterUserRequest registerUserRequest) {
        User user = userMapper.register_to_user(registerUserRequest);
        userRepository.save(user);
    }

    public ObjectError check_if_data_is_valid(RegisterUserRequest registerUserRequest) {
        if (isEmailTaken(registerUserRequest.getEmail())) {
            return new ObjectError("email", "Email is taken!");
        }

        if (isUsernameTaken(registerUserRequest.getUsername())) {
            return new ObjectError("username", "Username is taken!");
        }

        return null;
    }

    private boolean isUsernameTaken(String username) {
        return userRepository.existsByUsername(username);
    }

    private boolean isEmailTaken(String email) {
        return userRepository.existsByEmail(email);
    }
}
