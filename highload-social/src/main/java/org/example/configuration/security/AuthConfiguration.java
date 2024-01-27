package org.example.configuration.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AuthConfiguration implements WebMvcConfigurer {

    private final AuthInterceptor interceptor;

    public AuthConfiguration(@Lazy AuthInterceptor interceptor) {
        this.interceptor = interceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(interceptor).addPathPatterns(
                "/account/{id}",
                "/account:search",
                "/subscription/{publisherAccountId}",
                "/subscribers",
                "/publishers",
                "/post",
                "/feed",
                "/chat/**"
        );
    }
}
