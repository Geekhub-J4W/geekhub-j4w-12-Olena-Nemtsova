package edu.geekhub.coursework;

import edu.geekhub.coursework.chats.Chat;
import edu.geekhub.coursework.chats.intefaces.ChatService;
import edu.geekhub.coursework.security.SecurityUser;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatController {
    private final ChatService chatService;

    @Autowired
    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/chats/{limit}/{pageNumber}/{adminId}")
    public List<Chat> getChats(
        @PathVariable String adminId,
        @PathVariable int limit,
        @PathVariable int pageNumber
    ) {
        if (adminId.equals("own")) {
            return chatService.getChatsByPageAndAdminId(getUserId(), limit, pageNumber);
        }
        return chatService.getChatsByPageAndAdminId(Integer.parseInt(adminId), limit, pageNumber);
    }

    @MessageMapping("/chat/update/{id}")
    @SendTo("/topic/refresh")
    public Chat updateChat(
        Chat chat,
        @DestinationVariable int id
    ) {
        chat.setAdminId(getUserId());
        return chatService.updateChat(chat, id);
    }

    @GetMapping("/chats/pages/{limit}/{adminId}")
    public int getCountOfProductsPages(
        @PathVariable int limit,
        @PathVariable String adminId
    ) {
        if (adminId.equals("own")) {
            return chatService.getCountOfPages(limit, getUserId());
        }
        return chatService.getCountOfPages(limit, Integer.parseInt(adminId));
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
