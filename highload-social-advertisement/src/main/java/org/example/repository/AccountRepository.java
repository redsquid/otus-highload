package org.example.repository;


import lombok.RequiredArgsConstructor;
import org.example.entity.Account;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class AccountRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public void save(Account account) {
        jdbcTemplate.update(
                "insert into account values (:id, :token, :wallet_id)",
                Map.of("id", account.getId(), "token", account.getToken(), "wallet_id", account.getWalletId())
        );
    }

    public Account findByAccountId(UUID accountId) {
        return jdbcTemplate.queryForObject(
                "select * from account where id = :id",
                Map.of("id", accountId),
                (rs, n) -> new Account((UUID) rs.getObject("id"), rs.getString("access_token"), (UUID) rs.getObject("wallet_id"))
        );
    }
}
