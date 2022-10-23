package pl.jszmidla.flashcards.data.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class StringResponse {
    private int statusCode;
    private String body;
}
