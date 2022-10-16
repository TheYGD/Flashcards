package pl.jszmidla.flashcards.api;

import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import pl.jszmidla.flashcards.data.User;
import pl.jszmidla.flashcards.data.dto.RememberedAndUnrememberedFlashcardsSplitted;
import pl.jszmidla.flashcards.service.UsersActiveSetService;

import java.time.LocalDateTime;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/active-sets")
public class UsersActiveSetApi {

    private UsersActiveSetService usersActiveSetService;


    @GetMapping("/{setId}")
    public RememberedAndUnrememberedFlashcardsSplitted getSplittedFlashcardsFromSet(@PathVariable Long setId,
                                                                            @AuthenticationPrincipal User user) {
        RememberedAndUnrememberedFlashcardsSplitted flashcardList =
                usersActiveSetService.showSplittedFlashcardsToUser(setId, user);
        return flashcardList;
    }

    @GetMapping(value = "/{setId}/expire")
    public LocalDateTime getSetExpirationDate(@PathVariable Long setId, @AuthenticationPrincipal User user) {
        LocalDateTime expirationDate = usersActiveSetService.getSetExpirationDate(setId, user);
        return expirationDate;
    }

    @PostMapping("/{setId}/reload")
    public String reloadSetSooner(@PathVariable Long setId, @AuthenticationPrincipal User user) {
        usersActiveSetService.reloadSetSooner(setId, user);
        return "Success";
    }

    @PostMapping("/{setId}/remembered/{flashcardId}")
    public String markFlashcardFromSetAsRemembered(@PathVariable Long setId, @PathVariable Long flashcardId,
                                                   @AuthenticationPrincipal User user) {
        usersActiveSetService.markFlashcardAsRemembered(setId, flashcardId, user);
        return "Success";
    }

    @PostMapping("/{setId}/completed")
    public String markSetAsCompleted(@PathVariable Long setId, @AuthenticationPrincipal User user) {
        usersActiveSetService.markSetAsCompleted(setId, user);
        return "Success";
    }
}
