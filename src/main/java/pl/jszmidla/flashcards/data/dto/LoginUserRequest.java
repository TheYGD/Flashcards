package pl.jszmidla.flashcards.data.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginUserRequest {
    private String usernameOrEmail;
    private String password;
}
