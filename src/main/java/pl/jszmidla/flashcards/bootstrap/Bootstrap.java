package pl.jszmidla.flashcards.bootstrap;

import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pl.jszmidla.flashcards.data.Flashcard;
import pl.jszmidla.flashcards.data.FlashcardSet;
import pl.jszmidla.flashcards.data.User;
import pl.jszmidla.flashcards.data.dto.account.RegisterUserRequest;
import pl.jszmidla.flashcards.repository.*;
import pl.jszmidla.flashcards.service.UserService;

import javax.transaction.Transactional;
import java.util.LinkedList;
import java.util.List;

@Component
@AllArgsConstructor
/**
 * Convenient class for creating some test objects after deleting database
 * */
public class Bootstrap implements CommandLineRunner {

    private FlashcardRepository flashcardRepository;
    private FlashcardSetRepository flashcardSetRepository;
    private UserRepository userRepository;
    private UserService registerService;

    private static String USER_LOGIN = "LOGIN123";
    private static String USER_PASSWORD = "Haslo123";
    private static String USER_EMAIL = "email@user.pl";
    private static String USER_BIO = "I'm LOGIN123, I'm first user of this application :)";
    private static String AUTHOR_LOGIN = "tommy172";
    private static String AUTHOR_PASSWORD = "Haslo123";
    private static String AUTHOR_EMAIL = "abb@baa.com";
    private static String AUTHOR_BIO = "Have a look at my sets, they might help you to widen your knowledge. Good luck!";


    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() != 0) {
            return;
        }

        createUser(USER_LOGIN, USER_PASSWORD, USER_EMAIL);
        setUsersBio(USER_LOGIN, USER_BIO);
        createUser(AUTHOR_LOGIN, AUTHOR_PASSWORD, AUTHOR_EMAIL);
        setUsersBio(AUTHOR_LOGIN, AUTHOR_BIO);
        createFlashcardSets();
    }

    private void createUser(String login, String password, String email) {
        RegisterUserRequest registerUserRequest = new RegisterUserRequest();
        registerUserRequest.setUsername(login);
        registerUserRequest.setPassword(password);
        registerUserRequest.setEmail(email);

        registerService.registerUser(registerUserRequest);
    }

    @Transactional
    public void createFlashcardSets() {
        User author = userRepository.findByUsernameOrEmail(AUTHOR_LOGIN, AUTHOR_LOGIN)
                .orElseThrow();

        List<Flashcard> flashcardList1 = createFlashcardList("I", "ja", "You", "ty", "He", "on");
        FlashcardSet flashcardSet1 = createSet("angielski", "angielski :)", author, flashcardList1);

        List<Flashcard> flashcardList2 = createFlashcardList("Ich", "ja", "Du", "ty", "Er", "on");
        FlashcardSet flashcardSet2 = createSet("niemiecki", "niemiecki :)", author, flashcardList2);

        List<Flashcard> flashcardList3 = createFlashcardList("blue", "niebieski", "black", "czarny", "white",
                "biały");
        FlashcardSet flashcardSet3 = createSet("kolory ang", "colors", author, flashcardList3);

        List<Flashcard> flashcardList4 = createFlashcardList("one", "jeden", "two", "dwa", "three", "trzy");
        FlashcardSet flashcardSet4 = createSet("Liczby po angielsku", "Podstawowe liczby", author,
                flashcardList4);

        List<Flashcard> flashcardList5 = createFlashcardList("ein", "jeden", "zwei", "dwa", "drei", "trzy");
        FlashcardSet flashcardSet5 = createSet("Liczby po niemiecku", "Liczby 1-3 po niemiecku", author,
                flashcardList5);

        List<Flashcard> flashcardList6 = createFlashcardList("computer", "komputer", "mouse", "mysz",
                "keyboard", "klawiatura", "microphone", "mikrofon", "headphones", "słuchawki");
        FlashcardSet flashcardSet6 = createSet("Peryferia komputerowe w j.angielskim", "Computers",
                author, flashcardList6);

        flashcardRepository.saveAll(flashcardList1);
        flashcardRepository.saveAll(flashcardList2);
        flashcardRepository.saveAll(flashcardList3);
        flashcardRepository.saveAll(flashcardList4);
        flashcardRepository.saveAll(flashcardList5);
        flashcardRepository.saveAll(flashcardList6);

        flashcardSetRepository.saveAll(List.of(flashcardSet1, flashcardSet2, flashcardSet3, flashcardSet4, flashcardSet5,
                flashcardSet6));
    }

    /** input is n string pairs,
     * arr[i] - flashcard front
     * arr[i+1] - flashcard back */
    private List<Flashcard> createFlashcardList(String... flashcards) {
        List<Flashcard> flashcardList = new LinkedList<>();

        for (int i = 0; i < flashcards.length; i += 2) {
            String front = flashcards[i];
            String back = flashcards[i+1];

            Flashcard flashcard = new Flashcard();
            flashcard.setFront(front);
            flashcard.setBack(back);
            flashcardList.add(flashcard);
        }
        return flashcardList;
    }

    private FlashcardSet createSet(String name, String description, User author, List<Flashcard> flashcardList) {
        FlashcardSet flashcardSet = new FlashcardSet();
        flashcardSet.setName(name);
        flashcardSet.setDescription(description);
        flashcardSet.setAuthor(author);
        flashcardSet.setFlashcards(flashcardList);

        return flashcardSet;
    }

    private void setUsersBio(String username, String bio) {
        User user = userRepository.findByUsernameOrEmail(username, "")
                .orElseThrow();
        user.setBio(bio);
        userRepository.save(user);
    }
}
