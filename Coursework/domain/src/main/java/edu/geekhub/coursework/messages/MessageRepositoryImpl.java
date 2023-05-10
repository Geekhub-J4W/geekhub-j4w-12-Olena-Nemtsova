package edu.geekhub.coursework.messages;

import edu.geekhub.coursework.messages.interfaces.MessageRepository;
import java.util.List;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class MessageRepositoryImpl implements MessageRepository {
    private final MessageValidator validator;
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private static final String FETCH_MESSAGES_BY_USER_ID = """
        SELECT * FROM Messages
        INNER JOIN Chats
        ON Messages.chatId=Chats.id
        WHERE Chats.userId=:userId
        ORDER BY Messages.dateTime
        """;
    private static final String INSERT_MESSAGE = """
        INSERT INTO Messages(text, chatId, dateTime, senderId)
        VALUES (:text, :chatId, :dateTime, :senderId)
        """;
    private static final String FETCH_LAST_MESSAGE_BY_USER_ID = """
        SELECT * FROM Messages
        INNER JOIN Chats
        ON Messages.chatId=Chats.id
        WHERE Chats.userId=:userId
        ORDER BY Messages.id DESC
        LIMIT 1
        """;

    public MessageRepositoryImpl(
        MessageValidator validator,
        NamedParameterJdbcTemplate jdbcTemplate
    ) {
        this.validator = validator;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Message> getMessagesByUserId(int userId) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
            .addValue("userId", userId);

        return jdbcTemplate.query(FETCH_MESSAGES_BY_USER_ID, mapSqlParameterSource,
            (rs, rowNum) -> new Message(
                rs.getInt("id"),
                rs.getString("text"),
                rs.getTimestamp("dateTime").toLocalDateTime(),
                rs.getInt("chatId"),
                rs.getInt("senderId")
            ));
    }

    @Override
    public Message getLastMessageByUserId(int userId) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
            .addValue("userId", userId);

        return jdbcTemplate.query(FETCH_LAST_MESSAGE_BY_USER_ID, mapSqlParameterSource,
                (rs, rowNum) -> new Message(
                    rs.getInt("id"),
                    rs.getString("text"),
                    rs.getTimestamp("dateTime").toLocalDateTime(),
                    rs.getInt("chatId"),
                    rs.getInt("senderId")
                ))
            .stream()
            .findFirst()
            .orElse(null);
    }

    @Override
    public int addMessage(Message message) {
        validator.validate(message);

        GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
            .addValue("text", message.getText())
            .addValue("chatId", message.getChatId())
            .addValue("dateTime", message.getDateTime())
            .addValue("senderId", message.getSenderId());

        jdbcTemplate.update(INSERT_MESSAGE, mapSqlParameterSource, generatedKeyHolder);

        var keys = generatedKeyHolder.getKeys();
        if (keys != null) {
            return (int) keys.get("id");
        }
        return -1;
    }
}
