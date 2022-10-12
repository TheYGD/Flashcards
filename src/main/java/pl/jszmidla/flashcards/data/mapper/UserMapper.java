package pl.jszmidla.flashcards.data.mapper;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pl.jszmidla.flashcards.data.User;
import pl.jszmidla.flashcards.data.dto.RegisterUserRequest;

@Component
@AllArgsConstructor
public class UserMapper {

    private PasswordEncoder passwordEncoder;

    public User registerToUser(RegisterUserRequest register) {
        String encodedPassword = passwordEncoder.encode( register.getPassword() );
        User user = new User();
        user.setEmail(register.getEmail());
        user.setUsername(register.getUsername());
        user.setPassword(encodedPassword);

        return user;
    }
}
