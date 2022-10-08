package pl.jszmidla.flashcards.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.jszmidla.flashcards.data.FlashcardSet;
import pl.jszmidla.flashcards.data.User;
import pl.jszmidla.flashcards.data.dto.FlashcardSetDto;
import pl.jszmidla.flashcards.data.exception.FlashcardSetNotFoundException;
import pl.jszmidla.flashcards.data.exception.ForbiddenException;
import pl.jszmidla.flashcards.data.mapper.FlashcardSetMapper;
import pl.jszmidla.flashcards.repository.FlashcardSetRepository;


@Service
@AllArgsConstructor
public class FlashcardSetService {

    private FlashcardSetRepository flashcardSetRepository;
    private FlashcardSetMapper flashcardSetMapper;

    public FlashcardSet find_by_id(Long id) {
        return flashcardSetRepository.findById(id).orElseThrow(FlashcardSetNotFoundException::new);
    }

    public Long create_set(FlashcardSetDto flashcardSetDto, User user) {
        FlashcardSet flashcardSet = flashcardSetMapper.dto_to_entity(flashcardSetDto);
        flashcardSet.setAuthorId(user.getId());
        flashcardSetRepository.save(flashcardSet);

        return flashcardSet.getId();
    }

    public void delete_set(Long setId, User user) {
        FlashcardSet flashcardSet = find_by_id(setId);
        if (!flashcardSet.getAuthorId().equals(user.getId())) {
            throw new ForbiddenException();
        }

        flashcardSetRepository.removeById(setId);
    }
}
