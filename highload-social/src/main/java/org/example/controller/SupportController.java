package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.service.feed.cache.PrimingCacheManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SupportController {

    private final PrimingCacheManager service;

    @PostMapping(value = "/cache:prime")
    public void findAllPublishers() {
        service.prime();
    }
}
