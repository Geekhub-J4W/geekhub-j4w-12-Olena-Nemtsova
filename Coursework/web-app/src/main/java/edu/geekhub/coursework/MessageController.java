package edu.geekhub.coursework;

import edu.geekhub.coursework.messages.Message;
import edu.geekhub.coursework.messages.interfaces.MessageService;
import edu.geekhub.coursework.security.SecurityUser;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageController {
    private final MessageService messageService;
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public MessageController(
        MessageService messageService,
        SimpMessagingTemplate messagingTemplate
    ) {
        this.messageService = messageService;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/chat/{userId}")
    @SendTo("/topic/refresh")
    public Message sendMessageToUser(@DestinationVariable int userId, Message message) {
        messagingTemplate.convertAndSend("/topic/reply/" + userId, message);
        return messageService.addMessage(message, userId);
    }

    @MessageMapping("/chat")
    @SendTo("/topic/refresh")
    public Message sendMessageFromUser(Message message) {
        messagingTemplate.convertAndSend("/topic/reply/" + getUserId(), message);
        return messageService.addMessage(message, getUserId());
    }

    @GetMapping("/messages/{userId}")
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'ADMIN')")
    public List<Message> getChatMessagesByUserId(@PathVariable int userId) {
        return messageService.getMessagesByUserId(userId);
    }

    @GetMapping("/messages")
    public List<Message> getChatMessages() {
        return messageService.getMessagesByUserId(getUserId());
    }

    @GetMapping("/messages/last/{userId}")
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'ADMIN')")
    public Message getLastMessageByUserId(@PathVariable int userId) {
        return messageService.getLastMessageByUserId(userId);
    }

    private int getUserId() {
        Object object = SecurityContextHolder
            .getContext()
            .getAuthentication()
            .getPrincipal();

        SecurityUser user = (SecurityUser) object;
        return user.getUserId();
    }
}
