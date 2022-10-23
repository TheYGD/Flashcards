package pl.jszmidla.flashcards.data.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import pl.jszmidla.flashcards.data.FlashcardSet;
import pl.jszmidla.flashcards.data.dto.flashcard.FlashcardResponse;
import pl.jszmidla.flashcards.data.dto.flashcard.FlashcardSetRequest;
import pl.jszmidla.flashcards.data.dto.flashcard.FlashcardSetResponse;
import pl.jszmidla.flashcards.data.dto.flashcard.edit.FlashcardChangeRequest;
import pl.jszmidla.flashcards.data.dto.flashcard.edit.FlashcardSetChangeResponse;

import java.util.List;

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

    public FlashcardSetResponse entityToResponse(FlashcardSet flashcardSet) {
        FlashcardSetResponse flashcardSetResponse = new FlashcardSetResponse();
        flashcardSetResponse.setId(flashcardSet.getId());
        flashcardSetResponse.setAuthorId(flashcardSet.getAuthor().getId());
        flashcardSetResponse.setAuthorName(flashcardSet.getAuthor().getUsername());
        flashcardSetResponse.setName(flashcardSet.getName());
        flashcardSetResponse.setDescription(flashcardSet.getDescription());
        flashcardSetResponse.setFlashcardCount( flashcardSet.getFlashcards().size() );
        return flashcardSetResponse;
    }


    public FlashcardSetChangeResponse entityToChangeResponse(FlashcardSet set) {
        FlashcardSetChangeResponse response = new FlashcardSetChangeResponse();
        response.setId( set.getId() );
        response.setName( set.getName() );
        response.setDescription( set.getDescription() );

        List<FlashcardResponse> flashcardResponses = set.getFlashcards().stream()
                .map( flashcardMapper::entityToResponse )
                .toList();
        response.setFlashcardList( flashcardResponses );

        return response;
    }
}
