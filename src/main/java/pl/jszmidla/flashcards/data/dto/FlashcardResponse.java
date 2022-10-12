package pl.jszmidla.flashcards.data.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FlashcardResponse {

    private Long id;
    private String front;
    private String back;
}

