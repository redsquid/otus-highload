package org.example.support;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.Account;
import org.example.repository.AccountRepository;
import org.example.repository.SubscriptionRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubscriptionGenerator {

    private static final int SUBSCRIBERS = 100;

    private final ExecutorService executor = Executors.newFixedThreadPool(10);

    private final AccountRepository accountRepo;

    private final SubscriptionRepository subscriptionRepo;

    public void generate() {
        Account[] accounts = accountRepo.findLike("", "").toArray(new Account[0]);
        Arrays.sort(accounts, Comparator.comparingInt(o -> Integer.parseInt(o.getFirstName().replaceAll("firstName", ""))));
        for (int i = 0; i < accounts.length; i++) {
            final int n = i;
            executor.submit(() -> {
                for (int j = 0; j < SUBSCRIBERS; j++) {
                    int p = n + j + 1;
                    if (p >= accounts.length) {
                        p = p - accounts.length;
                    }
                    subscriptionRepo.save(accounts[n].getId(), accounts[p].getId());
                }
               log.info("save subscription for account: " + n);
            });
        }
        log.info("Completed");
    }
}
