package com.watermelon.halo.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.client.RestClient;
import org.springframework.context.annotation.Bean;
import com.watermelon.halo.gateway.repository.AiProviderConfigRepository;
import com.watermelon.halo.gateway.model.AiProviderConfig;

import org.springframework.context.annotation.Primary;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
    // ✅ 修复核心：手动注入 RestClient.Builder
    // 这样 Spring AI 的自动配置就能找到了，不会报错
    @Bean
    public RestClient.Builder restClientBuilder() {
        return RestClient.builder();
    }

}
