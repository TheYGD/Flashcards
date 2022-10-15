package pl.jszmidla.flashcards.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.jszmidla.flashcards.data.FlashcardSet;
import pl.jszmidla.flashcards.data.User;
import pl.jszmidla.flashcards.data.UsersRecentSet;
import pl.jszmidla.flashcards.data.exception.item.UsersRecentSetNotFoundException;
import pl.jszmidla.flashcards.repository.UsersRecentSetRepository;
import pl.jszmidla.flashcards.service.interfaces.EntityConnectedWithUserService;
import pl.jszmidla.flashcards.service.interfaces.UserEntityRegister;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UsersRecentSetService implements EntityConnectedWithUserService {

    private UsersRecentSetRepository usersRecentSetRepository;

    @Override
    @Autowired
    public void registerThisService(UserEntityRegister connector) {
        connector.registerService(this);
    }

    @Override
    public void createConnectedEntity(User user) {
        UsersRecentSet usersRecentSet = new UsersRecentSet();
        usersRecentSet.setUser(user);
        usersRecentSetRepository.save(usersRecentSet);
    }

    public UsersRecentSet findByUser(User user) {
        return usersRecentSetRepository.findByUser(user)
                .orElseThrow(UsersRecentSetNotFoundException::new);
    }

    public Optional<UsersRecentSet> findOptionalByUser(User user) {
        return usersRecentSetRepository.findByUser(user);
    }

    public List<FlashcardSet> getUsersRecentSetListOrNull(User user) {
        Optional<UsersRecentSet> usersRecentSetOptional = findOptionalByUser(user);
        if (usersRecentSetOptional.isEmpty()) {
            return null;
        }

        UsersRecentSet usersRecentSet = usersRecentSetOptional.get();
        return usersRecentSet.getFlashcardSetList();
    }

    public void addRecentSetIfLogged(User user, FlashcardSet flashcardSet) {
        Optional<UsersRecentSet> usersRecentSetOptional = findOptionalByUser(user);
        if (usersRecentSetOptional.isEmpty()) {
            return;
        }

        UsersRecentSet usersRecentSet = usersRecentSetOptional.get();

        // we want to change order
        if (usersRecentSet.getFlashcardSetList().contains(flashcardSet)) {
            usersRecentSet.getFlashcardSetList().remove(flashcardSet);
        }

        // we want to keep track only of 5 most recent
        if (usersRecentSet.getFlashcardSetList().size() == 5) {
            usersRecentSet.getFlashcardSetList().remove(4);
        }

        usersRecentSet.getFlashcardSetList().add(0, flashcardSet);
        usersRecentSetRepository.save(usersRecentSet);
    }

}
