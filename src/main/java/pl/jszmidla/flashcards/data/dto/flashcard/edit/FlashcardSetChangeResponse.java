package pl.jszmidla.flashcards.data.dto.flashcard.edit;

import lombok.Getter;
import lombok.Setter;
import pl.jszmidla.flashcards.data.dto.flashcard.FlashcardResponse;

import java.util.List;

@Getter
@Setter
public class FlashcardSetChangeResponse {

    private long id;
    private String name;
    private String description;
    private List<FlashcardResponse> flashcardList;
}
