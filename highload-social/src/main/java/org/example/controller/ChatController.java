package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.exceptions.ForbiddenOperationException;
import org.example.service.chat.ChatService;
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
    public UUID createChat() {
        return service.createChat();
    }

    @GetMapping("/chat")
    public List<UUID> findChats(@RequestParam(value = "owned", defaultValue = "false") boolean owned) {
        return service.findChats(owned);
    }

    @DeleteMapping("/chat/{chatId}")
    public ResponseEntity<String> deleteChat(@PathVariable("chatId") UUID chatId) {
        try {
            service.deleteChat(chatId);
            return ResponseEntity.ok().build();
        } catch (ForbiddenOperationException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @GetMapping("/chat/{chatId}/member")
    public ResponseEntity<?> findMembers(@PathVariable("chatId") UUID chatId) {
        try {
            return ResponseEntity.ok(service.findMembers(chatId));
        } catch (ForbiddenOperationException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @PostMapping("/chat/{chatId}/member/{accountId}")
    public ResponseEntity<String> addMember(@PathVariable("chatId") UUID chatId, @PathVariable("accountId") UUID accountId) {
        try {
            service.addMember(chatId, accountId);
            return ResponseEntity.ok().build();
        } catch (ForbiddenOperationException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @DeleteMapping("/chat/{chatId}/member/{accountId}")
    public ResponseEntity<String> removeMember(@PathVariable("chatId") UUID chatId, @PathVariable("accountId") UUID accountId) {
        try {
            service.removeMember(chatId, accountId);
            return ResponseEntity.ok().build();
        } catch (ForbiddenOperationException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @PostMapping("/chat/{chatId}")
    public ResponseEntity<String> send(@PathVariable("chatId") UUID chatId, @RequestBody String message) {
        try {
            service.sendMessage(chatId, message);
            return ResponseEntity.ok().build();
        } catch (ForbiddenOperationException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @GetMapping("/chat/{chatId}")
    public ResponseEntity<?> receive(@PathVariable("chatId") UUID chatId) {
        try {
            return ResponseEntity.ok(service.findAllMessages(chatId));
        } catch (ForbiddenOperationException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }
}
