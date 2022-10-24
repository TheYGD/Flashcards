package pl.jszmidla.flashcards.aop;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import pl.jszmidla.flashcards.data.User;

@ControllerAdvice
public class AdviceController {

    @ModelAttribute("username")
    public String addUsername(@AuthenticationPrincipal User user) {
        return user != null ? user.getUsername() : null;
    }

    @ExceptionHandler(RuntimeException.class)
    public String exceptionPage(RuntimeException exception, Model model) {
        model.addAttribute("exception", exception);
        return "exception/show";
    }
}
