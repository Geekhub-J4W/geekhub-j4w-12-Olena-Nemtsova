package edu.geekhub.homework.repository.interfaces;

import edu.geekhub.homework.domain.User;
import java.util.List;

public interface UserRepository {

    List<User> getUsers();

    String addUser(User user);

    User getUserById(String id);

    void deleteUserById(String id);

    void updateUserById(User user, String id);
}
