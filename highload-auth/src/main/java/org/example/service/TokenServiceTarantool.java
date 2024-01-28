package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.exception.UsernameAlreadyExistException;
import org.example.repository.TarantoolCredentialsRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TokenServiceTarantool {

    private static final int LIFETIME_SECONDS = 100000;

    private final TarantoolCredentialsRepository repo;

    public String createToken(String username, String password) {
        return repo.createToken(username, password, LocalDateTime.now().plusSeconds(LIFETIME_SECONDS));
    }

    public void saveCredentials(String username, String password, UUID accountId) throws UsernameAlreadyExistException {
        repo.saveCredentials(username, password, accountId);
    }

    public UUID validateToken(String token) {
        if (token == null) {
            return null;
        }
        String[] data = token.split(":");
        if (data.length != 2) {
            return null;
        }
        try {
            return repo.validateToken(UUID.fromString(data[1]), LocalDateTime.now());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
