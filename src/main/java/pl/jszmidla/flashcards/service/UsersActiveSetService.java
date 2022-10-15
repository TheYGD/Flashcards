package pl.jszmidla.flashcards.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.jszmidla.flashcards.data.FlashcardSet;
import pl.jszmidla.flashcards.data.User;
import pl.jszmidla.flashcards.data.UsersActiveSet;
import pl.jszmidla.flashcards.data.exception.item.ItemNotFoundException;
import pl.jszmidla.flashcards.data.exception.item.UsersActiveSetNotFoundException;
import pl.jszmidla.flashcards.repository.UsersActiveSetRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UsersActiveSetService {

    private UsersActiveSetRepository usersActiveSetRepository;

    public UsersActiveSet getUsersActiveSet(FlashcardSet set, User user) {
        return usersActiveSetRepository.findByUserAndFlashcardSet(user, set)
                .orElseThrow(UsersActiveSetNotFoundException::new);
    }

    @Transactional
    public UsersActiveSet getOrCreateUsersActiveSet(FlashcardSet set, User user) {
        try {
            return getUsersActiveSet(set, user);
        } catch (ItemNotFoundException e) {
            UsersActiveSet usersActiveSet = new UsersActiveSet();
            usersActiveSet.setUser(user);
            usersActiveSet.setFlashcardSet(set);
            usersActiveSet.adjustExpirationDate();

            usersActiveSetRepository.save(usersActiveSet);
            return usersActiveSet;
        }
    }

    public Set<Long> getRememberedFlashcardsIds(FlashcardSet set, User user) {
        UsersActiveSet usersActiveSet = getOrCreateUsersActiveSet(set, user);

        if (usersActiveSet.getExpirationDate().isBefore( LocalDateTime.now() )) {
            usersActiveSet.adjustExpirationDate();
            usersActiveSet.clearRememberedFlashcards();
            usersActiveSetRepository.save(usersActiveSet);
        }

        Set<Long> rememberedFlashcardIdSet = mapCSVStringToSetOfLongs(usersActiveSet.getRememberedFlashcardsCSV());
        return rememberedFlashcardIdSet;
    }

    private Set<Long> mapCSVStringToSetOfLongs(String rememberedFlashcardsCSV) {
        String[] idsSet = rememberedFlashcardsCSV.split(",");
        // in case of no cards remembered
        if (idsSet[0].isBlank()) {
            return Set.of();
        }

        return Arrays.stream(idsSet)
                .map(Long::valueOf)
                .collect(Collectors.toSet());
    }

    public void markFlashcardAsRemembered(FlashcardSet set, long flashcardId, User user) {
        UsersActiveSet usersActiveSet = getOrCreateUsersActiveSet(set, user);
        usersActiveSet.addFlashcardToRemembered(flashcardId);
        usersActiveSetRepository.save(usersActiveSet);
    }

    public void markFlashcardAsCompleted(FlashcardSet set, User user) {
        UsersActiveSet usersActiveSet = getOrCreateUsersActiveSet(set, user);
        usersActiveSet.incrementExpirationInterval();
        usersActiveSetRepository.save(usersActiveSet);
    }

    /**
     * If user wants to run set sooner than it would reload itself
     */
    public void reloadSetSooner(FlashcardSet set, User user) {
        UsersActiveSet usersActiveSet = getOrCreateUsersActiveSet(set, user);
        usersActiveSet.adjustExpirationDate();
        usersActiveSet.clearRememberedFlashcards();
        usersActiveSetRepository.save(usersActiveSet);
    }

    public LocalDateTime getSetExpirationDate(FlashcardSet set, User user) {
        UsersActiveSet usersActiveSet = getOrCreateUsersActiveSet(set, user);
        return usersActiveSet.getExpirationDate();
    }
}
