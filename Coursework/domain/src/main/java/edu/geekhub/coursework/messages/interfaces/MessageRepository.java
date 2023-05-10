package edu.geekhub.coursework.messages.interfaces;

import edu.geekhub.coursework.messages.Message;
import java.util.List;

public interface MessageRepository {
    List<Message> getMessagesByUserId(int userId);

    Message getLastMessageByUserId(int userId);

    int addMessage(Message message);
}
