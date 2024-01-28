package org.example.repository;

import io.tarantool.driver.api.TarantoolClient;
import io.tarantool.driver.api.TarantoolResult;
import io.tarantool.driver.api.tuple.TarantoolTuple;
import lombok.RequiredArgsConstructor;
import org.example.exception.UsernameAlreadyExistException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Component
@RequiredArgsConstructor
public class TarantoolCredentialsRepository {

    private final TarantoolClient<TarantoolTuple, TarantoolResult<TarantoolTuple>> tarantoolClient;

    public String createToken(String username, String password, LocalDateTime expiryDate) {
        try {
            return tarantoolClient.callForSingleResult(
                    "createToken",
                    List.of(username, password, expiryDate.atZone(ZoneId.systemDefault()).toEpochSecond()),
                    String.class).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            throw new RuntimeException("Something wrong");
        }
    }

    public void saveCredentials(String username, String password, UUID accountId) throws UsernameAlreadyExistException {
        try {
            boolean success = tarantoolClient.callForSingleResult(
                    "createCredential",
                    List.of(UUID.randomUUID(), username, password, accountId),
                    Boolean.class).get();
            if (!success) {
                throw new UsernameAlreadyExistException();
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            throw new RuntimeException("Something wrong");
        }
    }

    public UUID validateToken(UUID token, LocalDateTime now) {
        try {
            return tarantoolClient.callForSingleResult(
                    "validateToken",
                    List.of(token, now.atZone(ZoneId.systemDefault()).toEpochSecond()),
                    UUID.class).get();
        } catch (IllegalArgumentException e) {
            return null;
        }  catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            throw new RuntimeException("Something wrong");
        }
    }
}
