package pl.jszmidla.flashcards.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import pl.jszmidla.flashcards.data.dto.ChangePasswordRequest;
import pl.jszmidla.flashcards.service.ChangePasswordService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChangePasswordControllerTest {

    @Mock
    ChangePasswordService changePasswordService;
    @InjectMocks
    ChangePasswordController changePasswordController;


    @Test
    void change_password_successfully() {
        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest();
        String expectedResponse = "success";
        BindingResult bindingResult = Mockito.mock(BindingResult.class);
        when( changePasswordService.change_password(any(), any()) ).thenReturn( expectedResponse );
        when( changePasswordService.check_if_data_is_invalid(any(), any(), any()) ).thenReturn("");

        ResponseEntity<String> actualResponse = changePasswordController.change_password(changePasswordRequest,
                                                                                         bindingResult, null);

        assertEquals( HttpStatus.OK, actualResponse.getStatusCode() );
        assertEquals( expectedResponse, actualResponse.getBody() );
    }

    @Test
    void change_password_failed() {
        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest();
        String expectedResponse = "error";
        BindingResult bindingResult = Mockito.mock(BindingResult.class);
        when( changePasswordService.check_if_data_is_invalid(any(), any(), any()) ).thenReturn(expectedResponse);

        ResponseEntity<String> actualResponse = changePasswordController.change_password(changePasswordRequest,
                bindingResult, null);

        assertEquals( HttpStatus.BAD_REQUEST, actualResponse.getStatusCode() );
        assertEquals( expectedResponse, actualResponse.getBody() );
    }
}