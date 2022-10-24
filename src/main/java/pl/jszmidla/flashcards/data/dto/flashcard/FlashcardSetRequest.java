package pl.jszmidla.flashcards.data.dto.flashcard;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
public class FlashcardSetRequest {
    private Long authorId;
    private String authorName;

    @NotNull
    @Size(min=5, max=50)
    private String name;

    @NotNull
    @Size(min=5, max=200)
    private String description;

    @NotNull
    @Size(min=1, max=500)
    private List<FlashcardRequest> flashcardList;
}
