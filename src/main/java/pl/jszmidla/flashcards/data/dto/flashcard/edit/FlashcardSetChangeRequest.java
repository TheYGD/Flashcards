package pl.jszmidla.flashcards.data.dto.flashcard.edit;

import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
public class FlashcardSetChangeRequest {

    @NotNull
    long id;

    @NotNull
    @Size(min=5, max=50)
    private String name;

    @NotNull
    @Size(min=5, max=200)
    private String description;

    @Valid
    @Size(min=1, max=500)
    private List<FlashcardChangeRequest> flashcardChangeList;
}
