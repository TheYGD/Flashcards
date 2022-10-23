package pl.jszmidla.flashcards.data;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

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

    @OneToMany(cascade = CascadeType.ALL)
    private List<Flashcard> flashcards;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FlashcardSet flashcardSet = (FlashcardSet) o;
        return Objects.equals(getId(), flashcardSet.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), name, description);
    }
}
