package org.example.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.Post;
import org.springframework.beans.factory.annotation.Qualifier;
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
public class PostRepository {

    @Qualifier("masterJdbcTemplate")
    private final NamedParameterJdbcTemplate masterJdbcTemplate;

    @Qualifier("slaveJdbcTemplate")
    private final NamedParameterJdbcTemplate slaveJdbcTemplate;

    public Post insert(Post post) {
        post.setId(UUID.randomUUID());
        masterJdbcTemplate.update(
                "insert into post values (:id, :accountId, :post, :creationDateTime)",
                Map.of("id", post.getId(), "accountId", post.getAccountId(),
                        "post", post.getPost(), "creationDateTime", post.getCreationDateTime())
        );
        return post;
    }

    public List<UUID> extractFeed(UUID accountId) {
        return slaveJdbcTemplate.queryForList(
                "select p.id from post p join subscription s on p.account_id=s.publisher_account_id " +
                        "where s.subscriber_account_id=:accountId order by creation_date_time limit 1000",
                Map.of("accountId", accountId),
                UUID.class
        );
    }

    public List<Post> findAll(List<UUID> ids) {
        if (ids.isEmpty()) {
            return List.of();
        }
        return slaveJdbcTemplate.query("select * from post where id::text in (:ids)", Map.of("ids", ids), rs -> (List<Post>) postList(rs));
    }

    private List<Post> postList(ResultSet rs) throws SQLException {
        List<Post> posts = new ArrayList<>();
        while (rs.next()) {
            posts.add(post(rs));
        }
        return posts;
    }

    private Post post(ResultSet rs) {
        try {
            return new Post(
                    UUID.fromString(rs.getString("id")),
                    UUID.fromString(rs.getString("account_id")),
                    rs.getString("post"),
                    rs.getTimestamp("creation_date_time").toLocalDateTime()
            );
        } catch (SQLException e) {
            log.error("Exception when data was extracting from ResultSet", e);
            return null;
        }
    }
}
