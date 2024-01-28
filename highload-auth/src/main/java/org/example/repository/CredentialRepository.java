package org.example.repository;

import lombok.RequiredArgsConstructor;
import org.example.model.Credential;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class CredentialRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public void insert(String username, String password, UUID accountId) {
        Map<String, Object> p = Map.of(
                "id", UUID.randomUUID(),
                "username", username,
                "password", password,
                "accountId", accountId
        );
        jdbcTemplate.update("insert into credential values (:id, :username, :password, :accountId)", p);
    }

    @Transactional
    public void delete(UUID accountId) {
        jdbcTemplate.update(
                "delete from token where credential_id in (select c.id from credential c join token t on c.id = t.credential_id where c.account_id=:accountId)",
                Map.of("accountId", accountId)
        );
        jdbcTemplate.update("delete from credential where account_id=:accountId", Map.of("accountId", accountId));
    }

    public Credential findByUsername(String username) {
        return jdbcTemplate.query("select * from credential where username = :username", Map.of("username", username), rs -> {
            if (rs.next()) {
                return new Credential(
                        UUID.fromString(rs.getString("id")),
                        rs.getString("username"),
                        rs.getString("password"),
                        UUID.fromString(rs.getString("account_id"))
                );
            } else {
                return null;
            }
        });
    }

    public void saveToken(UUID credentialId, String token, LocalDateTime expiryDate) {
        Map<String, Object> p = Map.of(
                "id", UUID.randomUUID(),
                "credentialId", credentialId,
                "accessToken", token,
                "expiryDate", expiryDate
        );
        jdbcTemplate.update("insert into token values (:id, :credentialId, :accessToken, :expiryDate)", p);
    }

    public boolean unexpiredTokenIsExist(String token) {
        String query = "select exists(select 1 as VALUE from (select * from token where access_token=:token and expiry_date>:date) t " +
                "join credential c on t.credential_id=c.id)";

        Map<String, Object> p = Map.of("token", token, "date", LocalDateTime.now());
        Boolean result = jdbcTemplate.queryForObject(query, p, Boolean.class);
        return result != null && result;
    }

    public boolean exist(String userName) {
        Boolean exist = jdbcTemplate.queryForObject(
                "select exists(select 1 from credential where username = :username)",
                Map.of("username", userName),
                Boolean.class
        );
        return Boolean.TRUE.equals(exist);
    }
}
