package org.example.service.account;

import lombok.RequiredArgsConstructor;
import org.example.domain.Account;
import org.example.domain.Registration;
import org.example.exceptions.UsernameAlreadyExistException;
import org.example.repository.AccountRepository;
import org.example.service.credentials.CredentialService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final CredentialService credentialService;

    private final AccountRepository repo;

    public UUID create(Registration registration) throws UsernameAlreadyExistException {
        if (credentialService.exist(registration.getUsername())) {
            throw new UsernameAlreadyExistException();
        }
        UUID id = repo.insert(registration.getAccount()).getId();
        credentialService.bind(registration.getUsername(), registration.getPassword(), id);
        return id;
    }

    @Transactional
    public void delete(UUID accountId) {
        credentialService.delete(accountId);
        repo.delete(accountId);
    }

    public Account find(UUID accountId) {
        return repo.findById(accountId);
    }

    public List<Account> findLike(String firstName, String lastName) {
        return repo.findLike(firstName, lastName);
    }
}
