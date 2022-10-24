package pl.jszmidla.flashcards.data.dto.flashcard;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class FlashcardRequest {

    @NotNull
    @Size(max=100)
    private String front;

    @NotNull
    @Size(max=100)
    private String back;
}
