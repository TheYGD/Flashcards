package pl.jszmidla.flashcards.api;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.jszmidla.flashcards.data.User;
import pl.jszmidla.flashcards.data.dto.account.ChangeBioRequest;
import pl.jszmidla.flashcards.data.dto.account.ChangePasswordRequest;
import pl.jszmidla.flashcards.data.dto.StringResponse;
import pl.jszmidla.flashcards.data.mapper.StringResponseMapper;
import pl.jszmidla.flashcards.service.ProfileService;

import javax.validation.Valid;

@RestController
@RequestMapping("/profile")
@AllArgsConstructor
@Validated
public class ProfileApi {

    private ProfileService profileService;
    private StringResponseMapper stringResponseMapper;

    @PutMapping("/bio")
    public ResponseEntity<String> updateBio(@ModelAttribute @Valid ChangeBioRequest changeBioRequest,
                                            @AuthenticationPrincipal User user) {
        StringResponse response = profileService.updateBio(changeBioRequest, user);

        return stringResponseMapper.toResponseEntity(response);
    }

    @PutMapping("/password")
    public ResponseEntity<String> changePassword(@ModelAttribute @Valid ChangePasswordRequest changePasswordRequest,
                                 @AuthenticationPrincipal User user) {
        StringResponse response = profileService.changePassword(changePasswordRequest, user);

        return stringResponseMapper.toResponseEntity(response);
    }

}
