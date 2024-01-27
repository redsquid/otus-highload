package org.example.service.account;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.example.client.auth.AuthFeignClient;
import org.example.domain.Account;
import org.example.domain.Credential;
import org.example.domain.Registration;
import org.example.exceptions.UsernameAlreadyExistException;
import org.example.repository.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AuthFeignClient authFeignClient;

    private final AccountRepository repo;

    @Transactional(rollbackFor = Exception.class)
    public UUID create(Registration registration) throws UsernameAlreadyExistException {
        UUID id = repo.insert(registration.getAccount()).getId();
        try {
            authFeignClient.createCredentials(new Credential(null, registration.getUsername(), registration.getPassword(), id));
        } catch (FeignException.FeignClientException.Conflict e) {
            throw new UsernameAlreadyExistException();
        }
        return id;
    }

    public void delete(UUID accountId) {
        repo.delete(accountId);
    }

    public Account find(UUID accountId) {
        return repo.findById(accountId);
    }

    public List<Account> findLike(String firstName, String lastName) {
        return repo.findLike(firstName, lastName);
    }
}
