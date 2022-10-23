package pl.jszmidla.flashcards.controller;

import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.jszmidla.flashcards.data.User;
import pl.jszmidla.flashcards.data.dto.flashcard.FlashcardResponse;
import pl.jszmidla.flashcards.data.dto.flashcard.FlashcardSetRequest;
import pl.jszmidla.flashcards.data.dto.flashcard.FlashcardSetResponse;
import pl.jszmidla.flashcards.data.dto.flashcard.edit.FlashcardSetChangeResponse;
import pl.jszmidla.flashcards.service.FlashcardSetService;

import javax.validation.Valid;
import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/sets")
public class FlashcardSetController {

    private FlashcardSetService flashcardSetService;

    @GetMapping("/{id}")
    public String showSet(@PathVariable("id") Long setId, Model model, @AuthenticationPrincipal User user) {
        FlashcardSetResponse flashcardSet = flashcardSetService.showSetToUser(setId);
        List<FlashcardResponse> flashcards = flashcardSetService.getAllFlashcardFromSet(setId);

        model.addAttribute("set", flashcardSet);
        model.addAttribute("flashcards", flashcards);

        return "flashcard-set/show";
    }

    @GetMapping("/learn/{id}")
    public String learnSet(@PathVariable("id") Long setId, Model model) {
        FlashcardSetResponse flashcardSet = flashcardSetService.showSetToUser(setId);
        model.addAttribute("set", flashcardSet);

        return "flashcard-set/learn";
    }

    @GetMapping("/create")
    public String createSetPage() {
        return "flashcard-set/create-edit";
    }

    @PostMapping("/create")
    public String createSetPost(@RequestBody @Valid FlashcardSetRequest flashcardSetRequest,
                                @AuthenticationPrincipal User user) {
        Long flashcardSetId = flashcardSetService.createSet(flashcardSetRequest, user);

        return "redirect:/sets/" + flashcardSetId;
    }

    @GetMapping("/{id}/edit")
    public String editPage(@PathVariable long id, @AuthenticationPrincipal User user, Model model) {
        FlashcardSetChangeResponse set = flashcardSetService.getSetChangeResponse(id, user);
        model.addAttribute( "set", set );

        return "flashcard-set/create-edit";
    }
}
