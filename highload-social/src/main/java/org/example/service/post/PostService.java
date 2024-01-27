package org.example.service.post;

import lombok.RequiredArgsConstructor;
import org.example.domain.Post;
import org.example.repository.PostRepository;
import org.example.service.feed.cache.RebuildingCacheManager;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository repo;

    private final RebuildingCacheManager feedCacheManager;

    private final AmqpTemplate ampqTemplate;

    public void post(UUID accountId, String data) {
        Post post = repo.insert(new Post(null, accountId, data, LocalDateTime.now()));
        feedCacheManager.rebuild(post);
        ampqTemplate.convertAndSend("amq.topic", accountId.toString(), post);
    }

    public List<Post> findPosts(List<UUID> ids) {
        return repo.findAll(ids);
    }
}
