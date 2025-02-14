package com.bookstorage.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfig {
    @Bean
    public NewTopic bookCreatedTopic() {
        return new NewTopic("book_created", 1, (short) 1);
    }

    @Bean
    public NewTopic bookDeletedTopic() {
        return new NewTopic("book_deleted", 1, (short) 1);
    }
}
