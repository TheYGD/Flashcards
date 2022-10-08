package pl.jszmidla.flashcards.controller;

import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.jszmidla.flashcards.data.FlashcardSet;
import pl.jszmidla.flashcards.data.User;
import pl.jszmidla.flashcards.data.dto.FlashcardSetDto;
import pl.jszmidla.flashcards.service.FlashcardSetService;

import javax.validation.Valid;

@Controller
@AllArgsConstructor
@RequestMapping("/sets")
public class FlashcardSetController {

    private FlashcardSetService flashcardSetService;

    @GetMapping("/{id}")
    public String show_by_id(@PathVariable("id") Long setId, Model model) {
        FlashcardSet flashcardSet = flashcardSetService.find_by_id(setId);
        model.addAttribute("flashcardSet", flashcardSet);

        return "flashcard-set/show";
    }

    @GetMapping("/create")
    public String create_set_page() {
        return "flashcard-set/create";
    }

    @PostMapping("/create")
    public String create_set_post(@ModelAttribute @Valid FlashcardSetDto flashcardSetDto,
                                  @AuthenticationPrincipal User user) {
        Long flashcardSetId = flashcardSetService.create_set(flashcardSetDto, user);

        return "redirect:/sets/" + flashcardSetId;
    }

    @DeleteMapping("/delete/{id}")
    public String delete_set(@PathVariable("id") Long setId, @AuthenticationPrincipal User user) {
        flashcardSetService.delete_set(setId, user);

        return "redirect:/profile/sets"; // todo dsada
    }
}
