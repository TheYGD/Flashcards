package pl.jszmidla.flashcards.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.jszmidla.flashcards.data.FlashcardSet;

import java.util.Optional;

@Repository
public interface FlashcardSetRepository extends JpaRepository<FlashcardSet, Long> {

    FlashcardSet save(FlashcardSet flashcardSet);
    Optional<FlashcardSet> findById(Long id);
    void removeById(Long id);
}
