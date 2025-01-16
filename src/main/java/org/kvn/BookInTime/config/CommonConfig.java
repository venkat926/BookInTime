package org.kvn.BookInTime.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.SimpleMailMessage;

@Configuration
public class CommonConfig {

    @Bean
    public SimpleMailMessage simpleMailMessage() {
        return new SimpleMailMessage();
    }
}
