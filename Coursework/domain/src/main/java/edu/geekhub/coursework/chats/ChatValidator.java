package edu.geekhub.coursework.chats;

import edu.geekhub.coursework.users.Role;
import edu.geekhub.coursework.users.User;
import edu.geekhub.coursework.users.interfaces.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class ChatValidator {
    private final UserRepository userRepository;

    public ChatValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void validate(Chat chat) {
        if (chat == null) {
            throw new IllegalArgumentException("Chat was null");
        }

        validateUserId(chat.getUserId());
        validateAdminId(chat.getAdminId());
    }

    private void validateUserId(int userId) {
        if (userRepository.getUserById(userId) == null) {
            throw new IllegalArgumentException("Chat had not exists user id");
        }
    }

    private void validateAdminId(int adminId) {
        User admin = userRepository.getUserById(adminId);

        if (admin == null) {
            throw new IllegalArgumentException("Chat had not exists admin id");
        }
        if (admin.getRole() == Role.USER) {
            throw new IllegalArgumentException("Chat had wrong admin id");
        }
    }
}
