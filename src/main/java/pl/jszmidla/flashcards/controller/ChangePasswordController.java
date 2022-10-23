package pl.jszmidla.flashcards.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.jszmidla.flashcards.data.User;
import pl.jszmidla.flashcards.data.dto.account.ChangePasswordRequest;
import pl.jszmidla.flashcards.service.ChangePasswordService;

import javax.validation.Valid;

@Controller
@AllArgsConstructor
@RequestMapping("/login")
public class ChangePasswordController {

    private ChangePasswordService changePasswordService;

    @PostMapping
    @ResponseBody
    public ResponseEntity<String> changePassword(@RequestBody @Valid ChangePasswordRequest changePasswordRequest,
                                                 BindingResult bindingResult, @AuthenticationPrincipal User user) {
        String possibleError = changePasswordService.checkIfDataIsInvalid(bindingResult, changePasswordRequest, user);
        if (!possibleError.isBlank()) {
            return ResponseEntity.badRequest().body(possibleError);
        }

        String responseBody = changePasswordService.changePassword(changePasswordRequest, user);
        return ResponseEntity.ok(responseBody);
    }
}
