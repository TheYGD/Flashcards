package pl.jszmidla.flashcards.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.jszmidla.flashcards.data.Flashcard;


@Repository
public interface FlashcardRepository extends JpaRepository<Flashcard, Long> {

    Flashcard save(Flashcard flashcard);
    void removeById(Long id);
}
