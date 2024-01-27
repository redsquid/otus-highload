package org.example.service.feed.cache;

import lombok.RequiredArgsConstructor;
import org.example.domain.Post;
import org.example.repository.PostRepository;
import org.example.repository.SubscriptionRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
@RequiredArgsConstructor
public class RebuildingCacheManager {

    private static final int SUBSCRIBER_PAGE_SIZE = 100;

    private static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(4);

    private final SubscriptionRepository subscriptionRepo;

    private final PostRepository postRepo;

    private final FeedCache cache;

    public void rebuild(Post post) {
        EXECUTOR.submit(() -> {
            List<UUID> subscribers;
            int i = 0;
            do {
                subscribers = subscriptionRepo.findAllSubscribers(post.getAccountId(), i, SUBSCRIBER_PAGE_SIZE);
                subscribers.forEach(s -> EXECUTOR.submit(() -> cache.addPost(s, post.getId())));
                i++;
            } while (!subscribers.isEmpty());
        });
    }

    public void rebuildAllForAccount(UUID accountId) {
        EXECUTOR.submit(() -> {
            cache.evict(accountId);
            cache.addPosts(accountId, postRepo.extractFeed(accountId));
        });
    }
}
