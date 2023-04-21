package edu.geekhub.coursework.users.interfaces;

import edu.geekhub.coursework.users.Role;
import edu.geekhub.coursework.users.User;
import java.util.List;

public interface UserRepository {

    List<User> getUsers();

    int addUser(User user);

    User getUserById(int id);

    User getUserByEmail(String email);

    void deleteUserById(int id);

    void updateUserById(User user, int id);

    void updateUserWithoutPasswordById(User user, int id);

    List<User> getUsersOfRoleByPageAndInput(
            Role role,
            int limit,
            int pageNumber,
            String input
    );
}
