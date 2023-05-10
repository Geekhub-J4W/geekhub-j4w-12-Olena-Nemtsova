package edu.geekhub.coursework.messages;

import edu.geekhub.coursework.chats.intefaces.ChatRepository;
import edu.geekhub.coursework.users.interfaces.UserRepository;
import java.time.LocalDateTime;
import org.springframework.stereotype.Component;

@Component
public class MessageValidator {
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;

    public MessageValidator(
        ChatRepository chatRepository,
        UserRepository userRepository
    ) {
        this.chatRepository = chatRepository;
        this.userRepository = userRepository;
    }

    public void validate(Message message) {
        if (message == null) {
            throw new IllegalArgumentException("Message was null");
        }

        validateText(message.getText());
        validateDateTime(message.getDateTime());
        validateChatId(message.getChatId());
        validateSenderId(message.getSenderId());
    }

    private void validateText(String text) {
        if (text == null) {
            throw new IllegalArgumentException("Message text was null");
        }
        if (text.isBlank()) {
            throw new IllegalArgumentException("Message text was empty");
        }
        if (text.length() > 60000) {
            throw new IllegalArgumentException("Message text had wrong length");
        }
    }

    private void validateChatId(int chatId) {
        if (chatRepository.getChatById(chatId) == null) {
            throw new IllegalArgumentException("Message had not exists chat id");
        }
    }

    private void validateDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            throw new IllegalArgumentException("Message dateTime was null");
        }
        if (dateTime.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("Message dateTime was more than now");
        }
    }

    private void validateSenderId(int senderId) {
        if (userRepository.getUserById(senderId) == null) {
            throw new IllegalArgumentException("Message had not exists sender id");
        }
    }
}
