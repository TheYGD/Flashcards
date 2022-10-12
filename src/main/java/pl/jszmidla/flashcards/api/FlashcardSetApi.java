package pl.jszmidla.flashcards.api;

import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import pl.jszmidla.flashcards.data.User;
import pl.jszmidla.flashcards.data.dto.FlashcardResponse;
import pl.jszmidla.flashcards.service.FlashcardService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/flashcard-sets")
@AllArgsConstructor
public class FlashcardSetApi {

    private FlashcardService flashcardService;

    @GetMapping("/{setId}")
    public List<FlashcardResponse> getFlashcardsFromSet(@PathVariable Long setId) {
        List<FlashcardResponse> flashcardList = flashcardService.getFlashcardsFromSet(setId);
        return flashcardList;
    }

    @PostMapping("/{setId}/remembered/{flashcardId}")
    public String markFlashcardAsRemembered(@PathVariable Long setId, @PathVariable Long flashcardId,
                                            @AuthenticationPrincipal User user) {
        flashcardService.markFlashcardAsRemembered(setId, flashcardId, user);
        return "Success";
    }
}
