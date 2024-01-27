package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.configuration.security.SecurityContextHolder;
import org.example.domain.Post;
import org.example.service.feed.FeedService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class FeedController {

    private final FeedService service;

    @GetMapping("/feed")
    public List<String> feed() {
        return service.feed(SecurityContextHolder.getAccountId()).stream()
                .map(Post::getPost)
                .toList();
    }
}
