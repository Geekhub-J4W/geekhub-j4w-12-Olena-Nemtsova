package edu.geekhub.coursework.chats.intefaces;

import edu.geekhub.coursework.chats.Chat;
import java.util.List;

public interface ChatService {
    Chat getChatById(int id);

    Chat getChatByUserId(int userId);

    Chat updateChat(Chat chat, int id);

    List<Chat> getChatsByPageAndAdminId(int adminId, int limit, int pageNumber);

    int getCountOfPages(int limit, int adminId);

    Chat addChat(Chat chat);
}
