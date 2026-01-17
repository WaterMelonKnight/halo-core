package com.watermelon.halo.ghost.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration

public class ChatConfig {
    @Bean
    public ChatClient chatClient(ChatClient.Builder builder) {
        // 这里利用 Spring 自动注入的 Builder 来构建一个默认的 ChatClient
        return builder.build();
    }
}
