package pl.jszmidla.flashcards.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import pl.jszmidla.flashcards.service.ProfileService;

@Controller
@AllArgsConstructor
public class ProfileController {

    private ProfileService profileService;


}
