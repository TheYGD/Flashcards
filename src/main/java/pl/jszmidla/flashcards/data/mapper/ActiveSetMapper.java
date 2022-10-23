package pl.jszmidla.flashcards.data.mapper;

import org.springframework.stereotype.Component;
import pl.jszmidla.flashcards.data.ActiveSetState;
import pl.jszmidla.flashcards.data.FlashcardSet;
import pl.jszmidla.flashcards.data.UsersActiveSet;
import pl.jszmidla.flashcards.data.dto.flashcard.ActiveFlashcardSetResponse;

@Component
public class ActiveSetMapper {

    public ActiveFlashcardSetResponse entityToResponse(UsersActiveSet usersActiveSet) {
        ActiveFlashcardSetResponse response = new ActiveFlashcardSetResponse();
        response.setId(usersActiveSet.getFlashcardSet().getId());
        response.setName(usersActiveSet.getFlashcardSet().getName());
        response.setDescription(usersActiveSet.getFlashcardSet().getDescription());
        response.setFlashcardCount((long) usersActiveSet.getFlashcardSet().getFlashcards().size());
        response.setState(ActiveSetState.fromUsersActiveSet(usersActiveSet));

        return response;
    }

    public ActiveFlashcardSetResponse entityToResponseWithoutState(FlashcardSet flashcardSet) {
        ActiveFlashcardSetResponse response = new ActiveFlashcardSetResponse();
        response.setId(flashcardSet.getId());
        response.setName(flashcardSet.getName());
        response.setDescription(flashcardSet.getDescription());
        response.setFlashcardCount((long) flashcardSet.getFlashcards().size());

        return response;
    }
}
