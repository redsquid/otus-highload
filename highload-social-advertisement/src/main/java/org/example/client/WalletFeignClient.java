package org.example.client;

import feign.Param;
import feign.RequestLine;

import java.util.UUID;

public interface WalletFeignClient {

    @RequestLine("POST /wallet-{walletId}/balance:add?amount={amount}&id={transactionId}")
    UUID pay(@Param UUID walletId, @Param int amount, @Param UUID transactionId);

    @RequestLine("POST /wallet/balance:cancel?id={transactionId}")
    UUID cancel(@Param UUID transactionId);
}
