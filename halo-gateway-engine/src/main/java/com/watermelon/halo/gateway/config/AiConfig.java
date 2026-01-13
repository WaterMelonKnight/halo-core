package com.watermelon.halo.gateway.config;

import com.watermelon.halo.gateway.repository.AiProviderConfigRepository;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import reactor.core.publisher.Mono;

@Configuration
public class AiConfig {

    /**
     * 这里我们不用传统的 @Bean 直接返回 ChatClient，
     * 因为我们是 WebFlux 且配置在数据库里，读取是异步的。
     * * 方案：我们注册一个 "AI 能力提供者" 服务，
     * 每次请求时，它去数据库拿最新的 Key，然后现场造一个 Client。
     */
    
    // 注意：为了演示简单，这里我们还是用一种“启动时加载”的变体，
    // 或者更高级的：在业务逻辑里动态构建。
    
    // 咱们先用最稳妥的方案：让 Gateway 里的 Handler 去动态获取配置。
}