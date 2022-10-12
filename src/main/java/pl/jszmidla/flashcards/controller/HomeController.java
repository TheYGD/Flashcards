package pl.jszmidla.flashcards.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import pl.jszmidla.flashcards.service.HomeService;

@Controller
@AllArgsConstructor
public class HomeController {

    private HomeService homeService;

    @GetMapping
    public String homePage() {
        return "home/index";
    }
}
