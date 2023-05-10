package edu.geekhub.coursework.messages;

import edu.geekhub.coursework.chats.Chat;
import edu.geekhub.coursework.chats.intefaces.ChatService;
import edu.geekhub.coursework.messages.interfaces.MessageRepository;
import edu.geekhub.coursework.messages.interfaces.MessageService;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.tinylog.Logger;

@Service
public class MessageServiceImpl implements MessageService {
    private final MessageValidator validator;
    private final MessageRepository messageRepository;
    private final ChatService chatService;

    public MessageServiceImpl(
        MessageValidator validator,
        MessageRepository messageRepository,
        ChatService chatService
    ) {
        this.validator = validator;
        this.messageRepository = messageRepository;
        this.chatService = chatService;
    }

    @Override
    public List<Message> getMessagesByUserId(int userId) {
        return messageRepository.getMessagesByUserId(userId);
    }

    @Override
    public Message getLastMessageByUserId(int userId) {
        return messageRepository.getLastMessageByUserId(userId);
    }

    @Override
    public Message addMessage(Message message, int userId) {
        try {
            if (chatService.getChatById(message.getChatId()) == null) {
                message.setChatId(getChatId(userId));
            }
            validator.validate(message);

            int id = messageRepository.addMessage(message);
            if (id == -1) {
                throw new IllegalArgumentException("Unable to retrieve the generated key");
            }
            message.setId(id);
            Logger.info("Message was added:\n" + message);
            return message;
        } catch (IllegalArgumentException | DataAccessException exception) {
            Logger.warn("Message wasn't added: " + message + "\n" + exception.getMessage());
            return null;
        }
    }

    private int getChatId(int userId) {
        Chat chat = chatService.getChatByUserId(userId);
        if (chat == null) {
            chat = new Chat();
            chat.setUserId(userId);
            chat = chatService.addChat(chat);
            if (chat == null) {
                throw new IllegalArgumentException("Failed create new chat");
            }
        }
        return chat.getId();
    }
}
