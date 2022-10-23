package pl.jszmidla.flashcards.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import pl.jszmidla.flashcards.data.User;
import pl.jszmidla.flashcards.data.dto.account.ChangePasswordRequest;
import pl.jszmidla.flashcards.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChangePasswordServiceTest {

    @Mock
    UserRepository userRepository;
    @Mock
    PasswordEncoder passwordEncoder;
    @InjectMocks
    ChangePasswordService changePasswordService;


    @Test
    void changePasswordSuccessfully() {
        String message = "Password changed successfully.";
        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest();
        changePasswordRequest.setNewPassword("pass");
        User user = new User();
        when( userRepository.save(user) ).thenReturn(user);
        when( passwordEncoder.encode(any()) ).thenReturn(changePasswordRequest.getNewPassword());

        String response = changePasswordService.changePassword(changePasswordRequest, user);

        assertEquals(message, response);
        assertEquals(changePasswordRequest.getNewPassword(), user.getPassword());
    }

    @Test
    void givenValidData() {
        String expected = "";
        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest();
        BindingResult bindingResult = Mockito.mock(BindingResult.class);
        when( bindingResult.hasFieldErrors("newPassword") ).thenReturn(false);
        when( passwordEncoder.matches(any(), any()) ).thenReturn(true);

        String actual = changePasswordService.checkIfDataIsInvalid(bindingResult, changePasswordRequest, new User());

        assertEquals(expected, actual);
    }

    @Test
    void givenInvalidOldPassword() {
        String expected = "Incorrect password.";
        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest();
        BindingResult bindingResult = Mockito.mock(BindingResult.class);
        when( passwordEncoder.matches(any(), any()) ).thenReturn(false);

        String actual = changePasswordService.checkIfDataIsInvalid(bindingResult, changePasswordRequest, new User());

        assertEquals(expected, actual);
    }

    @Test
    void givenInvalidNewPassword() {
        String expected = "New password is invalid.";
        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest();
        BindingResult bindingResult = Mockito.mock(BindingResult.class);
        when( bindingResult.hasFieldErrors("newPassword") ).thenReturn(true);
        when( passwordEncoder.matches(any(), any()) ).thenReturn(true);

        String actual = changePasswordService.checkIfDataIsInvalid(bindingResult, changePasswordRequest, new User());

        assertEquals(expected, actual);
    }
}