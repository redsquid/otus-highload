package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.exception.ForbiddenOperationException;
import org.example.repository.ChatRepository;
import org.example.repository.Message;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepo;

    @Transactional
    public UUID createChat(UUID accountId) {
        UUID chatId = chatRepo.insertChat(accountId);
        chatRepo.insertMember(chatId, accountId);
        return chatId;
    }

    public List<UUID> findChatsByOwner(UUID accountId) {
        return chatRepo.findChatByOwnerId(accountId);
    }

    public List<UUID> findChatsByMember(UUID accountId) {
        return chatRepo.findChatByMemberId(accountId);
    }

    public void deleteChat(UUID accountId, UUID chatId) throws ForbiddenOperationException {
        UUID ownerId = chatRepo.findOwnerByChatId(chatId);
        if (ownerId != null && ownerId.equals(accountId)) {
            chatRepo.deleteChatById(chatId);
        } else {
            throw new ForbiddenOperationException("You must be owner of the chat");
        }
    }

    public void addMember(UUID accountId, UUID chatId, UUID memberAccountId) throws ForbiddenOperationException {
        UUID ownerId = chatRepo.findOwnerByChatId(chatId);
        if (ownerId != null && ownerId.equals(accountId)) {
            if (!chatRepo.memberExist(chatId, memberAccountId)) {
                chatRepo.insertMember(chatId, memberAccountId);
            }
        } else {
            throw new ForbiddenOperationException("You must be owner of the chat");
        }
    }

    public void removeMember(UUID accountId, UUID chatId, UUID memberAccountId) throws ForbiddenOperationException {
        UUID ownerId = chatRepo.findOwnerByChatId(chatId);
        if (ownerId != null && (ownerId.equals(accountId) || ownerId.equals(memberAccountId))) {
            chatRepo.deleteMember(chatId, memberAccountId);
        } else {
            throw new ForbiddenOperationException("You must be owner of the chat");
        }
    }

    public List<UUID> findMembers(UUID accountId, UUID chatId) throws ForbiddenOperationException {
        if (chatRepo.memberExist(chatId, accountId)) {
            return chatRepo.findMembersByChatId(chatId);
        } else {
            throw new ForbiddenOperationException("You must be member of the chat");
        }
    }

    public void sendMessage(UUID accountId, UUID chatId, String message) throws ForbiddenOperationException {
        if (chatRepo.memberExist(chatId, accountId)) {
            Message m = new Message(null, chatId, accountId, message, LocalDateTime.now());
            chatRepo.insertMessage(m);
        } else {
            throw new ForbiddenOperationException("You must be member of the chat");
        }
    }

    public List<Message> findAllMessages(UUID accountId, UUID chatId) throws ForbiddenOperationException {
        if (chatRepo.memberExist(chatId, accountId)) {
            return chatRepo.findAllMessages(chatId);
        } else {
            throw new ForbiddenOperationException("You must be member of the chat");
        }
    }
}
