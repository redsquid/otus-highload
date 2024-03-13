package org.example.service.saga;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.example.client.WalletFeignClient;
import org.example.infra.Phase;
import org.example.infra.PhaseException;

import java.util.UUID;

@RequiredArgsConstructor
class PayPhase implements Phase {

    private final WalletFeignClient client;

    private final UUID walletId;

    private final int amount;

    private final UUID transactionId;

    @Override
    public void execute() throws PhaseException {
        try {
            client.pay(walletId, amount, transactionId);
        } catch (Exception e) {
            throw new PhaseException(e);
        }
    }

    @Override
    public void cancel() {
        try {
            client.cancel(transactionId);
        } catch (FeignException.FeignClientException e) {
            //TODO repeat later
        }
    }
}
