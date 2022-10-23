package pl.jszmidla.flashcards.data.dto.flashcard.edit;

import lombok.Getter;
import lombok.Setter;
import pl.jszmidla.flashcards.data.dto.flashcard.FlashcardRequest;

import javax.validation.Valid;

@Getter
@Setter
public class FlashcardChangeRequest {

    Long id;

    @Valid
    FlashcardRequest body;
}
