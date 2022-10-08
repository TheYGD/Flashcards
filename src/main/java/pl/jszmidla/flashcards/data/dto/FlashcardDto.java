package pl.jszmidla.flashcards.data.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class FlashcardDto {

    @NotNull
    @Size(max=100)
    private String front;

    @NotNull
    @Size(max=100)
    private String back;
}
