package pl.jszmidla.flashcards.service;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import pl.jszmidla.flashcards.data.User;
import pl.jszmidla.flashcards.data.dto.ChangePasswordRequest;
import pl.jszmidla.flashcards.repository.UserRepository;

@Service
@AllArgsConstructor
public class ChangePasswordService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public String change_password(ChangePasswordRequest changePasswordRequest, User user) {
        String successfulMessage = "Password changed successfully.";

        String encodedPassword = passwordEncoder.encode(changePasswordRequest.getNewPassword());
        user.setPassword(encodedPassword);
        userRepository.save(user);

        return successfulMessage;
    }

    /**
     * @return blank string if data is valid, if it is invalid, returns the cause message
     */
    public String check_if_data_is_invalid(BindingResult bindingResult, ChangePasswordRequest changePasswordRequest,
                                         User user) {

        String invalidOldPasswordMessage = "Incorrect password.";
        String invalidNewPasswordMessage = "New password is invalid.";


        if (!is_password_correct(user, changePasswordRequest)) { // binding result's check for old password is redundant
            return invalidOldPasswordMessage;
        }
        if (bindingResult.hasFieldErrors("newPassword") ) {
            return invalidNewPasswordMessage;
        }

        return "";
    }

    private boolean is_password_correct(User user, ChangePasswordRequest changePasswordRequest) {
        return passwordEncoder.matches(changePasswordRequest.getOldPassword(), user.getPassword());
    }
}
