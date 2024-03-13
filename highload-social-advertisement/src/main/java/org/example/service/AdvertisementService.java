package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.Account;
import org.example.entity.Advertisement;
import org.example.infra.PhaseException;
import org.example.infra.Saga;
import org.example.repository.AccountRepository;
import org.example.service.saga.AdSagaFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdvertisementService {

    private static final int AMOUNT_FOR_AD = 10;

    private final AccountRepository accountRepo;

    private final AdSagaFactory sagaFactory;

    public void registerAccount(Account account) {
        accountRepo.save(account);
    }

    public void send(Advertisement ad) {
        Account account = accountRepo.findByAccountId(ad.getAccountId());
        Saga saga = sagaFactory.create(account, AMOUNT_FOR_AD, UUID.randomUUID(), ad);
        try {
            saga.execute();
        } catch (PhaseException e) {
            log.error("Saga was failed", e);
        }
    }
}
