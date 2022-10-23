package pl.jszmidla.flashcards.data;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Getter
@Setter
@Table(name = "flashcards")
public class Flashcard extends BaseEntity {

    private String front;
    private String back;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Flashcard flashcard = (Flashcard) o;
        return getId().equals( ((Flashcard) o).getId() );
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), front, back);
    }
}
