package org.example.configuration.persistent;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class SlaveConfiguration {

    @Bean
    @ConfigurationProperties("spring.datasource.slave0")
    public DataSourceProperties slave0DataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.slave0.hikari")
    public DataSource slave0DataSource() {
        return slave0DataSourceProperties()
                .initializeDataSourceBuilder()
                .build();
    }

    @Bean
    public NamedParameterJdbcTemplate slaveJdbcTemplate() {
        return new NamedParameterJdbcTemplate(slave0DataSource());
    }
}
