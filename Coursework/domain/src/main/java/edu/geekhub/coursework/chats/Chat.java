package edu.geekhub.coursework.chats;

import java.util.Objects;

public class Chat {
    private int id;
    private int userId;
    private int adminId;

    public Chat(int id, int userId, int adminId) {
        this.id = id;
        this.userId = userId;
        this.adminId = adminId;
    }

    public Chat() {
        this(-1, -1, -1);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    @Override
    public String toString() {
        return "Chat{"
               + "id=" + id
               + ", userId=" + userId
               + ", adminId=" + adminId
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
        Chat chat = (Chat) o;
        return id == chat.id
               && userId == chat.userId
               && adminId == chat.adminId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, adminId);
    }
}
