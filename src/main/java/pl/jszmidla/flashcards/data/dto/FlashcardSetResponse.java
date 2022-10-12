package pl.jszmidla.flashcards.data.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FlashcardSetResponse {

    private Long id;
    private Long authorId;
    private String authorName;
    private String name;
    private String description;
}

