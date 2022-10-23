package pl.jszmidla.flashcards.data.dto.account;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ChangeBioRequest {

    @NotNull
    String bio;
}
