package org.example.client.auth;


import feign.Headers;
import feign.Param;
import feign.RequestLine;
import org.example.domain.Credential;

import java.util.UUID;

public interface AuthFeignClient {

    @RequestLine("POST /credentials")
    @Headers("Content-Type: application/json")
    void createCredentials(Credential credential);

    @RequestLine("POST /token:validate?token={token}")
    UUID validateToken(@Param("token") String token);
}
