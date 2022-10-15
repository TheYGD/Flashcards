package pl.jszmidla.flashcards.data;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "users_active_sets")
public class UsersActiveSet extends BaseEntity {

    @ManyToOne
    private User user;

    @ManyToOne
    private FlashcardSet flashcardSet;

    /** Comma separated values */
    @Column(name="remembered_flashcards_csv")
    private String rememberedFlashcardsCSV = "";

    /** After the expiration date, rememberedFlashcardsCSV should become empty, to display all cards */
    private LocalDateTime expirationDate;

    private int expirationInterval = 1;


    public void adjustExpirationDate() {
        LocalDateTime now = LocalDateTime.now();

        expirationDate = switch (expirationInterval) {
            case 1 -> now.plusMinutes(20);
            case 2 -> now.plusHours(1);
            case 3 -> now.plusHours(8);
            case 4 -> now.plusDays(1);
            case 5 -> now.plusDays(3);
            case 6 -> now.plusDays(7);
            case 7 -> now.plusDays(14);
            default -> now.plusDays(30);
        };
    }

    public void incrementExpirationInterval() {
        expirationInterval++;
        adjustExpirationDate();
    }

    public void addFlashcardToRemembered(long flashcardId) {
        rememberedFlashcardsCSV += flashcardId + ",";
    }

    public void clearRememberedFlashcards() {
        rememberedFlashcardsCSV = "";
    }
}
