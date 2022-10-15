package pl.jszmidla.flashcards.data;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Entity
@Getter
@Setter
public class UsersRecentSet extends BaseEntity {

    @OneToOne
    private User user;

    @ManyToMany
    @JoinTable(
            name="recent_sets_flashcard_sets",
            joinColumns = @JoinColumn(name="recent_set_id"),
            inverseJoinColumns = @JoinColumn(name="flashcard_set_id"))
    @OrderColumn(name="list_order")
    private List<FlashcardSet> flashcardSetList = new LinkedList<>();
}
