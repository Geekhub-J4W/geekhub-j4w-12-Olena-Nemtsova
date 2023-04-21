package edu.geekhub.coursework.users;

import edu.geekhub.coursework.users.interfaces.UserRepository;
import edu.geekhub.coursework.users.interfaces.UserService;
import edu.geekhub.coursework.util.PageValidator;
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
    private final PageValidator pageValidator;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(
        UserValidator validator,
        PageValidator pageValidator,
        UserRepository userRepository
    ) {
        this.validator = validator;
        this.pageValidator = pageValidator;
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

            User existsUser = getUserByEmail(user.getEmail());
            if (existsUser != null
                && passwordEncoder.matches("Temporary1", existsUser.getPassword())) {
                return updateUserById(user, existsUser.getId());
            } else if (existsUser != null) {
                throw new IllegalArgumentException(
                    "User with email '" + existsUser.getEmail() + "' already exists"
                );
            }

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
            validator.validateUserToDelete(userToDel);

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
            User userToUpdate = getUserById(id);
            validator.validateUsersForUpdate(userToUpdate, user);

            if (user.getPassword() != null) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
                userRepository.updateUserById(user, id);
            } else {
                userRepository.updateUserWithoutPasswordById(user, id);
            }
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
    public int getCountOfPages(Role role, int limit, String input) {
        try {
            pageValidator.validatePageLimit(limit);
        } catch (IllegalArgumentException exception) {
            Logger.warn(exception.getMessage());
            return 1;
        }
        double count = getUsersOfRoleNameContainsInput(role, input).size() / (double) limit;

        int moreCount = (int) Math.ceil(count);
        return moreCount != 0 ? moreCount : 1;
    }

    @Override
    public List<User> getUsersOfRoleByPageAndInput(
        Role role,
        int limit,
        int pageNumber,
        String input
    ) {
        try {
            pageValidator.validatePageLimit(limit);
            pageValidator.validatePageNumber(pageNumber, getCountOfPages(role, limit, input));

            return userRepository.getUsersOfRoleByPageAndInput(role, limit, pageNumber, input);
        } catch (IllegalArgumentException exception) {
            Logger.warn(exception.getMessage());
            return new ArrayList<>();
        }
    }

    private List<User> getUsersOfRoleNameContainsInput(Role role, String input) {
        return getUsers().stream()
            .filter(user -> user.getRole() == role
                            && (user.getFirstName().toLowerCase().replace(" ", "")
                                + user.getLastName().toLowerCase().replace(" ", ""))
                                .contains(input.toLowerCase().replace(" ", "")))
            .toList();
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.getUserByEmail(email);
    }
}
