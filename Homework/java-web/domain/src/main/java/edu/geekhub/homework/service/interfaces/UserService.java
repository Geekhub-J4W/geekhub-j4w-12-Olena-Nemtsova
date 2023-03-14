package edu.geekhub.homework.service.interfaces;

import edu.geekhub.homework.domain.User;
import java.util.List;

public interface UserService {

    User getUserByEmailAndPassword(String email, String password);

    User addUser(User user);

    boolean deleteUserById(String id);

    User updateUserById(User user, String id);

    List<User> getUsers();

    User getUserById(String id);

    List<User> getAdmins();

    List<User> getCustomers();
}
