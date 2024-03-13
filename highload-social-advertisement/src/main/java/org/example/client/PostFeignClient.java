package org.example.client;

import feign.Body;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

import java.util.UUID;

public interface PostFeignClient {

    @RequestLine("POST /post")
    @Headers("Authorization: Bearer {authHeader}")
    @Body("{body}")
    UUID post(@Param String body, @Param String authHeader);
}
