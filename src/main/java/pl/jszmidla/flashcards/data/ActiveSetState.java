package pl.jszmidla.flashcards.data;

import java.time.LocalDateTime;

public enum ActiveSetState {
    /** set that has been never (or for a very long time) used by user */
    INACTIVE,
    /** set that has been started by user, but not completed */
    ACTIVE_NOT_COMPLETED,
    /** set that was completed by user and has not reloaded yet */
    COMPLETED_WAIT,
    /** set that was completed by user and has been reloaded */
    COMPLETED_READY;


    public static ActiveSetState fromUsersActiveSet(UsersActiveSet usersActiveSet) {
        // no actual usersActiveSet just 'mock'
        if (usersActiveSet.getId() == null) {
            return INACTIVE;
        }

        // that is already reloaded but not updated yet
        if (usersActiveSet.getReloadDate().isBefore( LocalDateTime.now() )) {
            return COMPLETED_READY;
        }

        // no reload date - set is ready or not completed
        if (usersActiveSet.getReloadDate().equals( UsersActiveSet.MAX_DATE )) {
            if (usersActiveSet.getRememberedFlashcardsCSV().equals("") && usersActiveSet.getReloadInterval() > 0) {
                return COMPLETED_READY;
            }
            return ACTIVE_NOT_COMPLETED;
        }

        return COMPLETED_WAIT;
    }

    public String getCssClass() {
        return "flashcard-" + name().toLowerCase().replace("_", "-");
    }
}
