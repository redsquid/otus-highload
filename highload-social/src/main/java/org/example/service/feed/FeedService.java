package org.example.service.feed;

import lombok.RequiredArgsConstructor;
import org.example.domain.Post;
import org.example.service.feed.cache.FeedCache;
import org.example.service.post.PostService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FeedService {

    private final FeedCache cache;

    private final PostService postService;

    public List<Post> feed(UUID accountId) {
        List<UUID> ids = cache.feed(accountId);
        return postService.findPosts(ids);
    }
}
