package org.example.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class SubscriptionRepository {

    @Qualifier("masterJdbcTemplate")
    private final NamedParameterJdbcTemplate masterJdbcTemplate;

    @Qualifier("slaveJdbcTemplate")
    private final NamedParameterJdbcTemplate slaveJdbcTemplate;

    public void save(UUID subscriberAccountId, UUID publisherAccountId) {
        masterJdbcTemplate.update(
                "insert into subscription values (:subscriberAccountId, :publisherAccountId)",
                Map.of("subscriberAccountId", subscriberAccountId, "publisherAccountId", publisherAccountId));
    }

    public void delete(UUID subscriberAccountId, UUID publisherAccountId) {
        masterJdbcTemplate.update(
                "delete from subscription where subscriber_account_id=:subscriberAccountId and publisher_account_id=:publisherAccountId",
                Map.of("subscriberAccountId", subscriberAccountId, "publisherAccountId", publisherAccountId));
    }

    public List<UUID> findAllPublishers(UUID accountId) {
        return slaveJdbcTemplate.queryForList(
                "select publisher_account_id from subscription where subscriber_account_id=:accountId",
                Map.of("accountId", accountId),
                UUID.class
        );
    }

    public List<UUID> findAllSubscribers(UUID accountId) {
        return slaveJdbcTemplate.queryForList(
                "select subscriber_account_id from subscription where publisher_account_id=:accountId",
                Map.of("accountId", accountId),
                UUID.class
        );
    }

    public List<UUID> findAllSubscribers(UUID accountId, int page, int limit) {
        int offset = page * limit;
        return slaveJdbcTemplate.queryForList(
                "select subscriber_account_id from subscription where publisher_account_id=:accountId " +
                        "order by subscriber_account_id limit :limit offset :offset",
                Map.of("accountId", accountId, "limit", limit, "offset", offset),
                UUID.class
        );
    }

    public boolean exist(UUID subscriberAccountId, UUID publisherAccountId) {
        Boolean exist = slaveJdbcTemplate.queryForObject(
                "select exists(select 1 from subscription where subscriber_account_id=:subscriberId and publisher_account_id=:publisherId)",
                Map.of("subscriberId", subscriberAccountId, "publisherId", publisherAccountId),
                Boolean.class
        );
        return Boolean.TRUE.equals(exist);
    }
}
