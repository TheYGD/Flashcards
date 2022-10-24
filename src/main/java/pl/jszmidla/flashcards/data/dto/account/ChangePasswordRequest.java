package pl.jszmidla.flashcards.data.dto.account;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class ChangePasswordRequest {

    @NotNull
    @Size(min=8, max=30)
    private String oldPassword;

    @NotNull
    @Size(min=8, max=30)
    private String newPassword;
}
