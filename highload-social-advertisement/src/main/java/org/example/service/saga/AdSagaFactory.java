package org.example.service.saga;

import lombok.RequiredArgsConstructor;
import org.example.client.PostFeignClient;
import org.example.client.WalletFeignClient;
import org.example.entity.Account;
import org.example.entity.Advertisement;
import org.example.infra.Saga;
import org.example.repository.AdvertisementRepository;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AdSagaFactory {

    private final WalletFeignClient walletFeignClient;

    private final PostFeignClient postFeignClient;

    private final AdvertisementRepository adRepo;

    public Saga create(Account account, int amount, UUID transactionId, Advertisement ad) {
        AdSagaContext context = new AdSagaContext();
        Saga saga = new Saga();
        saga.add(new PayPhase(walletFeignClient, account.getWalletId(), amount, transactionId));
        saga.add(new PostPhase(context, postFeignClient, ad.getText(), account.getToken()));
        saga.add(new SaveAdPhase(context, adRepo, ad));
        return saga;
    }
}
