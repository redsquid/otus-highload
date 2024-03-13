package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.service.WalletService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class Controller {

    private final WalletService service;

    @PostMapping(value = "/wallet")
    public UUID create(@RequestParam("accountId")UUID accountId) {
        return service.createWallet(accountId);
    }

    @PostMapping(value = "/wallet-{walletId}/balance:add")
    public void addBalance(@PathVariable("walletId") UUID walletId, @RequestParam("amount") Integer amount, @RequestParam("id") UUID transactionId) {
        service.executeTransaction(transactionId, walletId, amount);
    }

    @PostMapping(value = "/wallet/balance:cancel")
    public void addBalance(@RequestParam("id") UUID transactionId) {
        service.cancelTransaction(transactionId);
    }
}
