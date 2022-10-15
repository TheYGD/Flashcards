package pl.jszmidla.flashcards.data.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RememberedAndUnrememberedFlashcardsSplitted {

    List<FlashcardResponse> rememberedFlashcardList;
    List<FlashcardResponse> unrememberedFlashcardList;
}
