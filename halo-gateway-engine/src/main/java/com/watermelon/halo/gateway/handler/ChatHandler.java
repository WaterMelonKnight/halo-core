
package com.watermelon.halo.gateway.handler;
import com.watermelon.halo.gateway.model.AiProviderConfig;
import com.watermelon.halo.gateway.repository.AiProviderConfigRepository;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class ChatHandler {

    private final AiProviderConfigRepository repo;
    private final ChatClient.Builder clientBuilder;

    public ChatHandler(AiProviderConfigRepository repo, ChatClient.Builder clientBuilder) {
        this.repo = repo;
        this.clientBuilder = clientBuilder;
    }

    public Mono<ServerResponse> chat(ServerRequest request) {
        // 1. 从数据库异步读取激活的配置
        return repo.findFirstByIsActiveTrue()
            .flatMap(config -> {
                // 2. 拿到 Key 和 URL，现场构建 API 对象
                // 注意：Spring AI 目前的底层 OpenAiApi 可以手动 new
                var openAiApi = new OpenAiApi(config.getBaseUrl(), config.getApiKey());
                var chatModel = new OpenAiChatModel(openAiApi);
                var chatClient = clientBuilder.build(); // 这里其实需要把 chatModel 塞进去
                
                // Spring AI 的 API 设计这里稍微有点绕，
                // 如果是简单场景，我们可以直接用 OpenAiApi 来调用，绕过 ChatClient 封装
                // 或者重新构建 ChatModel
                
                // === 简单粗暴版 ===
                // 直接用 WebClient 拿 config 去调 DeepSeek，就像你最早做的那样。
                // 但既然用了 Spring AI，我们应该用它的类。
                
                // 推荐：暂时先只做 "启动时加载" (最简单)
                // 以后再做 "运行时热切换"
                return Mono.error(new RuntimeException("还没写完"));
            });
    }
}