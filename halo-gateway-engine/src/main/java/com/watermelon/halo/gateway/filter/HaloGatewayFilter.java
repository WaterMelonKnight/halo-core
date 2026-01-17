package com.watermelon.halo.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;

import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.watermelon.halo.gateway.repository.AiProviderConfigRepository;

import jakarta.annotation.Resource;
import reactor.core.publisher.Mono;

@Component
public class HaloGatewayFilter implements GlobalFilter, Ordered{
    // 注入你的 R2DBC Repository
    @Resource
    private AiProviderConfigRepository repo;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        
        // 只拦截去往 chat 的请求
        if (path.startsWith("/v1/chat") || path.startsWith("/chat")) {
            
            // 【关键点】：在这里使用 R2DBC 的响应式流，而不是 block()
            // 每次请求进来，都会去获取（或者获取缓存的）最新的配置
            return repo.findFirstByIsActiveTrue()
                    .switchIfEmpty(Mono.error(new RuntimeException("数据库里没配置 AI Key！"))) // 处理空的情况
                    .flatMap(config -> {
                        // 1. 拿到 Key
                        String deepseekKey = config.getApiKey();
                        
                        // 2. 打印日志 (实际生产建议 debug 级别)
                        System.out.println("⚡️ 动态获取 Key 成功: " + config.getProviderName());

                        // 3. 修改 Header 并转发
                        return chain.filter(
                                exchange.mutate()
                                        .request(builder -> builder.header("Authorization", "Bearer " + deepseekKey))
                                        .build()
                        );
                    });
        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
