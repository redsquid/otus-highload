package org.example.service.subscription;

import lombok.RequiredArgsConstructor;
import org.example.repository.SubscriptionRepository;
import org.example.service.feed.cache.RebuildingCacheManager;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository repo;

    private final RebuildingCacheManager rebuildingCacheManager;

    public void subscribe(UUID subscriberAccountId, UUID publisherAccountId) {
        if (repo.exist(subscriberAccountId, publisherAccountId)) {
            return;
        }
        repo.save(subscriberAccountId, publisherAccountId);
        rebuildingCacheManager.rebuildAllForAccount(subscriberAccountId);
    }

    public void unsubscribe(UUID subscriberAccountId, UUID publisherAccountId) {
        if (!repo.exist(subscriberAccountId, publisherAccountId)) {
            return;
        }
        repo.delete(subscriberAccountId, publisherAccountId);
        rebuildingCacheManager.rebuildAllForAccount(subscriberAccountId);
    }

    public List<UUID> findAllSubscribers(UUID accountId) {
        return repo.findAllSubscribers(accountId);
    }

    public List<UUID> findAllPublishers(UUID accountId) {
        return repo.findAllPublishers(accountId);
    }
}
