package org.example.repository;

import lombok.RequiredArgsConstructor;
import org.example.entity.Advertisement;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdvertisementRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public void save(Advertisement ad, UUID postId) {
        UUID id = UUID.randomUUID();
        jdbcTemplate.update(
                "insert into advertisement values (:id, :account_id, :post_id, :data, :date_time)",
                Map.of("id", id, "account_id", ad.getAccountId(), "post_id", postId, "data", ad.getText(), "date_time", LocalDateTime.now())
        );
    }
}
