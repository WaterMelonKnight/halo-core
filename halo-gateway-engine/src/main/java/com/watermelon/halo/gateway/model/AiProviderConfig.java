package com.watermelon.halo.gateway.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import java.time.LocalDateTime;
import lombok.Data;


@Table("ai_provider_config") // 对应数据库表名
@Data
public class AiProviderConfig {
    @Id
    private Long id;
    private String providerName;
    private String baseUrl;
    private String apiKey;
    private Boolean isActive;
    private LocalDateTime updatedAt;
    private String model;
}