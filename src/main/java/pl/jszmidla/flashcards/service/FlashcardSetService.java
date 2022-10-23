package pl.jszmidla.flashcards.service;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import pl.jszmidla.flashcards.data.Flashcard;
import pl.jszmidla.flashcards.data.FlashcardSet;
import pl.jszmidla.flashcards.data.User;
import pl.jszmidla.flashcards.data.dto.StringResponse;
import pl.jszmidla.flashcards.data.dto.flashcard.FlashcardResponse;
import pl.jszmidla.flashcards.data.dto.flashcard.FlashcardSetRequest;
import pl.jszmidla.flashcards.data.dto.flashcard.FlashcardSetResponse;
import pl.jszmidla.flashcards.data.dto.flashcard.edit.FlashcardChangeRequest;
import pl.jszmidla.flashcards.data.dto.flashcard.edit.FlashcardSetChangeRequest;
import pl.jszmidla.flashcards.data.dto.flashcard.edit.FlashcardSetChangeResponse;
import pl.jszmidla.flashcards.data.exception.ForbiddenException;
import pl.jszmidla.flashcards.data.exception.item.FlashcardSetNotFoundException;
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

    private static final String DELETE_SET_FAIL_MESSAGE = "Error. Try again later.";
    private static final String DELETE_SET_SUCCESS_MESSAGE = "Set deleted.";
    private static final String EDIT_SET_FAIL_MESSAGE = "Error. Try again later.";
    private static final String EDIT_SET_SUCCESS_MESSAGE = "Set edited.";


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

        flashcardSetRepository.save(flashcardSet);

        return flashcardSet.getId();
    }

    @Transactional
    public StringResponse deleteSet(Long setId, User user) {
        FlashcardSet flashcardSet = findById(setId);
        if (!flashcardSet.getAuthor().equals(user)) {
            return new StringResponse( HttpStatus.BAD_REQUEST.value(), DELETE_SET_FAIL_MESSAGE );
        }

        flashcardRepository.deleteAll( flashcardSet.getFlashcards() );
        flashcardSetRepository.removeById(setId);
        return new StringResponse( HttpStatus.OK.value(), DELETE_SET_SUCCESS_MESSAGE );
    }

    public List<FlashcardSet> findSetsByQuery(String query) {
        List<FlashcardSet> setResponseList = flashcardSetRepository.findAllByNameContainingIgnoreCase(query);
        return setResponseList;
    }

    public List<FlashcardResponse> getAllFlashcardFromSet(Long id) {
        FlashcardSet flashcardSet = findById(id);
        List<FlashcardResponse> flashcardResponses = flashcardSet.getFlashcards().stream()
                .map(flashcardMapper::entityToResponse)
                .toList();

        return flashcardResponses;
    }

    public List<FlashcardSet> getUsersSets(User user) {
        return flashcardSetRepository.findAllByAuthor(user);
    }

    public List<FlashcardSetResponse> getUsersSetsResponses(User user) {
        return getUsersSets(user).stream()
                .map(flashcardSetMapper::entityToResponse)
                .toList();
    }

    /** @return true if edited successfully */
    @Transactional
    public StringResponse editSet(FlashcardSetChangeRequest changeRequest, User user) {
        FlashcardSet set = findById(changeRequest.getId());
        if (!set.getAuthor().equals(user)) {
            return new StringResponse( HttpStatus.BAD_REQUEST.value(), EDIT_SET_FAIL_MESSAGE );
        }

        set.setName( changeRequest.getName() );
        set.setDescription( changeRequest.getDescription() );

        for (FlashcardChangeRequest flashcardRequest : changeRequest.getFlashcardChangeList()) {
            // add new flashcard
            if (flashcardRequest.getId() == null) {
                Flashcard flashcard = flashcardMapper.requestToEntity(flashcardRequest.getBody() );
                set.getFlashcards().add(flashcard);
            }

            // edit existing one
            else if (flashcardRequest.getBody() != null) {
                Flashcard flashcard = flashcardMapper.requestToEntity(flashcardRequest.getBody() );
                flashcard.setId( flashcardRequest.getId() );
                int indexOfFlashcard = set.getFlashcards().indexOf( flashcard );
                set.getFlashcards().set(indexOfFlashcard, flashcard); // replace with new content
            }

            // delete flashcard
            else {
                Flashcard flashcard = new Flashcard();
                flashcard.setId( flashcardRequest.getId() );
                set.getFlashcards().remove(flashcard); // remove by id
                flashcardRepository.removeById(flashcard.getId());
            }
        }

        flashcardSetRepository.save(set);

        return new StringResponse( HttpStatus.OK.value(), EDIT_SET_SUCCESS_MESSAGE );
    }

    public FlashcardSetChangeResponse getSetChangeResponse(long id, User user) {
        FlashcardSet set = findById(id);
        if (!set.getAuthor().equals(user)) {
            throw new ForbiddenException();
        }

        FlashcardSetChangeResponse response = flashcardSetMapper.entityToChangeResponse(set);
        return response;
    }
}
