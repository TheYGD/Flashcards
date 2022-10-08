package pl.jszmidla.flashcards.data;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name="flashcard_sets")
public class FlashcardSet extends BaseEntity {

    private String name;
    private String description;
    private Long authorId;
    @OneToMany
    private List<Flashcard> flashcards;
}
