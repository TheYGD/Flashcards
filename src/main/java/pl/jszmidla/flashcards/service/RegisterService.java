package pl.jszmidla.flashcards.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import pl.jszmidla.flashcards.data.Role;
import pl.jszmidla.flashcards.data.User;
import pl.jszmidla.flashcards.data.dto.RegisterUserRequest;
import pl.jszmidla.flashcards.data.mapper.UserMapper;
import pl.jszmidla.flashcards.repository.UserRepository;
import pl.jszmidla.flashcards.service.interfaces.EntityConnectedWithUserService;
import pl.jszmidla.flashcards.service.interfaces.UserEntityRegister;

import javax.transaction.Transactional;
import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RegisterService implements UserEntityRegister {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private List<EntityConnectedWithUserService> connectedEntityServices = new LinkedList<>();

    @Override
    public void registerService(EntityConnectedWithUserService service) {
        connectedEntityServices.add(service);
    }

    @Transactional
    public void registerUser(RegisterUserRequest registerUserRequest) {
        User user = userMapper.registerToUser(registerUserRequest);
        user.setRole(Role.USER);
        userRepository.save(user);
        createConnectedEntities(user);
    }

    /** Created not to unnecessary mix services with each other */
    private void createConnectedEntities(User user) {
        for (EntityConnectedWithUserService service : connectedEntityServices) {
            service.createConnectedEntity(user);
        }
    }

    public ObjectError checkIfDataIsValid(RegisterUserRequest registerUserRequest) {
        if (isEmailTaken(registerUserRequest.getEmail())) {
            return new ObjectError("email", "Email is taken!");
        }

        if (isUsernameTaken(registerUserRequest.getUsername())) {
            return new ObjectError("username", "Username is taken!");
        }

        return null;
    }

    private boolean isUsernameTaken(String username) {
        return userRepository.existsByUsername(username);
    }

    private boolean isEmailTaken(String email) {
        return userRepository.existsByEmail(email);
    }
}
