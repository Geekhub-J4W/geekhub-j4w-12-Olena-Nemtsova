package edu.geekhub.coursework.chats.intefaces;

import edu.geekhub.coursework.chats.Chat;
import java.util.List;

public interface ChatRepository {
    Chat getChatByUserId(int userId);

    Chat getChatById(int id);

    List<Chat> getChatsByPageAndAdminId(int adminId, int limit, int pageNumber);

    List<Chat> getChats();

    int addChat(Chat chat);

    void updateChatAdminById(Chat chat, int id);
}
