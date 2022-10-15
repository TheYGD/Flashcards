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
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class FlashcardSetService {

    private FlashcardSetRepository flashcardSetRepository;
    private FlashcardRepository flashcardRepository;
    private FlashcardSetMapper flashcardSetMapper;
    private FlashcardMapper flashcardMapper;
    private UsersActiveSetService usersActiveSetService;
    private UsersRecentSetService usersRecentSetService;


    public FlashcardSet findById(Long id) {
        return flashcardSetRepository.findById(id).orElseThrow(FlashcardSetNotFoundException::new);
    }
    public FlashcardSetResponse showSetToUser(Long id, User user) {
        FlashcardSet flashcardSet = findById(id);
        usersRecentSetService.addRecentSetIfLogged(user, flashcardSet);
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

    public RememberedAndUnrememberedFlashcardsSplitted getSplittedFlashcardsFromSet(Long id, User user) {
        FlashcardSet flashcardSet = findById(id);
        Set<Long> rememberedFlashcardsIds = usersActiveSetService.getRememberedFlashcardsIds(flashcardSet, user);

        Map<Boolean, List<FlashcardResponse>> flashcardsSplittedMap = flashcardSet.getFlashcards().stream()
                .map(flashcardMapper::entityToResponse)
                .collect(Collectors.groupingBy( flashcard -> rememberedFlashcardsIds.contains( flashcard.getId() ) ));

        RememberedAndUnrememberedFlashcardsSplitted flashcardsSplitted = new RememberedAndUnrememberedFlashcardsSplitted();
        flashcardsSplitted.setRememberedFlashcardList( flashcardsSplittedMap.get(true) );
        flashcardsSplitted.setUnrememberedFlashcardList( flashcardsSplittedMap.get(false) );

        return flashcardsSplitted;
    }

    public void markFlashcardFromSetAsRemembered(long setId, long flashcardId, User user) {
        FlashcardSet flashcardSet = findById(setId);
        usersActiveSetService.markFlashcardAsRemembered(flashcardSet, flashcardId, user);
    }

    public void markSetAsCompleted(long setId, User user) {
        FlashcardSet flashcardSet = findById(setId);
        usersActiveSetService.markFlashcardAsCompleted(flashcardSet, user);
    }

    /**
     * If user wants to run set sooner than it would reload itself
     */
    public void reloadSetSooner(long setId, User user) {
        FlashcardSet flashcardSet = findById(setId);
        usersActiveSetService.reloadSetSooner(flashcardSet, user);
    }

    public LocalDateTime getSetExpirationDate(Long setId, User user) {
        FlashcardSet flashcardSet = findById(setId);
        LocalDateTime expirationDate = usersActiveSetService.getSetExpirationDate(flashcardSet, user);
        return expirationDate;
    }
}
