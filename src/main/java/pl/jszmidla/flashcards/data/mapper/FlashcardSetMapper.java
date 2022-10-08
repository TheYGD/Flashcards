package pl.jszmidla.flashcards.data.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import pl.jszmidla.flashcards.data.FlashcardSet;
import pl.jszmidla.flashcards.data.dto.FlashcardSetDto;

@Component
@AllArgsConstructor
public class FlashcardSetMapper {

    private FlashcardMapper flashcardMapper;

    public FlashcardSet dto_to_entity(FlashcardSetDto flashcardSetDto) {
        FlashcardSet flashcardSet = new FlashcardSet();
        flashcardSet.setName(flashcardSetDto.getName());
        flashcardSet.setDescription(flashcardSetDto.getDescription());
        flashcardSet.setAuthorId(flashcardSetDto.getAuthorId());
        flashcardSet.setFlashcards( flashcardSetDto.getFlashcardList().stream()
                .map( flashcardDto -> flashcardMapper.dto_to_entity(flashcardDto) )
                .toList() );

        return flashcardSet;
    }

    public FlashcardSetDto entity_to_dto(FlashcardSet flashcardSet) {
        FlashcardSetDto flashcardSetDto = new FlashcardSetDto();
        flashcardSetDto.setName(flashcardSet.getName());
        flashcardSetDto.setDescription(flashcardSet.getDescription());
        flashcardSetDto.setAuthorId(flashcardSet.getAuthorId());
        flashcardSetDto.setFlashcardList( flashcardSet.getFlashcards().stream()
                .map( flashcard -> flashcardMapper.entity_to_dto(flashcard) )
                .toList() );

        return flashcardSetDto;
    }
}
