package org.example.configuration.feign;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import feign.Feign;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import org.example.client.chat.ChatFeignClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

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
                .target(ChatFeignClient.class, host);
    }
}
