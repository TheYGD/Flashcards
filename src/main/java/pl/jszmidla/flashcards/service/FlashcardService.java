package pl.jszmidla.flashcards.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.jszmidla.flashcards.data.FlashcardSet;
import pl.jszmidla.flashcards.data.User;
import pl.jszmidla.flashcards.data.dto.FlashcardResponse;
import pl.jszmidla.flashcards.data.mapper.FlashcardMapper;

import java.util.List;

@Service
@AllArgsConstructor
public class FlashcardService {

    private FlashcardSetService flashcardSetService;
    private FlashcardMapper flashcardMapper;

    public List<FlashcardResponse> getFlashcardsFromSet(Long id) {
        FlashcardSet set = flashcardSetService.findById(id);
        List<FlashcardResponse> flashcardList = set.getFlashcards().stream()
                .map(flashcardMapper::entityToResponse)
                .toList();

        return flashcardList;
    }

    public void markFlashcardAsRemembered(Long setId, Long flashcardId, User user) {

    }
}
