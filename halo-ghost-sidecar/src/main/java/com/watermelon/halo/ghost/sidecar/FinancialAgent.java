package com.watermelon.halo.ghost.sidecar;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

@Component
public class FinancialAgent {

    private static final Logger log = LoggerFactory.getLogger(FinancialAgent.class);
    private final RestClient restClient;

    public FinancialAgent(RestClient.Builder builder) {
        // 连接到本地的 Gateway (端口 8080)
        this.restClient = builder.baseUrl("http://localhost:8080").build();
    }

    // 每 20 秒执行一次 (模拟高频交易分析)
    @Scheduled(fixedRate = 20000)
    public void analyzeMarket() {
        log.info("🕵️ [Agent] Starting market analysis task...");

        try {
            // 1. 构造发给 DeepSeek 的提示词
            String prompt = "我是量化交易员。请用一句话随机模拟分析当前的 Bitcoin 走势，风格要专业。";
            
            // 2. 调用网关 (Gateway)
            Map response = restClient.post()
                    .uri("/v1/chat/completions")
                    .body(Map.of("message", prompt))
                    .retrieve()
                    .body(Map.class);

            // 3. 处理结果
            if (response != null && response.containsKey("choices")) {
                String aiAdvice = response.get("choices").toString();
                log.info("🤖 [DeepSeek Advice]: {}", aiAdvice);
            }

        } catch (Exception e) {
            log.error("❌ Failed to call Gateway: {}", e.getMessage());
        }
    }
}