package pl.jszmidla.flashcards.data.dto.flashcard;

import lombok.Getter;
import lombok.Setter;
import pl.jszmidla.flashcards.data.dto.flashcard.FlashcardResponse;

import java.util.List;

@Getter
@Setter
public class RememberedAndUnrememberedFlashcardsSplitted {

    List<FlashcardResponse> rememberedFlashcardList;
    List<FlashcardResponse> unrememberedFlashcardList;
}
