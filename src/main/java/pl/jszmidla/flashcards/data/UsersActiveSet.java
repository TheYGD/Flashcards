package pl.jszmidla.flashcards.data;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

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
    private int rememberedFlashcardsCount = 0;

    /** after this date, all cards will become unremembered */
    private LocalDateTime reloadDate = MAX_DATE;

    private int reloadInterval = 0;

    public static LocalDateTime MAX_DATE = LocalDateTime.of(9999, 12, 31, 23, 59, 59);


    public void setUpReloadDate() {
        LocalDateTime now = LocalDateTime.now();

        reloadDate = switch (reloadInterval) {
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

    private void deleteReloadDate() {
        reloadDate = MAX_DATE;
    }

    private void completeSet() {
        reloadInterval++;
        setUpReloadDate();
    }

    public void addFlashcardToRemembered(long flashcardId) {
        rememberedFlashcardsCSV += flashcardId + ",";
        rememberedFlashcardsCount++;
        // if it looks like set is completed, we have to check if all the ids are unique and flashcards with those ids
        //    belong to this set, because it's possible to trick this implementation with doing fake requests, but I
        //    think storing remembered ids as csvString and doing this validation is still very efficient
        if (rememberedFlashcardsCount == flashcardSet.getFlashcards().size()) {
            boolean wereDuplicatesOrInvalidIdsPresent = deletePossibleDuplicatesAndWrongIds();
            if (!wereDuplicatesOrInvalidIdsPresent) {
                completeSet();
            }
        }
    }

    /** @return true if duplicates or invalid ids were present  */
    private boolean deletePossibleDuplicatesAndWrongIds() {
        Set<Long> flashcardsInSetIds = flashcardSet.getFlashcards().stream()
                .map(Flashcard::getId)
                .collect(Collectors.toSet());

        Set<String> correctUniqueIds = Arrays.stream(rememberedFlashcardsCSV.split(","))
                .filter( id -> flashcardsInSetIds.contains(Long.valueOf(id)) ) // if someone posted request with id outside this set
                .collect(Collectors.toSet());

        if (correctUniqueIds.size() != rememberedFlashcardsCount) {
            StringBuilder sb = new StringBuilder();
            for (String id : correctUniqueIds) {
                sb.append(id);
                sb.append(",");
            }
            rememberedFlashcardsCSV = sb.toString();
            rememberedFlashcardsCount = correctUniqueIds.size();
            return true;
        }

        return false;
    }

    public void clearRememberedFlashcards() {
        rememberedFlashcardsCSV = "";
        rememberedFlashcardsCount = 0;
        deleteReloadDate();
    }

    public boolean isAfterReloadTime() {
        return getReloadDate().isBefore( LocalDateTime.now() );
    }
}
