package com.watermelon.halo.gateway.repository;

import com.watermelon.halo.gateway.model.AiProviderConfig;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface AiProviderConfigRepository extends R2dbcRepository<AiProviderConfig, Long> {
    // 查找当前激活的配置
    Mono<AiProviderConfig> findFirstByIsActiveTrue();
}