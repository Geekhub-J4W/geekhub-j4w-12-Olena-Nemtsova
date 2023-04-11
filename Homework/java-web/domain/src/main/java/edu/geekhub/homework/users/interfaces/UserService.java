package edu.geekhub.homework.users.interfaces;

import edu.geekhub.homework.users.Role;
import edu.geekhub.homework.users.User;
import java.util.List;

public interface UserService {

    User addUser(User user);

    boolean deleteUserById(int id);

    User updateUserById(User user, int id);

    List<User> getUsers();

    User getUserById(int id);

    User getUserByEmail(String email);

    List<User> getUsersByRole(Role role);
}
