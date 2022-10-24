package pl.jszmidla.flashcards.data.dto.account;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterUserRequest {

    @NotNull
    @Email
    private String email;

    @NotNull
    @Size(min=5, max=20)
    private String username;

    @NotNull
    @Size(min=8, max=30)
    private String password;
}
