package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.entity.Account;
import org.example.entity.Advertisement;
import org.example.service.AdvertisementService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class Controller {

    private final AdvertisementService service;

    @PostMapping(value = "/account:register")
    public void register(@RequestBody Account account) {
        service.registerAccount(account);
    }

    @PostMapping(value = "/advertisement")
    public void advertise(@RequestBody Advertisement advertisement) {
        service.send(advertisement);
    }
}
