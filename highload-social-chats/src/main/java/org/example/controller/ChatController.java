package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.exception.ForbiddenOperationException;
import org.example.service.ChatService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class ChatController {

    private final ChatService service;

    @PostMapping("/chat")
    public UUID createChat(@RequestParam UUID accountId) {
        return service.createChat(accountId);
    }

    @GetMapping("/chat")
    public List<UUID> findChats(@RequestParam UUID accountId, @RequestParam(defaultValue = "false") boolean owned) {
        return owned ? service.findChatsByOwner(accountId) : service.findChatsByMember(accountId);
    }

    @DeleteMapping("/chat/{chatId}")
    public ResponseEntity<String> deleteChat(@RequestParam UUID accountId, @PathVariable UUID chatId) {
        try {
            service.deleteChat(accountId, chatId);
            return ResponseEntity.ok().build();
        } catch (ForbiddenOperationException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @GetMapping("/chat/{chatId}/member")
    public ResponseEntity<?> findMembers(@RequestParam UUID accountId, @PathVariable UUID chatId) {
        try {
            return ResponseEntity.ok(service.findMembers(accountId, chatId));
        } catch (ForbiddenOperationException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @PostMapping("/chat/{chatId}/member/{memberAccountId}")
    public ResponseEntity<String> addMember(@RequestParam UUID accountId, @PathVariable UUID chatId, @PathVariable UUID memberAccountId) {
        try {
            service.addMember(accountId, chatId, memberAccountId);
            return ResponseEntity.ok().build();
        } catch (ForbiddenOperationException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @DeleteMapping("/chat/{chatId}/member/{memberAccountId}")
    public ResponseEntity<String> removeMember(@RequestParam UUID accountId, @PathVariable UUID chatId, @PathVariable UUID memberAccountId) {
        try {
            service.removeMember(accountId, chatId, memberAccountId);
            return ResponseEntity.ok().build();
        } catch (ForbiddenOperationException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @PostMapping("/chat/{chatId}")
    public ResponseEntity<String> send(@RequestParam UUID accountId, @PathVariable UUID chatId, @RequestBody String message) {
        try {
            service.sendMessage(accountId, chatId, message);
            return ResponseEntity.ok().build();
        } catch (ForbiddenOperationException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @GetMapping("/chat/{chatId}")
    public ResponseEntity<?> receive(@RequestParam UUID accountId, @PathVariable UUID chatId) {
        try {
            return ResponseEntity.ok(service.findAllMessages(accountId, chatId));
        } catch (ForbiddenOperationException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }
}
