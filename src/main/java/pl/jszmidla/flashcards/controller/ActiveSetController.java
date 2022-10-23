package pl.jszmidla.flashcards.controller;

import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.jszmidla.flashcards.data.User;
import pl.jszmidla.flashcards.data.dto.flashcard.ActiveFlashcardSetResponse;
import pl.jszmidla.flashcards.service.UsersActiveSetService;

import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/sets")
public class ActiveSetController {

    private UsersActiveSetService usersActiveSetService;

    @GetMapping("/search")
    public String searchForSets(@RequestParam(defaultValue = "") String query, @AuthenticationPrincipal User user,
                                Model model) {
        List<ActiveFlashcardSetResponse> setList = usersActiveSetService.findSetsByQuery(query, user);
        model.addAttribute("setList", setList);

        return "flashcard-set/search";
    }
}
