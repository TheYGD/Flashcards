package pl.jszmidla.flashcards.controller;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.jszmidla.flashcards.data.dto.RegisterUserRequest;
import pl.jszmidla.flashcards.service.RegisterService;

import javax.validation.Valid;

@Controller
@AllArgsConstructor
@NoArgsConstructor
@RequestMapping("/register")
public class RegisterController {

    private RegisterService registerService;

    @GetMapping
    public String register_page() {
        return "user/register";
    }

    @PostMapping
    public String register_post(@ModelAttribute @Valid RegisterUserRequest registerUserRequest,
                                BindingResult bindingResult) {
        ObjectError possibleError = registerService.check_if_data_is_valid(registerUserRequest);
        if (possibleError != null) {
            bindingResult.addError(possibleError);
        }
        if (bindingResult.hasErrors() ) {
            return "redirect:/register";
        }

        registerService.register_user(registerUserRequest);

        return "redirect:/";
    }
}
