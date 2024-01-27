package org.example.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ChatRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public UUID insertChat(UUID ownerAccountId) {
        UUID id = UUID.randomUUID();
        jdbcTemplate.update("insert into chat values (:id, :ownerAccountId)", Map.of("id", id, "ownerAccountId", ownerAccountId));
        return id;
    }

    public void deleteChatById(UUID chatId) {
        jdbcTemplate.update("delete from chat where id=:id", Map.of("id", chatId));
    }

    public List<UUID> findChatByOwnerId(UUID accountId) {
        return jdbcTemplate.queryForList(
                "select id from chat where owner_account_id=:accountId",
                Map.of("accountId", accountId),
                UUID.class
        );
    }

    public List<UUID> findChatByMemberId(UUID accountId) {
        return jdbcTemplate.queryForList(
                "select id from chat c join chat_member cm on c.id=cm.chat_id  where cm.account_id=:accountId",
                Map.of("accountId", accountId),
                UUID.class
        );
    }

    public UUID findOwnerByChatId(UUID chatId) {
        return jdbcTemplate.queryForObject("select owner_account_id from chat where id=:chatId", Map.of("chatId", chatId), UUID.class);
    }

    public void insertMember(UUID chatId, UUID accountId) {
        jdbcTemplate.update("insert into chat_member values (:chatId, :accountId)", Map.of("chatId", chatId, "accountId", accountId));
    }

    public void deleteMember(UUID chatId, UUID accountId) {
        jdbcTemplate.update(
                "delete from chat_member where chat_id=:chatId and account_id=:accountId",
                Map.of("chatId", chatId, "accountId", accountId)
        );
    }

    public boolean memberExist(UUID chatId, UUID accountId) {
        Boolean exist = jdbcTemplate.queryForObject(
                "select exists(select 1 from chat_member where chat_id = :chatId and account_id=:accountId)",
                Map.of("chatId", chatId, "accountId", accountId),
                Boolean.class
        );
        return Boolean.TRUE.equals(exist);
    }

    public List<UUID> findMembersByChatId(UUID chatId) {
        return jdbcTemplate.queryForList(
                "select account_id from chat_member where chat_id=:chatId",
                Map.of("chatId", chatId),
                UUID.class
        );
    }

    public void insertMessage(Message m) {
        m.setId(UUID.randomUUID());
        Map<String, Object> param = Map.of(
                "id", m.getId(),
                "chatId", m.getChatId(),
                "accountId", m.getAccountId(),
                "text", m.getText(),
                "creationDateTime", m.getCreationDateTime()
        );
        jdbcTemplate.update("insert into chat_message values (:id, :chatId, :accountId, :text, :creationDateTime)", param);
    }

    public List<Message> findAllMessages(UUID chatId) {
        return jdbcTemplate.query(
                "select * from chat_message where chat_id=:chatId",
                Map.of("chatId", chatId),
                rs -> (List<Message>) messageList(rs)
        );
    }

    private List<Message> messageList(ResultSet rs) throws SQLException {
        List<Message> posts = new ArrayList<>();
        while (rs.next()) {
            posts.add(message(rs));
        }
        return posts;
    }

    private Message message(ResultSet rs) {
        try {
            return new Message(
                    UUID.fromString(rs.getString("id")),
                    UUID.fromString(rs.getString("chat_id")),
                    UUID.fromString(rs.getString("account_id")),
                    rs.getString("message"),
                    rs.getTimestamp("creation_date_time").toLocalDateTime()
            );
        } catch (SQLException e) {
            log.error("Exception when data was extracting from ResultSet", e);
            return null;
        }
    }
}
