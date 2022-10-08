package pl.jszmidla.flashcards.data;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@Table(name = "flashcards")
public class Flashcard extends BaseEntity {

    private String front;
    private String back;
}
