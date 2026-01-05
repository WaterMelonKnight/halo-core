package com.watermelon.halo.ghost.sidecar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Component
public class FinancialAgent {

    private static final Logger log = LoggerFactory.getLogger(FinancialAgent.class);
    private final RestClient gatewayClient;
    private final CryptoService cryptoService; // 注入新服务

    // 构造函数注入
    public FinancialAgent(RestClient.Builder builder, CryptoService cryptoService) {
        this.gatewayClient = builder.baseUrl("http://localhost:8080").build();
        this.cryptoService = cryptoService;
    }

    @Scheduled(fixedRate = 15000) // 改成 15秒一次
    public void analyzeMarket() {
        log.info("🕵️ [Agent] Fetching real-world market data...");

        try {
            // 1. 获取真实价格
            String btcPrice = cryptoService.getBitcoinPrice();
            String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));

            // 2. 构造基于事实的 Prompt
            String prompt = String.format(
                "现在时间是 %s，比特币(BTC)的实时价格是 %s USDT。请扮演一位激进的华尔街交易员，用简短、犀利的一句话点评当前价格，并给出'买入'或'卖出'的各种搞怪理由。",
                time, btcPrice
            );

            // 3. 调用网关
            Map response = gatewayClient.post()
                    .uri("/v1/chat/completions")
                    .body(Map.of("message", prompt))
                    .retrieve()
                    .body(Map.class);

            if (response != null && response.containsKey("choices")) {
                log.info("📈 [BTC Price]: ${}", btcPrice);
                log.info("🤖 [DeepSeek]: {}", response.get("choices"));
            }

        } catch (Exception e) {
            log.error("❌ Task failed: {}", e.getMessage());
        }
    }
}