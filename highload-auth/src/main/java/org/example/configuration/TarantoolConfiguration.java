package org.example.configuration;

import io.tarantool.driver.api.TarantoolClient;
import io.tarantool.driver.api.TarantoolClientFactory;
import io.tarantool.driver.api.TarantoolResult;
import io.tarantool.driver.api.tuple.TarantoolTuple;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TarantoolConfiguration {

    @Bean
    public TarantoolClient<TarantoolTuple, TarantoolResult<TarantoolTuple>> tarantoolClient(
            @Value("${highload.tarantool.host}") String host,
            @Value("${highload.tarantool.port}") int port
    ) {
        return TarantoolClientFactory.createClient()
                .withAddress(host, port)
                .withCredentials("highload", "highload")
                .build();
    }
}
