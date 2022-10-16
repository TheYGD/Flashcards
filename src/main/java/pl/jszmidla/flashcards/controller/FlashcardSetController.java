package pl.jszmidla.flashcards.controller;

import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.jszmidla.flashcards.data.User;
import pl.jszmidla.flashcards.data.dto.FlashcardResponse;
import pl.jszmidla.flashcards.data.dto.FlashcardSetRequest;
import pl.jszmidla.flashcards.data.dto.FlashcardSetResponse;
import pl.jszmidla.flashcards.data.dto.RememberedAndUnrememberedFlashcardsSplitted;
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

    @GetMapping("/search")
    public String searchForSets(@RequestParam(defaultValue = "") String query, Model model) {
        List<FlashcardSetResponse> setList = flashcardSetService.findSetsByQuery(query);
        model.addAttribute("setList", setList);

        return "flashcard-set/search";
    }

    @GetMapping("/create")
    public String createSetPage() {
        return "flashcard-set/create";
    }

    @PostMapping(value= "/create")
    public String createSetPost(@RequestBody @Valid FlashcardSetRequest flashcardSetRequest,
                                @AuthenticationPrincipal User user) {
        Long flashcardSetId = flashcardSetService.createSet(flashcardSetRequest, user);

        return "redirect:/sets/" + flashcardSetId;
    }

    @DeleteMapping("/delete/{id}")
    public String deleteSet(@PathVariable("id") Long setId, @AuthenticationPrincipal User user) {
        flashcardSetService.deleteSet(setId, user);

        return "redirect:/profile/sets"; // todo dsada
    }
}
