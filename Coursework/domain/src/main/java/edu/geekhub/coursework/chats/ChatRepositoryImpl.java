package edu.geekhub.coursework.chats;

import edu.geekhub.coursework.chats.intefaces.ChatRepository;
import java.util.List;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class ChatRepositoryImpl implements ChatRepository {
    private final ChatValidator validator;
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private static final String FETCH_CHAT_BY_USER_ID = """
        SELECT * FROM Chats
        WHERE userId=:userId
        """;
    private static final String FETCH_CHAT_BY_ID = """
        SELECT * FROM Chats
        WHERE id=:id
        """;
    private static final String FETCH_CHATS_BY_ADMIN_ID_BY_PAGE = """
        SELECT Chats.* FROM Chats
        LEFT JOIN Messages
        ON Chats.id=Messages.chatId
        WHERE adminId=:adminId
        GROUP BY Chats.id
        ORDER BY MAX(Messages.dateTime) DESC
        LIMIT :limit
        OFFSET :limit * :pageNumber
        """;
    private static final String FETCH_CHATS_WITHOUT_ADMIN_ID_BY_PAGE = """
        SELECT Chats.* FROM Chats
        LEFT JOIN Messages
        ON Chats.id=Messages.chatId
        WHERE Chats.adminId IS NULL
        GROUP BY Chats.id
        ORDER BY MAX(Messages.dateTime) DESC
        LIMIT :limit
        OFFSET :limit * :pageNumber
        """;
    private static final String FETCH_ALL_CHATS_BY_PAGE = """
        SELECT Chats.* FROM Chats
        LEFT JOIN Messages
        ON Chats.id=Messages.chatId
        GROUP BY Chats.id
        ORDER BY MAX(Messages.dateTime) DESC
        LIMIT :limit
        OFFSET :limit * :pageNumber
        """;
    private static final String FETCH_ALL_CHATS = """
        SELECT * FROM Chats
        """;
    private static final String INSERT_CHAT = """
        INSERT INTO Chats(userId)
        VALUES (:userId)
        """;
    private static final String UPDATE_CHAT_ADMIN_BY_ID = """
        UPDATE Chats SET
        adminId=:adminId
        WHERE id=:id
        """;

    public ChatRepositoryImpl(ChatValidator validator, NamedParameterJdbcTemplate jdbcTemplate) {
        this.validator = validator;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Chat getChatByUserId(int userId) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
            .addValue("userId", userId);

        return jdbcTemplate.query(FETCH_CHAT_BY_USER_ID, mapSqlParameterSource,
                (rs, rowNum) -> new Chat(
                    rs.getInt("id"),
                    rs.getInt("userId"),
                    rs.getInt("adminId")
                ))
            .stream()
            .findFirst()
            .orElse(null);
    }

    @Override
    public Chat getChatById(int id) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
            .addValue("id", id);

        return jdbcTemplate.query(FETCH_CHAT_BY_ID, mapSqlParameterSource,
                (rs, rowNum) -> new Chat(
                    rs.getInt("id"),
                    rs.getInt("userId"),
                    rs.getInt("adminId")
                ))
            .stream()
            .findFirst()
            .orElse(null);
    }

    @Override
    public int addChat(Chat chat) {
        validator.validate(chat);

        GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
            .addValue("userId", chat.getUserId());

        jdbcTemplate.update(INSERT_CHAT, mapSqlParameterSource, generatedKeyHolder);

        var keys = generatedKeyHolder.getKeys();
        if (keys != null) {
            return (int) keys.get("id");
        }
        return -1;
    }

    @Override
    public void updateChatAdminById(Chat chat, int id) {
        validator.validate(chat);

        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
            .addValue("adminId", chat.getAdminId())
            .addValue("id", id);

        jdbcTemplate.update(UPDATE_CHAT_ADMIN_BY_ID, mapSqlParameterSource);
    }

    @Override
    public List<Chat> getChatsByPageAndAdminId(int adminId, int limit, int pageNumber) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
            .addValue("adminId", adminId)
            .addValue("limit", limit)
            .addValue("pageNumber", pageNumber - 1);

        String sql;
        switch (adminId) {
            case -1 -> sql = FETCH_ALL_CHATS_BY_PAGE;
            case 0 -> sql = FETCH_CHATS_WITHOUT_ADMIN_ID_BY_PAGE;
            default -> sql = FETCH_CHATS_BY_ADMIN_ID_BY_PAGE;
        }

        return jdbcTemplate.query(sql, mapSqlParameterSource,
            (rs, rowNum) -> new Chat(
                rs.getInt("id"),
                rs.getInt("userId"),
                rs.getInt("adminId")
            ));
    }

    @Override
    public List<Chat> getChats() {
        return jdbcTemplate.query(FETCH_ALL_CHATS,
            (rs, rowNum) -> new Chat(
                rs.getInt("id"),
                rs.getInt("userId"),
                rs.getInt("adminId")
            ));
    }
}
