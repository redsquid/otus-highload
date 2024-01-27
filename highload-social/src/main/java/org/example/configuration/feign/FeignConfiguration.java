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
import org.example.client.auth.AuthFeignClient;
import org.example.client.chat.ChatFeignClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@Slf4j
@Configuration
public class FeignConfiguration {

    @Bean
    ChatFeignClient chatFeignClient(@Value("${higload.social.chat.host}") String host) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(
                        LocalDateTime.class,
                        (JsonDeserializer) (json, type, ctx) -> LocalDateTime.parse(json.getAsJsonPrimitive().getAsString())
                ).create();
        return Feign.builder()
                .encoder(new GsonEncoder(gson))
                .decoder(new GsonDecoder(gson))
                .logger(new Slf4jLogger(ChatFeignClient.class))
                .logLevel(Logger.Level.FULL)
                .target(ChatFeignClient.class, host);
    }

    @Bean
    AuthFeignClient authFeignClient(@Value("${higload.social.auth.host}") String host) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(
                        LocalDateTime.class,
                        (JsonDeserializer) (json, type, ctx) -> LocalDateTime.parse(json.getAsJsonPrimitive().getAsString())
                ).create();

        return Feign.builder()
                .encoder(new GsonEncoder(gson))
                .decoder(new GsonDecoder(gson))
                .logger(new Slf4jLogger(AuthFeignClient.class))
                .logLevel(Logger.Level.FULL)
                .target(AuthFeignClient.class, host);
    }
}
