package pl.jszmidla.flashcards.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.jszmidla.flashcards.data.FlashcardSet;
import pl.jszmidla.flashcards.data.User;
import pl.jszmidla.flashcards.data.UsersActiveSet;
import pl.jszmidla.flashcards.data.dto.flashcard.ActiveFlashcardSetResponse;
import pl.jszmidla.flashcards.data.dto.flashcard.FlashcardResponse;
import pl.jszmidla.flashcards.data.dto.flashcard.RememberedAndUnrememberedFlashcardsSplitted;
import pl.jszmidla.flashcards.data.exception.item.ItemNotFoundException;
import pl.jszmidla.flashcards.data.exception.item.UsersActiveSetNotFoundException;
import pl.jszmidla.flashcards.data.mapper.ActiveSetMapper;
import pl.jszmidla.flashcards.data.mapper.FlashcardMapper;
import pl.jszmidla.flashcards.repository.UsersActiveSetRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UsersActiveSetService {

    private UsersActiveSetRepository usersActiveSetRepository;
    private FlashcardSetService flashcardSetService;
    private UsersRecentSetService usersRecentSetService;
    private FlashcardMapper flashcardMapper;
    private ActiveSetMapper activeSetMapper;


    public UsersActiveSet getUsersActiveSet(User user, FlashcardSet set) {
        return usersActiveSetRepository.findByUserAndFlashcardSet(user, set)
                .orElseThrow(UsersActiveSetNotFoundException::new);
    }

    @Transactional
    public UsersActiveSet getOrCreateUsersActiveSet(FlashcardSet set, User user) {
        try {
            return getUsersActiveSet(user, set);
        } catch (ItemNotFoundException e) {
            UsersActiveSet usersActiveSet = new UsersActiveSet();
            usersActiveSet.setUser(user);
            usersActiveSet.setFlashcardSet(set);

            usersActiveSetRepository.save(usersActiveSet);
            return usersActiveSet;
        }
    }

    public Set<Long> getRememberedFlashcardsIds(FlashcardSet set, User user) {
        UsersActiveSet usersActiveSet = getOrCreateUsersActiveSet(set, user);

        // reload set if it is the time
        if (usersActiveSet.isAfterReloadTime()) {
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

    public void markFlashcardAsRemembered(long setId, long flashcardId, User user) {
        FlashcardSet set = flashcardSetService.findById(setId);

        UsersActiveSet usersActiveSet = getOrCreateUsersActiveSet(set, user);
        usersActiveSet.addFlashcardToRemembered(flashcardId);
        usersActiveSetRepository.save(usersActiveSet);
    }

    /**
     * If user wants to run set sooner than it would reload itself
     */
    public void reloadSetSooner(long setId, User user) {
        FlashcardSet set = flashcardSetService.findById(setId);

        UsersActiveSet usersActiveSet = getOrCreateUsersActiveSet(set, user);
        usersActiveSet.clearRememberedFlashcards();

        usersActiveSetRepository.save(usersActiveSet);
    }

    public LocalDateTime getSetReloadDate(long setId, User user) {
        FlashcardSet set = flashcardSetService.findById(setId);
        UsersActiveSet usersActiveSet = getOrCreateUsersActiveSet(set, user);

        return usersActiveSet.getReloadDate();
    }

    public RememberedAndUnrememberedFlashcardsSplitted showSplittedFlashcardsToUser(Long id, User user) {
        FlashcardSet flashcardSet = flashcardSetService.findById(id);
        Set<Long> rememberedFlashcardsIds = getRememberedFlashcardsIds(flashcardSet, user);

        Map<Boolean, List<FlashcardResponse>> flashcardsSplittedMap = flashcardSet.getFlashcards().stream()
                .map(flashcardMapper::entityToResponse)
                .collect(Collectors.groupingBy( flashcard -> rememberedFlashcardsIds.contains( flashcard.getId() ) ));

        RememberedAndUnrememberedFlashcardsSplitted flashcardsSplitted = new RememberedAndUnrememberedFlashcardsSplitted();
        flashcardsSplitted.setRememberedFlashcardList( flashcardsSplittedMap.get(true) );
        flashcardsSplitted.setUnrememberedFlashcardList( flashcardsSplittedMap.get(false) );

        // add this set to user's recent seen
        usersRecentSetService.addRecentSetIfLogged(user, flashcardSet);

        return flashcardsSplitted;
    }

    /** returns list of activeSets, if a set is inactive we have to 'mock' it, so it will only contain flashcardSet,
     *    but no other properties */
    public List<UsersActiveSet> getActiveSetsOutOfSets(User user, List<FlashcardSet> flashcardSetList) {
        List<UsersActiveSet> activeSets = flashcardSetList.stream()
                .map( set -> {
                    try {
                        return getUsersActiveSet(user, set);
                    } catch( UsersActiveSetNotFoundException e ) {
                        UsersActiveSet mockActiveSet = new UsersActiveSet();
                        mockActiveSet.setFlashcardSet(set);
                        return mockActiveSet;
                    }
                })
                .toList();

        return activeSets;
    }

    public List<ActiveFlashcardSetResponse> findSetsByQuery(String query, User user) {
        List<FlashcardSet> flashcardSetList = flashcardSetService.findSetsByQuery(query);
        return getActiveSetsResponsesFromSets(flashcardSetList, user);
    }

    public List<ActiveFlashcardSetResponse> getActiveSetsResponsesFromSets(List<FlashcardSet> sets, User user) {
        // for not logged users treat it just like a regular set without state
        if (user == null) {
            return sets.stream()
                    .map(activeSetMapper::entityToResponseWithoutState)
                    .toList();
        }

        List<UsersActiveSet> activeSetList = getActiveSetsOutOfSets(user, sets);

        return activeSetList.stream()
                .map(activeSetMapper::entityToResponse)
                .toList();
    }
}
