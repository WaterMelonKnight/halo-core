package com.watermelon.halo.ghost.sidecar;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class FinancialAgent {

    private static final Logger log = LoggerFactory.getLogger(FinancialAgent.class);
    
    private final CryptoService cryptoService;
    private final RedissonClient redissonClient;
    // ✅ 引入 ChatClient (这是 Spring AI 的智能大脑)
    private final ChatClient chatClient;
    // 构造函数注入
    public FinancialAgent(ChatClient.Builder builder, CryptoService cryptoService, RedissonClient redissonClient, ChatClient chatClient) {
        // this.gatewayClient = builder.baseUrl("http://localhost:8080").build();
        this.cryptoService = cryptoService;
        this.redissonClient = redissonClient;
        // 🔥 【大脑搬家核心】在这里定义 Prompt 和连接配置
        this.chatClient = builder
                // 1. 设置网关地址：假装网关就是 DeepSeek
                // 注意：这里需要配合 application.yml 配置 base-url，或者在这里硬编码 .baseUrl(...)
                // 建议在 yml 配置: spring.ai.openai.base-url=http://localhost:8080
                
                // 2. 注入“人设” (System Prompt)
                .defaultSystem("""
                    你是一个专业的 Web3 风险控制 AI Agent，代号 "Financial Sentinel"。
                    你的职责是监控市场数据。
                    如果发现行情剧烈波动（例如短时跌幅超过 5% 或 RSI < 30），请**务必**调用报警工具通知用户。
                    不要犹豫，宁可错报，不可漏报。
                    """)
                .build();
    }

    private void performAnalysis(){
        log.info("🕵️ [Agent] Fetching real-world market data...");

        try {
            // 1. 获取真实价格
            String btcPrice = cryptoService.getBitcoinPrice();
            String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));

            // 2. 构造基于事实的 Prompt
            String promptData = String.format(
                "现在时间是 %s，比特币(BTC)的实时价格是 %s USDT。请扮演一位激进的华尔街交易员，用简短、犀利的一句话点评当前价格，并给出'买入'或'卖出'的各种搞怪理由。",
                time, btcPrice
            );
            // String promptData = "Target: BTC/USDT  Current Price: $82,000 1h Change: -5.8%  <-- 巨大的跌幅 24h Change: -12.5% RSI (14): 22 (严重超卖) Volume: 异常放量 (Sell Wall detected)";
            // 3. 调用网关
            // 2. ✅ 调用 AI (使用 ChatClient)
            String response = chatClient.prompt()
                    .user(u -> u.text("当前市场数据如下：\n{data}\n请分析风险。")
                            .param("data", promptData))
                    
                    // 🔥🔥 【关键】挂载我们在 Config 里定义的 Skill
                    // 这会让 DeepSeek 知道它有了“报警”的能力
                    .functions("sendAlert") 
                    .call()
                    .content();

            log.info("🤖 [DeepSeek 决策结果]: {}", response);


        } catch (Exception e) {
            log.error("❌ Task failed: {}", e.getMessage());
        }
    }

    @Scheduled(fixedRate = 15000) // 改成 15秒一次
    public void analyzeMarket() {
    // 定义一个锁的 key，比如 "task:market-analysis"
        RLock lock = redissonClient.getLock("halo:sidecar:task:market-analysis");

        // 尝试抢锁：等待 0 秒，锁过期时间 9 秒（任务间隔10秒，所以9秒后自动释放给下一轮）
        try {
            if (lock.tryLock(0, 9, TimeUnit.SECONDS)) {
                log.info("🔒 抢到锁了！我是 Leader，开始干活...");
                // === 这里放你之前的 DeepSeek 调用逻辑 ===
                performAnalysis(); 
            } else {
                log.info("✋ 没抢到锁，其他 Sidecar 正在干活，我休息。");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // 注意：因为设置了自动过期，这里其实可以不手动 unlock，
            // 或者判断 if(lock.isHeldByCurrentThread()) lock.unlock();
        }
    }
}