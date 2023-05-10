package edu.geekhub.coursework.chats;

import edu.geekhub.coursework.chats.intefaces.ChatRepository;
import edu.geekhub.coursework.chats.intefaces.ChatService;
import edu.geekhub.coursework.util.PageValidator;
import java.util.ArrayList;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.tinylog.Logger;

@Service
public class ChatServiceImpl implements ChatService {
    private final ChatValidator validator;
    private final PageValidator pageValidator;
    private final ChatRepository chatRepository;

    public ChatServiceImpl(
        ChatValidator validator,
        PageValidator pageValidator,
        ChatRepository chatRepository
    ) {
        this.validator = validator;
        this.pageValidator = pageValidator;
        this.chatRepository = chatRepository;
    }

    @Override
    public Chat getChatById(int id) {
        return chatRepository.getChatById(id);
    }

    @Override
    public Chat getChatByUserId(int userId) {
        return chatRepository.getChatByUserId(userId);
    }

    @Override
    public Chat addChat(Chat chat) {
        try {
            chat.setAdminId(1);

            validator.validate(chat);
            int id = chatRepository.addChat(chat);
            if (id == -1) {
                throw new IllegalArgumentException("Unable to retrieve the generated key");
            }
            chat.setId(id);
            Logger.info("Chat was added:\n" + chat);
            return chat;
        } catch (IllegalArgumentException | DataAccessException | NullPointerException exception) {
            Logger.warn("Chat wasn't added: " + chat + "\n" + exception.getMessage());
            return null;
        }
    }

    @Override
    public Chat updateChat(Chat chat, int id) {
        try {
            if (getChatById(id) == null) {
                throw new IllegalArgumentException("Chat with id '" + id + "' not found");
            }
            validator.validate(chat);
            chatRepository.updateChatAdminById(chat, id);

            Logger.info("Chat was updated:\n" + chat);
            return chat;
        } catch (IllegalArgumentException | DataAccessException exception) {
            Logger.warn("Chat wasn't updated: " + chat + "\n" + exception.getMessage());
            return null;
        }
    }

    @Override
    public List<Chat> getChatsByPageAndAdminId(int adminId, int limit, int pageNumber) {
        try {
            pageValidator.validatePageLimit(limit);
            pageValidator.validatePageNumber(pageNumber, getCountOfPages(limit, adminId));

            return chatRepository.getChatsByPageAndAdminId(adminId, limit, pageNumber);
        } catch (IllegalArgumentException exception) {
            Logger.warn(exception.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public int getCountOfPages(int limit, int adminId) {
        try {
            pageValidator.validatePageLimit(limit);
        } catch (IllegalArgumentException exception) {
            Logger.warn(exception.getMessage());
            return 1;
        }
        double count = getChatsByAdminId(adminId).size() / (double) limit;

        int moreCount = (int) Math.ceil(count);
        return moreCount != 0 ? moreCount : 1;
    }

    private List<Chat> getChatsByAdminId(int adminId) {
        if (adminId == -1) {
            return chatRepository.getChats();
        }
        return chatRepository.getChats().stream()
            .filter(chat -> chat.getAdminId() == adminId)
            .toList();
    }
}
