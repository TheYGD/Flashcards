package pl.jszmidla.flashcards.api;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import pl.jszmidla.flashcards.data.User;
import pl.jszmidla.flashcards.data.dto.StringResponse;
import pl.jszmidla.flashcards.data.dto.flashcard.FlashcardSetResponse;
import pl.jszmidla.flashcards.data.dto.flashcard.edit.FlashcardSetChangeRequest;
import pl.jszmidla.flashcards.data.mapper.StringResponseMapper;
import pl.jszmidla.flashcards.service.FlashcardSetService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/your-sets")
@AllArgsConstructor
public class FlashcardSetApi {

    private FlashcardSetService flashcardSetService;
    private StringResponseMapper stringResponseMapper;

    @GetMapping
    public List<FlashcardSetResponse> getOwnSets(@AuthenticationPrincipal User user) {
        List<FlashcardSetResponse> usersSets = flashcardSetService.getUsersSetsResponses(user);
        return usersSets;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSet(@PathVariable Long id, @AuthenticationPrincipal User user) {
        StringResponse response = flashcardSetService.deleteSet(id, user);
        return stringResponseMapper.toResponseEntity(response);
    }

    @PutMapping
    public ResponseEntity<String> editSet(@RequestBody @Valid FlashcardSetChangeRequest flashcardSetChangeRequest,
                                          @AuthenticationPrincipal User user) {
        StringResponse response = flashcardSetService.editSet(flashcardSetChangeRequest, user);
        return stringResponseMapper.toResponseEntity(response);
    }
}