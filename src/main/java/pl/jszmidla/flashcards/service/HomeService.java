package pl.jszmidla.flashcards.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.jszmidla.flashcards.data.FlashcardSet;
import pl.jszmidla.flashcards.data.User;
import pl.jszmidla.flashcards.data.UsersActiveSet;
import pl.jszmidla.flashcards.data.dto.flashcard.ActiveFlashcardSetResponse;
import pl.jszmidla.flashcards.data.mapper.ActiveSetMapper;

import java.util.List;

@Service
@AllArgsConstructor
public class HomeService {

    private UsersRecentSetService usersRecentSetService;
    private UsersActiveSetService usersActiveSetService;
    private ActiveSetMapper activeSetMapper;


    public List<ActiveFlashcardSetResponse> getUsersRecentSets(User user) {
        List<FlashcardSet> recentSetList = usersRecentSetService.getUsersRecentSetListOrNull(user);
        if (recentSetList == null) {
            return null;
        }

        List<UsersActiveSet> activeSetList = usersActiveSetService.getActiveSetsOutOfSets(user, recentSetList);

        return activeSetList.stream()
                .map(activeSetMapper::entityToResponse)
                .toList();
    }

}
