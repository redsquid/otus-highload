package org.example.configuration.feign;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import feign.Feign;
import feign.Logger;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.slf4j.Slf4jLogger;
import lombok.extern.slf4j.Slf4j;
import org.example.client.PostFeignClient;
import org.example.client.WalletFeignClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@Slf4j
@Configuration
public class FeignConfiguration {

    @Bean
    PostFeignClient postFeignClient(@Value("${higload.social.post.host}") String host) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(
                        LocalDateTime.class,
                        (JsonDeserializer) (json, type, ctx) -> LocalDateTime.parse(json.getAsJsonPrimitive().getAsString())
                ).create();
        return Feign.builder()
                .encoder(new GsonEncoder(gson))
                .decoder(new GsonDecoder(gson))
                .logger(new Slf4jLogger(PostFeignClient.class))
                .logLevel(Logger.Level.FULL)
                .target(PostFeignClient.class, host);
    }

    @Bean
    WalletFeignClient walletFeignClient(@Value("${higload.social.wallet.host}") String host) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(
                        LocalDateTime.class,
                        (JsonDeserializer) (json, type, ctx) -> LocalDateTime.parse(json.getAsJsonPrimitive().getAsString())
                ).create();
        return Feign.builder()
                .encoder(new GsonEncoder(gson))
                .decoder(new GsonDecoder(gson))
                .logger(new Slf4jLogger(WalletFeignClient.class))
                .logLevel(Logger.Level.FULL)
                .target(WalletFeignClient.class, host);
    }
}
