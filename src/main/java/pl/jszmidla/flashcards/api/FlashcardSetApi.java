package pl.jszmidla.flashcards.api;

import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import pl.jszmidla.flashcards.data.User;
import pl.jszmidla.flashcards.data.dto.FlashcardResponse;
import pl.jszmidla.flashcards.data.dto.RememberedAndUnrememberedFlashcardsSplitted;
import pl.jszmidla.flashcards.service.FlashcardSetService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/flashcard-sets")
@AllArgsConstructor
public class FlashcardSetApi {

    private FlashcardSetService flashcardSetService;

    @GetMapping("/{setId}")
    public RememberedAndUnrememberedFlashcardsSplitted getFlashcardsFromSet(@PathVariable Long setId,
                                                                            @AuthenticationPrincipal User user) {
        RememberedAndUnrememberedFlashcardsSplitted flashcardList =
                flashcardSetService.getSplittedFlashcardsFromSet(setId, user);
        return flashcardList;
    }

    @GetMapping(value = "/{setId}/expire")
    public LocalDateTime getSetExpirationDate(@PathVariable Long setId, @AuthenticationPrincipal User user) {
        LocalDateTime expirationDate = flashcardSetService.getSetExpirationDate(setId, user);
        return expirationDate;
    }

    @PostMapping("/{setId}/remembered/{flashcardId}")
    public String markFlashcardFromSetAsRemembered(@PathVariable Long setId, @PathVariable Long flashcardId,
                                            @AuthenticationPrincipal User user) {
        flashcardSetService.markFlashcardFromSetAsRemembered(setId, flashcardId, user);
        return "Success";
    }

    @PostMapping("/{setId}/completed")
    public String markSetAsCompleted(@PathVariable Long setId, @AuthenticationPrincipal User user) {
        flashcardSetService.markSetAsCompleted(setId, user);
        return "Success";
    }

    @PostMapping("/{setId}/reload")
    public String reloadSetSooner(@PathVariable Long setId, @AuthenticationPrincipal User user) {
        flashcardSetService.reloadSetSooner(setId, user);
        return "Success";
    }

}
