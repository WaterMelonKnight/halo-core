package com.watermelon.halo.gateway;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
public class AIChatController {

    private final ChatClient chatClient;

    // 注入 Builder，这是 Spring AI 新版本的标准写法
    public AIChatController(ChatClient.Builder builder) {
        System.out.println("====== 🟢 AIChatController 被加载了！ ======"); // 显眼包日志
        this.chatClient = builder.build();
    }

    @PostMapping("/v1/chat/completions")
    public Mono<Map<String, Object>> chat(@RequestBody Map<String, String> body) {
        // 1. 获取用户输入
        String userMessage = body.getOrDefault("message", "Hello");

        // 2. 因为 Gateway 是 WebFlux (异步)，而目前的 Spring AI 底层是同步的，
        // 我们用 Mono.fromCallable 包装一下，防止阻塞主线程太久。
        return Mono.fromCallable(() -> {
            // ✅ 关键修复：使用 Fluent API 直接获取 String 内容
            // .user() -> .call() -> .content() 
            String aiResponse = chatClient.prompt()
                    .user(userMessage)
                    .call()
                    .content();
            
            return Map.of("choices", aiResponse); // 简单返回
        });
    }
}