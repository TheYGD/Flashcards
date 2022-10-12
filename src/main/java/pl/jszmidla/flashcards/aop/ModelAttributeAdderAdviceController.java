package pl.jszmidla.flashcards.aop;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import pl.jszmidla.flashcards.data.User;

@ControllerAdvice
public class ModelAttributeAdderAdviceController {

    @ModelAttribute("username")
    public String addUsername(@AuthenticationPrincipal User user) {
        return user != null ? user.getUsername() : null;
    }

}
