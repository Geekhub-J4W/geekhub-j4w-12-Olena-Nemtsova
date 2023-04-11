package edu.geekhub.homework.users.interfaces;

import edu.geekhub.homework.users.Role;
import edu.geekhub.homework.users.User;
import java.util.List;

public interface UserRepository {

    List<User> getUsers();

    int addUser(User user);

    User getUserById(int id);

    User getUserByEmail(String email);

    void deleteUserById(int id);

    void updateUserById(User user, int id);

    List<User> getUsersByRole(Role role);
}
