package org.example.repository;

import lombok.RequiredArgsConstructor;
import org.example.service.Transaction;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class WalletRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public UUID insertWallet(UUID accountId) {
        UUID id = UUID.randomUUID();
        jdbcTemplate.update("insert into wallet values (:id, :account_id, 0)", Map.of("id", id , "account_id", accountId));
        return id;
    }

    public Integer selectForUpdateBalance(UUID walletId) {
        return jdbcTemplate.queryForObject(
                "select balance from wallet where id = :wallet_id for update",
                Map.of("wallet_id", walletId),
                Integer.class
        );
    }

    public void updateWallet(UUID walletId, int balance) {
        jdbcTemplate.update(
                "update wallet set balance = :balance where id = :wallet_id",
                Map.of("balance", balance, "wallet_id", walletId)
        );
    }

    public void insertTransaction(UUID id, UUID walletId, int amount) {
        jdbcTemplate.update(
                "insert into transaction values (:id, :wallet_id, :amount, :date_time)",
                Map.of("id", id, "wallet_id", walletId, "amount", amount, "date_time", LocalDateTime.now())
        );
    }

    public Transaction selectTransactionAmount(UUID id) {
        return jdbcTemplate.queryForObject(
                "select wallet_id, amount from transaction where id = :id",
                Map.of("id", id),
                (rs, n) -> new Transaction(UUID.fromString(rs.getString("wallet_id")), rs.getInt("amount"))
        );
    }

    public void deleteTransaction(UUID id) {
        jdbcTemplate.update("delete from transaction where id = :id", Map.of("id", id));
    }
}
