package org.example.client.chat;

import feign.Body;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import org.example.domain.Message;

import java.util.List;
import java.util.UUID;

public interface ChatFeignClient {

    @RequestLine("POST /chat?accountId={accountId}")
    UUID createChat(@Param UUID accountId);

    @RequestLine("GET /chat?accountId={accountId}&owned={owned}")
    List<UUID> findChats(@Param UUID accountId, @Param boolean owned);

    @RequestLine("DELETE /chat/{chatId}?accountId={accountId}")
    void deleteChat(@Param UUID accountId, @Param UUID chatId);

    @RequestLine("POST /chat/{chatId}/member/{memberAccountId}?accountId={accountId}")
    void addMember(@Param UUID accountId, @Param UUID chatId, @Param UUID memberAccountId);

    @RequestLine("DELETE /chat/{chatId}/member/{memberAccountId}?accountId={accountId}")
    void removeMember(@Param UUID accountId, @Param UUID chatId, @Param UUID memberAccountId);

    @RequestLine("GET /chat/{chatId}/member?accountId={accountId}")
    List<UUID> findMembers(@Param UUID accountId, @Param UUID chatId);

    @RequestLine("POST /chat/{chatId}?accountId={accountId}")
    @Body("{message}")
    @Headers("Content-Type: application/json")
    void send(@Param UUID accountId, @Param UUID chatId, @Param("message") String message);

    @RequestLine("GET /chat/{chatId}?accountId={accountId}")
    List<Message> receive(@Param UUID accountId, @Param UUID chatId);
}
