package pl.jszmidla.flashcards.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.jszmidla.flashcards.data.FlashcardSet;
import pl.jszmidla.flashcards.data.User;
import pl.jszmidla.flashcards.data.dto.FlashcardSetRequest;
import pl.jszmidla.flashcards.data.exception.FlashcardSetNotFoundException;
import pl.jszmidla.flashcards.data.exception.ForbiddenException;
import pl.jszmidla.flashcards.data.mapper.FlashcardSetMapper;
import pl.jszmidla.flashcards.repository.FlashcardRepository;
import pl.jszmidla.flashcards.repository.FlashcardSetRepository;

import javax.transaction.Transactional;
import java.util.List;


@Service
@AllArgsConstructor
public class FlashcardSetService {

    private FlashcardSetRepository flashcardSetRepository;
    private FlashcardRepository flashcardRepository;
    private FlashcardSetMapper flashcardSetMapper;

    public FlashcardSet findById(Long id) {
        return flashcardSetRepository.findById(id).orElseThrow(FlashcardSetNotFoundException::new);
    }

    @Transactional
    public Long createSet(FlashcardSetRequest flashcardSetRequest, User user) {
        FlashcardSet flashcardSet = flashcardSetMapper.requestToEntity(flashcardSetRequest);
        flashcardSet.setAuthor(user);

        flashcardRepository.saveAll(flashcardSet.getFlashcards());
        flashcardSetRepository.save(flashcardSet);

        return flashcardSet.getId();
    }

    public void deleteSet(Long setId, User user) {
        FlashcardSet flashcardSet = findById(setId);
        if (!flashcardSet.getAuthor().getId().equals(user.getId())) {
            throw new ForbiddenException();
        }

        flashcardSetRepository.removeById(setId);
    }

    public List<FlashcardSet> findSetsByQuery(String query) {
        List<FlashcardSet> setList = flashcardSetRepository.findAllByNameContaining(query);
        return setList;
    }
}
