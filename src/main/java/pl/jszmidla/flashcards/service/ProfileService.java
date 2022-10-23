package pl.jszmidla.flashcards.service;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.jszmidla.flashcards.data.FlashcardSet;
import pl.jszmidla.flashcards.data.User;
import pl.jszmidla.flashcards.data.dto.*;
import pl.jszmidla.flashcards.data.dto.account.ChangeBioRequest;
import pl.jszmidla.flashcards.data.dto.account.ChangePasswordRequest;
import pl.jszmidla.flashcards.data.dto.flashcard.ActiveFlashcardSetResponse;
import pl.jszmidla.flashcards.data.dto.flashcard.FlashcardSetResponse;
import pl.jszmidla.flashcards.data.exception.item.UserNotFoundException;
import pl.jszmidla.flashcards.repository.UserRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class ProfileService {

    private UserRepository userRepository;
    private FlashcardSetService flashcardSetService;
    private UsersActiveSetService usersActiveSetService;
    private final PasswordEncoder passwordEncoder;

    private static final String SUCCESS_BIO_MESSAGE = "Changed bio successfully.";
    private static final String PASSWORD_SUCCESS_MESSAGE = "Changed password successfully.";
    private static final String PASSWORD_WRONG_FAIL_MESSAGE = "Given password is wrong.";
    private static final String PASSWORD_SAME_FAIL_MESSAGE = "New password cannot be the same as the old one.";


    public List<ActiveFlashcardSetResponse> getUsersActiveSets(User profileUser, User activeUser) {
        List<FlashcardSet> flashcardSetList = flashcardSetService.getUsersSets(profileUser);
        List<ActiveFlashcardSetResponse> responses = usersActiveSetService.getActiveSetsResponsesFromSets(
                flashcardSetList, activeUser);
        return responses;
    }

    public User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);
    }

    public StringResponse updateBio(ChangeBioRequest changeBioRequest, User user) {
        user.setBio(changeBioRequest.getBio());
        userRepository.save(user);
        return new StringResponse(HttpStatus.OK.value(), SUCCESS_BIO_MESSAGE);
    }

    public StringResponse changePassword(ChangePasswordRequest changePasswordRequest, User user) {
        // wrong password
        if (!passwordEncoder.matches( changePasswordRequest.getOldPassword(), user.getPassword() )) {
            return new StringResponse( HttpStatus.BAD_REQUEST.value(), PASSWORD_WRONG_FAIL_MESSAGE);
        }

        // passwords are same
        if (changePasswordRequest.getNewPassword().equals( changePasswordRequest.getOldPassword() )) {
            return new StringResponse( HttpStatus.BAD_REQUEST.value(), PASSWORD_SAME_FAIL_MESSAGE);
        }

        String encodedPassword = passwordEncoder.encode(changePasswordRequest.getNewPassword());
        user.setPassword( encodedPassword );
        userRepository.save(user);

        return new StringResponse( HttpStatus.OK.value(), PASSWORD_SUCCESS_MESSAGE);
    }
}
