package org.example.service.feed.cache;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.repository.AccountRepository;
import org.example.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
@RequiredArgsConstructor
public class PrimingCacheManager {

    private static final int PAGE_SIZE = 10000;

    private static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(10);

    private final AccountRepository accountRepo;

    private final PostRepository postRepo;

    private final FeedCache cache;

    public void prime() {
        EXECUTOR.submit(() -> {
            log.info("Cache priming has been started");
            Logger logger = new Logger();
            List<UUID> accountIds;
            int i = 0;
            int total = 0;
            do {
                accountIds = accountRepo.findAllIds(i, PAGE_SIZE);
                accountIds.forEach(id -> EXECUTOR.submit(() -> {
                    cache.addPosts(id, postRepo.extractFeed(id));
                    logger.log();
                }));
                i++;
                total += accountIds.size();
            } while (!accountIds.isEmpty());
            log.info("{} accounts sent to prime", total);
        });
    }

    @Slf4j
    private static class Logger {

        private final AtomicInteger count = new AtomicInteger(0);

        public void log() {
            int n = count.addAndGet(1);
            log.info("{} feeds was primed", n);
        }
    }
}
