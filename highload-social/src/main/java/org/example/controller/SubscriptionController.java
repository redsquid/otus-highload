package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.configuration.security.SecurityContextHolder;
import org.example.service.subscription.SubscriptionService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService service;

    @PostMapping(value = "/subscription/{publisherAccountId}")
    public void subscribe(@PathVariable("publisherAccountId") UUID publisherAccountId) {
        service.subscribe(SecurityContextHolder.getAccountId(), publisherAccountId);
    }

    @DeleteMapping(value = "/subscription/{publisherAccountId}")
    public void unsubscribe(@PathVariable("publisherAccountId") UUID publisherAccountId) {
        service.unsubscribe(SecurityContextHolder.getAccountId(), publisherAccountId);
    }

    @GetMapping(value = "/subscribers")
    public List<UUID> findAllSubscribers() {
        return service.findAllSubscribers(SecurityContextHolder.getAccountId());
    }

    @GetMapping(value = "/publishers")
    public List<UUID> findAllPublishers() {
        return service.findAllPublishers(SecurityContextHolder.getAccountId());
    }
}
