package pl.jszmidla.flashcards.data.dto.flashcard;

import lombok.Getter;
import lombok.Setter;
import pl.jszmidla.flashcards.data.ActiveSetState;

@Getter
@Setter
public class ActiveFlashcardSetResponse {

    private Long id;
    private String name;
    private String description;
    private Long flashcardCount;
    private ActiveSetState state;

    public String getCssClass() {
        return state != null ? state.getCssClass() : "";
    }
}

