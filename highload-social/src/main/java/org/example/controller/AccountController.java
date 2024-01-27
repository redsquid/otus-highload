package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.domain.Account;
import org.example.domain.Registration;
import org.example.exceptions.UsernameAlreadyExistException;
import org.example.service.account.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping(value = "/account", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> create(@RequestBody Registration registration) {
        try {
            return ResponseEntity.ok(accountService.create(registration).toString());
        } catch (UsernameAlreadyExistException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exist");
        }
    }

    @GetMapping("/account/{id}")
    public ResponseEntity<Account> find(@PathVariable("id") UUID id) {
        Account account = accountService.find(id);
        return account == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(account);
    }

    @PostMapping("/account:search")
    public ResponseEntity<List<Account>> find(
            @RequestParam(value = "first", required = false) String firstName,
            @RequestParam(value = "last", required = false) String lastName
    ) {
        List<Account> accounts = accountService.findLike(firstName, lastName);
        return ResponseEntity.ok(accounts);
    }
}
