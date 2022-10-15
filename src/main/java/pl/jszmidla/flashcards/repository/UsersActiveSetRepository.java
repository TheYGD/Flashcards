package pl.jszmidla.flashcards.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.jszmidla.flashcards.data.FlashcardSet;
import pl.jszmidla.flashcards.data.User;
import pl.jszmidla.flashcards.data.UsersActiveSet;

import java.util.Optional;

@Repository
public interface UsersActiveSetRepository extends JpaRepository<UsersActiveSet, Long> {

    UsersActiveSet save(UsersActiveSet usersActiveSet);

    Optional<UsersActiveSet> findByUserAndFlashcardSet(User user, FlashcardSet flashcardSet);
}
