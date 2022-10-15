package pl.jszmidla.flashcards.service.interfaces;

/** Created not to unnecessary mix services with each other
 *  This is a User register, after user registration it will invoke
 *  all the connected services methods to create their Entities */
public interface UserEntityRegister {

    void registerService(EntityConnectedWithUserService service);
}
