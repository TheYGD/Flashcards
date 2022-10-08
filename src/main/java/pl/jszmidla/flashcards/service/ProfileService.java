package pl.jszmidla.flashcards.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.jszmidla.flashcards.repository.UserRepository;

@Service
@AllArgsConstructor
public class ProfileService {

    private UserRepository userRepository;


}
