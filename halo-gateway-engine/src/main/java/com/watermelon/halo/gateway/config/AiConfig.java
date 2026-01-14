package com.watermelon.halo.gateway.config;

import com.watermelon.halo.gateway.model.AiProviderConfig;
import com.watermelon.halo.gateway.repository.AiProviderConfigRepository;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient; // 即使不用Bean，导入也没事
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.time.Duration;

@Configuration
public class AiConfig {

    @Resource
    private AiProviderConfigRepository aiConfigRepository;

    @Bean
    public OpenAiChatModel openAiChatModel() {
        // 改进点 1: 显式阻塞 (Block)
        // R2DBC 是异步的，但在 Spring 启动初始化 Bean 时，我们需要同步拿到结果。
        // 这里必须使用 .block() 把 Mono<AiProviderConfig> 变成 AiProviderConfig 对象。
        // 设置 10秒超时，防止数据库连不上导致启动卡死
        AiProviderConfig config = aiConfigRepository.findFirstByIsActiveTrue()
                .block(Duration.ofSeconds(10));

        // 改进点 2: 空值检查 (Null Safety)
        // block() 如果没查到数据会返回 null，必须处理，否则下面 getApiKey 会报空指针
        if (config == null) {
            throw new RuntimeException("❌ 启动失败：数据库表 [ai_provider_config] 中没有激活的配置 (isActive=true)！");
        }

        System.out.println("🚀 [Halo Gateway] 已加载 AI 配置: " + config.getProviderName());

        // 改进点 3: 健壮性检查 (可选)
        if (!StringUtils.hasText(config.getApiKey())) {
             throw new RuntimeException("❌ 启动失败：获取到的 API Key 为空！");
        }

        // --- 以下构建逻辑不变 ---
        
        // 构建 API 连接对象 (DeepSeek 兼容 OpenAI)
        OpenAiApi openAiApi = new OpenAiApi(config.getBaseUrl(), config.getApiKey());

        // 构建 ChatModel
        OpenAiChatOptions options = OpenAiChatOptions.builder()
                .withModel(config.getModel())
                .withTemperature((float) 0.7)
                .build();

        return new OpenAiChatModel(openAiApi, options);
    }
}