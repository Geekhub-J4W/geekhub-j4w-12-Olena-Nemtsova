package edu.geekhub.homework.service;

import edu.geekhub.homework.domain.User;
import edu.geekhub.homework.domain.UserValidator;
import edu.geekhub.homework.repository.interfaces.UserRepository;
import edu.geekhub.homework.service.interfaces.UserService;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.tinylog.Logger;

public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserValidator userValidator;

    public UserServiceImpl(UserRepository userRepository,
                           UserValidator userValidator) {
        this.userRepository = userRepository;
        this.userValidator = userValidator;
    }

    @Override
    public User getUserByEmailAndPassword(String email, String password) {
        return getUsers().stream()
            .filter(user -> user.getEmail().equals(email))
            .filter(user -> user.getPassword().equals(password))
            .findFirst()
            .orElse(null);
    }

    @Override
    public boolean addUser(User user) {
        boolean successAdd = false;
        try {
            userValidator.validate(user);
            String id = userRepository.addUser(user);
            if (id == null) {
                throw new IllegalArgumentException("Unable to retrieve the generated key");
            }
            user.setId(id);
            Logger.info("User was added:\n" + user);
            successAdd = true;
        } catch (IllegalArgumentException | DataAccessException exception) {
            Logger.warn("User wasn't added: " + user + "\n" + exception.getMessage());
        }
        return successAdd;
    }

    @Override
    public boolean deleteUserById(String id) {
        User userToDel = getUserById(id);
        try {
            if (userToDel == null) {
                throw new IllegalArgumentException("User with id " + id + " not found");
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
    public boolean updateUserById(User user, String id) {
        try {
            userValidator.validate(user);
            if (getUserById(id) == null) {
                throw new IllegalArgumentException("User with id " + id + " not found");
            }
            userRepository.updateUserById(user, id);
            return true;
        } catch (IllegalArgumentException | DataAccessException exception) {
            Logger.warn("User wasn't edited: " + user + "\n" + exception.getMessage());
            return false;
        }
    }

    @Override
    public List<User> getUsers() {
        return userRepository.getUsers();
    }

    @Override
    public User getUserById(String id) {
        return userRepository.getUserById(id);
    }

    @Override
    public List<User> getAdmins() {
        return userRepository.getUsers()
            .stream()
            .filter(User::isAdmin)
            .toList();
    }

    @Override
    public List<User> getCustomers() {
        return userRepository.getUsers()
            .stream()
            .filter(u -> !u.isAdmin())
            .toList();
    }
}
