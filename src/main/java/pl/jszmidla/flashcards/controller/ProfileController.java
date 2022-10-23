package pl.jszmidla.flashcards.controller;

import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.jszmidla.flashcards.data.User;
import pl.jszmidla.flashcards.data.dto.flashcard.ActiveFlashcardSetResponse;
import pl.jszmidla.flashcards.data.dto.flashcard.FlashcardSetResponse;
import pl.jszmidla.flashcards.service.ProfileService;

import java.util.List;

@Controller
@AllArgsConstructor
public class ProfileController {

    private ProfileService profileService;


    @GetMapping("/profiles/{id}")
    public String showUsersProfile(@PathVariable Long id, Model model, @AuthenticationPrincipal User activeUser) {
        User profileUser = profileService.getUser(id);
        List<ActiveFlashcardSetResponse> usersSets = profileService.getUsersActiveSets(profileUser, activeUser);
        model.addAttribute("user", profileUser);
        model.addAttribute("usersSetList", usersSets);

        return "profiles/show";
    }

    @GetMapping("/profile")
    public String showOwnProfile(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user", user);
        return "profiles/own";
    }
}
