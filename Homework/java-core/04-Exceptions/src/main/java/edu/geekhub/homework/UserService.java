package edu.geekhub.homework;

import edu.geekhub.exceptions.ConnectionInterruptedException;
import edu.geekhub.models.User;
import edu.geekhub.storage.Repository;
import edu.geekhub.storage.MemoryStorage;

import java.util.UUID;

// Don't move this class
public class UserService {

    private final Repository repository = new MemoryStorage();
    private User[] users = new User[0];

    public void addUser(User user) {
        boolean isAdd = false;
        while (!isAdd) {
            try {
                repository.tryToAdd(user);
                setUsers();
                isAdd = true;
            } catch (ConnectionInterruptedException e) {
            }
        }
    }

    public void setUsers() {
        while (true) {
            try {
                users = repository.tryToGetAll();
                break;
            } catch (ConnectionInterruptedException e) {
            }
        }
    }

    public User[] getUsers() {
        return users;
    }

    public boolean containEmail(String email) {
        if (users.length > 0) {
            for (User item : users) {
                if (item.getEmail().equals(email)) return true;
            }
        }
        return false;
    }

    public boolean containId(UUID id) {
        if (users.length > 0) {
            for (User item : users) {
                if (item.getId().equals(id)) return true;
            }
        }
        return false;
    }

    public boolean containUserName(String userName) {
        if (users.length > 0) {
            for (User item : users) {
                if (item.getUserName().equals(userName)) return true;
            }
        }
        return false;
    }
}
