package org.example.service.feed.cache;


import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FeedCache {

    private static final int FEED_SIZE = 5;

    private final RedisTemplate<UUID, UUID> redisTemplate;

    private ListOperations<UUID, UUID> listOperations;

    @PostConstruct
    public void init() {
        this.listOperations = redisTemplate.opsForList();
    }

    public void addPost(UUID accountId, UUID postId) {
        listOperations.trim(accountId, 0, FEED_SIZE - 1);
        listOperations.leftPush(accountId, postId);
    }

    public void addPosts(UUID accountId, List<UUID> postIds) {
        int remains = FEED_SIZE < postIds.size() ? 0 : FEED_SIZE - postIds.size();
        listOperations.trim(accountId, 0, remains);
        listOperations.leftPushAll(accountId, postIds);
    }

    public List<UUID> feed(UUID accountId) {
        return listOperations.range(accountId, 0, FEED_SIZE - 1);
    }

    public void evict(UUID accountId) {
        redisTemplate.delete(List.of(accountId));
    }
}
