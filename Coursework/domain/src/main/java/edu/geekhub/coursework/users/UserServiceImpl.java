package edu.geekhub.coursework.users;

import edu.geekhub.coursework.users.interfaces.UserRepository;
import edu.geekhub.coursework.users.interfaces.UserService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.tinylog.Logger;

@Service
public class UserServiceImpl implements UserService {
    private final UserValidator validator;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserValidator validator,
                           UserRepository userRepository) {
        this.validator = validator;
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder(12);
    }

    @Override
    public User getUserById(int id) {
        return userRepository.getUserById(id);
    }

    @Override
    public User addUser(User user) {
        try {
            validator.validate(user);
            user.setPassword(passwordEncoder.encode(user.getPassword()));

            int id = userRepository.addUser(user);
            if (id == -1) {
                throw new IllegalArgumentException("Unable to retrieve the generated key");
            }
            user.setId(id);
            Logger.info("User was added:\n" + user);
            return getUserById(id);
        } catch (IllegalArgumentException | DataAccessException exception) {
            Logger.warn("User wasn't added: " + user + "\n" + exception.getMessage());
            return null;
        }
    }

    @Override
    public boolean deleteUserById(int id) {
        User userToDel = getUserById(id);
        try {
            if (userToDel == null) {
                throw new IllegalArgumentException("User with id '" + id + "' not found");
            }
            userRepository.deleteUserById(id);
            Logger.info("User was deleted:\n" + userToDel);
            return true;
        } catch (IllegalArgumentException | DataAccessException exception) {
            Logger.warn("User wasn't deleted: " + userToDel + "\n" + exception.getMessage());
            return false;
        }
    }

    @Override
    public User updateUserById(User user, int id) {
        try {
            validator.validate(user);
            if (getUserById(id) == null) {
                throw new IllegalArgumentException("User with id '" + id + "' not found");
            }
            user.setPassword(passwordEncoder.encode(user.getPassword()));

            userRepository.updateUserById(user, id);
            Logger.info("User was updated:\n" + user);
            return getUserById(id);
        } catch (IllegalArgumentException | DataAccessException exception) {
            Logger.warn("User wasn't updated: " + user + "\n" + exception.getMessage());
            return null;
        }
    }

    @Override
    public List<User> getUsers() {
        return userRepository.getUsers();
    }

    @Override
    public List<User> getUsersByRole(Role role) {
        if (role == null) {
            Logger.warn("User`s role was null");
            return new ArrayList<>();
        }
        return userRepository.getUsersByRole(role);
    }
}
