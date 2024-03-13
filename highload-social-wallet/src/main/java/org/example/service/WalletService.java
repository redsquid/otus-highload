package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.repository.WalletRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository repo;

    public UUID createWallet(UUID accountId) {
        return repo.insertWallet(accountId);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ, rollbackFor = Exception.class)
    public void executeTransaction(UUID transactionKey, UUID walletId, int amount) {
        int balance = repo.selectForUpdateBalance(walletId);
        balance += amount;
        repo.updateWallet(walletId, balance);
        repo.insertTransaction(transactionKey, walletId, amount);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ, rollbackFor = Exception.class)
    public void cancelTransaction(UUID id) {
        Transaction transaction = repo.selectTransactionAmount(id);
        int balance = repo.selectForUpdateBalance(transaction.getWalletId());
        balance -= transaction.getAmount();
        repo.updateWallet(transaction.getWalletId(), balance);
        repo.deleteTransaction(id);
    }
}
