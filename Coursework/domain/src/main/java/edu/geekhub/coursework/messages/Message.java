package edu.geekhub.coursework.messages;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.Objects;

public class Message {
    private int id;
    private String text;
    @JsonFormat(shape = JsonFormat.Shape.OBJECT, pattern = "dd.MM.yyyy HH:mm")
    private LocalDateTime dateTime;
    private int chatId;
    private int senderId;

    public Message(int id, String text, LocalDateTime dateTime, int chatId, int senderId) {
        this.id = id;
        this.text = text;
        this.dateTime = dateTime;
        this.chatId = chatId;
        this.senderId = senderId;
    }

    public Message() {
        this(-1, null, null, -1, -1);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text.trim();
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public int getChatId() {
        return chatId;
    }

    public void setChatId(int chatId) {
        this.chatId = chatId;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    @Override
    public String toString() {
        return "Message{"
               + "id=" + id
               + ", text='" + text + '\''
               + ", dateTime=" + dateTime
               + ", chatId=" + chatId
               + ", senderId=" + senderId
               + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Message message = (Message) o;
        return id == message.id
               && chatId == message.chatId
               && senderId == message.senderId
               && Objects.equals(text, message.text)
               && Objects.equals(dateTime, message.dateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, text, dateTime, chatId, senderId);
    }
}
