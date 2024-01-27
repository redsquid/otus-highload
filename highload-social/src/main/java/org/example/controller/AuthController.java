package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.binary.Base64;
import org.example.service.credentials.CredentialService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final CredentialService service;

    @GetMapping(path = "/token")
    public ResponseEntity<String> token(@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String value) {
        if (value == null || !value.matches("Basic .+")) {
            return unauthorized();
        }
        String[] creds = new String(Base64.decodeBase64(value.substring(6))).split(":");
        if (creds.length != 2) {
            return unauthorized();
        }
        String token = service.createToken(creds[0], creds[1]);
        return token == null ? ResponseEntity.status(HttpStatus.FORBIDDEN).build() : ResponseEntity.ok(token);
    }

    private ResponseEntity<String> unauthorized() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .header("WWW-Authenticate", "Basic realm=\"Access to resource\"")
                .build();
    }
}
