package pl.jszmidla.flashcards.controller;

import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.jszmidla.flashcards.data.User;
import pl.jszmidla.flashcards.data.dto.account.RegisterUserRequest;
import pl.jszmidla.flashcards.service.UserService;

import javax.validation.Valid;

@Controller
@AllArgsConstructor
@RequestMapping
public class UserController {

    private UserService registerService;


    @GetMapping("/register")
    public String registerPageAnonymous(@AuthenticationPrincipal User user) {
        if (user != null) {
            return "redirect:/";
        }

        return "user/register";
    }

    @PostMapping("/register")
    public String registerPost(@ModelAttribute @Valid RegisterUserRequest registerUserRequest,
                               BindingResult bindingResult) {
        ObjectError possibleError = registerService.checkIfDataIsValid(registerUserRequest);
        if (possibleError != null) {
            bindingResult.addError(possibleError);
        }
        if (bindingResult.hasErrors() ) {
            return "redirect:/register";
        }

        registerService.registerUser(registerUserRequest);

        return "redirect:/";
    }

    @GetMapping("/login")
    public String loginPage(@AuthenticationPrincipal User user) {
        if (user != null) {
            return "redirect:/";
        }

        return "user/login";
    }

    @GetMapping("/change-password")
    public String changePasswordPage() {
        return "user/change-password";
    }

}
