package pl.jszmidla.flashcards.controller;

import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.jszmidla.flashcards.data.User;
import pl.jszmidla.flashcards.data.dto.flashcard.ActiveFlashcardSetResponse;
import pl.jszmidla.flashcards.service.HomeService;

import java.util.List;

@Controller
@AllArgsConstructor
public class HomeController {

    private HomeService homeService;

    @GetMapping
    public String homePage(Model model, @AuthenticationPrincipal User user) {
        List<ActiveFlashcardSetResponse> recentSetList = homeService.getUsersRecentSets(user);
        model.addAttribute("recentSetList", recentSetList);
        return "home/index";
    }
}
