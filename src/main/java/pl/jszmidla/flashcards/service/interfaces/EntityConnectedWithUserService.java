package pl.jszmidla.flashcards.service.interfaces;

import pl.jszmidla.flashcards.data.User;

/** Created not to unnecessary mix services with each other
 *  This is a service, which want to create entity connected to the User entity */
public interface EntityConnectedWithUserService {

    void createConnectedEntity(User user);
    void registerThisService(UserEntityRegister connector);
}
