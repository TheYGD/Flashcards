package pl.jszmidla.flashcards.data.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import pl.jszmidla.flashcards.data.FlashcardSet;
import pl.jszmidla.flashcards.data.dto.FlashcardSetRequest;
import pl.jszmidla.flashcards.data.dto.FlashcardSetResponse;

@Component
@AllArgsConstructor
public class FlashcardSetMapper {

    private FlashcardMapper flashcardMapper;

    public FlashcardSet requestToEntity(FlashcardSetRequest flashcardSetRequest) {
        FlashcardSet flashcardSet = new FlashcardSet();
        flashcardSet.setName(flashcardSetRequest.getName());
        flashcardSet.setDescription(flashcardSetRequest.getDescription());
        flashcardSet.setFlashcards( flashcardSetRequest.getFlashcardList().stream()
                .map( flashcardDto -> flashcardMapper.requestToEntity(flashcardDto) )
                .toList() );

        return flashcardSet;
    }
}
