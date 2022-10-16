package pl.jszmidla.flashcards.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.jszmidla.flashcards.data.FlashcardSet;
import pl.jszmidla.flashcards.data.User;
import pl.jszmidla.flashcards.data.dto.FlashcardResponse;
import pl.jszmidla.flashcards.data.dto.FlashcardSetRequest;
import pl.jszmidla.flashcards.data.dto.FlashcardSetResponse;
import pl.jszmidla.flashcards.data.dto.RememberedAndUnrememberedFlashcardsSplitted;
import pl.jszmidla.flashcards.data.exception.item.FlashcardSetNotFoundException;
import pl.jszmidla.flashcards.data.exception.ForbiddenException;
import pl.jszmidla.flashcards.data.mapper.FlashcardMapper;
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
    private FlashcardMapper flashcardMapper;


    public FlashcardSet findById(Long id) {
        return flashcardSetRepository.findById(id).orElseThrow(FlashcardSetNotFoundException::new);
    }
    public FlashcardSetResponse showSetToUser(Long id) {
        FlashcardSet flashcardSet = findById(id);
        return flashcardSetMapper.entityToResponse(flashcardSet);
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
        if (!flashcardSet.getAuthor().equals(user)) {
            throw new ForbiddenException();
        }

        flashcardSetRepository.removeById(setId);
    }

    public List<FlashcardSetResponse> findSetsByQuery(String query) {
        List<FlashcardSetResponse> setResponseList = flashcardSetRepository.findAllByNameContainingIgnoreCase(query).stream()
                .map(flashcardSetMapper::entityToResponse)
                .toList();
        return setResponseList;
    }

    public List<FlashcardResponse> getAllFlashcardFromSet(Long id) {
        FlashcardSet flashcardSet = findById(id);
        List<FlashcardResponse> flashcardResponses = flashcardSet.getFlashcards().stream()
                .map(flashcardMapper::entityToResponse)
                .toList();

        return flashcardResponses;
    }
}
