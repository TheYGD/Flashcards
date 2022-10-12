package pl.jszmidla.flashcards.data.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class ForbiddenException extends RuntimeException {

    static String MESSAGE = "Operation forbidden!";

    public ForbiddenException() {
        super(MESSAGE);
    }
}
