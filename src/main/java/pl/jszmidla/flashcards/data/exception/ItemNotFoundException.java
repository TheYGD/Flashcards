package pl.jszmidla.flashcards.data.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ItemNotFoundException extends RuntimeException {

    static String TEMPLATE = "%s not found!";

    public ItemNotFoundException(String itemName) {
        super(TEMPLATE.formatted(itemName));
    }
}
