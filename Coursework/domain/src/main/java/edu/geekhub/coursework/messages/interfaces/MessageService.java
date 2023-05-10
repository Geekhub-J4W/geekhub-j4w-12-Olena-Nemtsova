package edu.geekhub.coursework.messages.interfaces;

import edu.geekhub.coursework.messages.Message;
import java.util.List;

public interface MessageService {
    List<Message> getMessagesByUserId(int userId);

    Message getLastMessageByUserId(int userId);

    Message addMessage(Message message, int userId);
}
