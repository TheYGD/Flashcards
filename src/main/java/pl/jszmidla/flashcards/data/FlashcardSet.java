package pl.jszmidla.flashcards.data;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name="flashcard_sets")
public class FlashcardSet extends BaseEntity {

    private String name;
    private String description;

    @ManyToOne
    @JoinColumn(name="author_id")
    private User author;

    @OneToMany
    private List<Flashcard> flashcards;
}
