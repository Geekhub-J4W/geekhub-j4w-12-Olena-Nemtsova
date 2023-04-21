package edu.geekhub.coursework.users.interfaces;

import edu.geekhub.coursework.users.Role;
import edu.geekhub.coursework.users.User;
import java.util.List;

public interface UserService {

    User getUserById(int id);

    User addUser(User user);

    boolean deleteUserById(int id);

    User updateUserById(User user, int id);

    List<User> getUsers();

    int getCountOfPages(Role role, int limit, String input);

    List<User> getUsersOfRoleByPageAndInput(
            Role role,
            int limit,
            int pageNumber,
            String input
    );

    User getUserByEmail(String email);
}
