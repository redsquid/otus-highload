package org.example.service.saga;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.client.PostFeignClient;
import org.example.infra.Phase;
import org.example.infra.PhaseException;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
class PostPhase implements Phase {

    private final AdSagaContext context;

    private final PostFeignClient client;

    private final String message;

    private final String token;

    @Override
    public void execute() throws PhaseException {
        try {
            UUID postId = client.post(message, token);
            context.setPostId(postId);
        } catch (Exception e) {
            log.error("PostPhase exception", e);
            throw new PhaseException(e);
        }
    }

    @Override
    public void cancel() {
    }
}
