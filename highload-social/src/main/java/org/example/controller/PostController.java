package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.configuration.security.SecurityContextHolder;
import org.example.service.post.PostService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService service;

    @PostMapping("/post")
    public UUID post(@RequestBody String data) {
        return service.post(SecurityContextHolder.getAccountId(), data);
    }
}
