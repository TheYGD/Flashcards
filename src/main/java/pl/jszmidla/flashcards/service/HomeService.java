package pl.jszmidla.flashcards.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.jszmidla.flashcards.data.FlashcardSet;
import pl.jszmidla.flashcards.data.User;
import pl.jszmidla.flashcards.data.dto.FlashcardSetResponse;
import pl.jszmidla.flashcards.data.mapper.FlashcardSetMapper;

import java.util.List;

@Service
@AllArgsConstructor
public class HomeService {

    private UsersRecentSetService usersRecentSetService;
    private FlashcardSetMapper flashcardSetMapper;


    public List<FlashcardSetResponse> getUsersRecentSets(User user) {
        List<FlashcardSet> recentSetList = usersRecentSetService.getUsersRecentSetListOrNull(user);

        if (recentSetList == null) {
            return null;
        }

        return recentSetList.stream()
                .map(flashcardSetMapper::entityToResponse)
                .toList();
    }

}
