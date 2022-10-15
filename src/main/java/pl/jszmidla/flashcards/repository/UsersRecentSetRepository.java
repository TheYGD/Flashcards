package pl.jszmidla.flashcards.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.jszmidla.flashcards.data.User;
import pl.jszmidla.flashcards.data.UsersRecentSet;

import java.util.Optional;

@Repository
public interface UsersRecentSetRepository extends JpaRepository<UsersRecentSet, Long> {

    UsersRecentSet save(UsersRecentSet usersRecentSet);

    Optional<UsersRecentSet> findByUser(User user);
}
