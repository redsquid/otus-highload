package org.example.service.chat;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.example.client.chat.ChatFeignClient;
import org.example.configuration.security.SecurityContextHolder;
import org.example.domain.Message;
import org.example.exceptions.ForbiddenOperationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatFeignClient client;

    public UUID createChat() {
        return client.createChat(SecurityContextHolder.getAccountId());
    }

    public List<UUID> findChats(boolean owned) {
        return client.findChats(SecurityContextHolder.getAccountId(), owned);
    }

    public void deleteChat(UUID chatId) throws ForbiddenOperationException {
        try {
            client.deleteChat(SecurityContextHolder.getAccountId(), chatId);
        } catch (FeignException.FeignClientException.Forbidden e) {
            throw new ForbiddenOperationException("You must be owner of the chat");
        }
    }

    public void addMember(UUID chatId, UUID memberAccountId) throws ForbiddenOperationException {
        try {
            client.addMember(SecurityContextHolder.getAccountId(), chatId, memberAccountId);
        } catch (FeignException.FeignClientException.Forbidden e) {
            throw new ForbiddenOperationException("You must be owner of the chat");
        }
    }

    public void removeMember(UUID chatId, UUID memberAccountId) throws ForbiddenOperationException {
        try {
            client.removeMember(SecurityContextHolder.getAccountId(), chatId, memberAccountId);
        } catch (FeignException.FeignClientException.Forbidden e) {
            throw new ForbiddenOperationException("You must be owner of the chat");
        }
    }

    public List<UUID> findMembers(UUID chatId) throws ForbiddenOperationException {
        try {
            return client.findMembers(SecurityContextHolder.getAccountId(), chatId);
        } catch (FeignException.FeignClientException.Forbidden e) {
            throw new ForbiddenOperationException("You must be member of the chat");
        }
    }

    public void sendMessage(UUID chatId, String message) throws ForbiddenOperationException {
        try {
            client.send(SecurityContextHolder.getAccountId(), chatId, message);
        } catch (FeignException.FeignClientException.Forbidden e) {
            throw new ForbiddenOperationException("You must be member of the chat");
        }
    }

    public List<Message> findAllMessages(UUID chatId) throws ForbiddenOperationException {
        try {
            return client.receive(SecurityContextHolder.getAccountId(), chatId);
        } catch (FeignException.FeignClientException.Forbidden e) {
            throw new ForbiddenOperationException("You must be member of the chat");
        }
    }
}
