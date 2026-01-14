package com.watermelon.halo.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.client.RestClient;
import org.springframework.context.annotation.Bean;
import com.watermelon.halo.gateway.repository.AiProviderConfigRepository;
import com.watermelon.halo.gateway.model.AiProviderConfig;
import org.springframework.ai.openai.api.OpenAiApi;
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

    @Bean
    @Primary
    public OpenAiApi openAiApi(AiProviderConfigRepository repo) {
        // 阻塞式读取（仅在启动时执行一次，为了初始化 Bean）
        // 注意：R2DBC 是异步的，这里为了 @Bean 初始化可能需要 block 一下，或者用 CommandLineRunner 初始化
        AiProviderConfig config = repo.findFirstByIsActiveTrue().block();
        
        if (config == null) {
            throw new RuntimeException("数据库里没配置 AI Key！快去 insert 一条！");
        }
        
        System.out.println("🚀 已从数据库加载 AI 配置: " + config.getProviderName());
        
        // 使用数据库里的参数初始化 DeepSeek (OpenAI 兼容模式)
        return new OpenAiApi(config.getBaseUrl(), config.getApiKey());
    }
}
