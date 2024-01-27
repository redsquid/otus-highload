package org.example.service.credentials;

import lombok.RequiredArgsConstructor;
import org.example.domain.Credential;
import org.example.repository.CredentialRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CredentialService {

    private static final int LIFETIME_SECONDS = 36000;

    private final CredentialRepository repo;

    public String createToken(String username, String password) {
        Credential c = repo.findByUsername(username);
        if (c == null || !c.getPassword().equals(password)) {
            return null;
        }
        String token = UUID.randomUUID().toString();
        repo.saveToken(c.getId(), token, LocalDateTime.now().plusSeconds(LIFETIME_SECONDS));
        return c.getAccountId() + ":" + token;
    }

    public void bind(String username, String password, UUID accountId) {
        repo.insert(username, password, accountId);
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
            UUID accountId = UUID.fromString(data[0]);
            return repo.unexpiredTokenIsExist(accountId, data[1]) ? accountId : null;
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public void delete(UUID accountId) {
        repo.delete(accountId);
    }

    public boolean exist(String userName) {
        return repo.exist(userName);
    }
}
