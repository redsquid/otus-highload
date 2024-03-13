package org.example.service.saga;

import lombok.Data;

import java.util.UUID;

@Data
public class AdSagaContext {

    private UUID postId;
}
