package org.example.support;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.Account;
import org.example.domain.Post;
import org.example.repository.AccountRepository;
import org.example.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostGenerator {

    private static final int POSTS = 50;

    private final ExecutorService executor = Executors.newFixedThreadPool(10);

    private final AccountRepository accountRepo;

    private final PostRepository postRepo;

    public void generate() {
        Account[] accounts = accountRepo.findLike("", "").toArray(new Account[0]);
        LocalDateTime date = LocalDateTime.of(2023, 1, 10, 0, 0 , 0);
        for (int i = 0; i < accounts.length; i++) {
            final int n = i;
            date = date.plusSeconds(1);
            final LocalDateTime current = date;
            executor.submit(() -> {
                LocalDateTime d = current;
                for (int p = 0; p < POSTS; p++) {
                    d = d.plusDays(n);
                    postRepo.insert(new Post(null, accounts[n].getId(), accounts[n].getFirstName() + " post: " + p, d));
                }
                log.info("saved post for account: " + n);
            });
        }
        log.info("Completed");
    }
}
