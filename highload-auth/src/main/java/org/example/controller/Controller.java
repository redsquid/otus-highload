package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import org.example.exception.UsernameAlreadyExistException;
import org.example.model.Credential;
import org.example.service.TokenService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class Controller {

    private final TokenService service;

    @PostMapping(value = "/credentials", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createCredentials(@RequestBody Credential credential) {
        try {
            service.saveCredentials(credential.getUsername(), credential.getPassword(), credential.getAccountId());
            return ResponseEntity.ok().build();
        } catch (UsernameAlreadyExistException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PostMapping("/token")
    public ResponseEntity<String> createToken(@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String value) {
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

    @PostMapping("/token:validate")
    public UUID validateToken(@RequestParam String token) {
        return service.validateToken(token);
    }
}
