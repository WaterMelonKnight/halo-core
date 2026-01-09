package com.watermelon.halo.gateway.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import java.time.LocalDateTime;

@Table("ai_provider_config") // 对应数据库表名

public class AiProviderConfig {
    @Id
    private Long id;
    private String providerName;
    private String baseUrl;
    private String apiKey;
    private Boolean isActive;
    private LocalDateTime updatedAt;

    // 省略 Getter/Setter 和 构造函数
    // 记得加上，或者用 @Data (如果你引入了 Lombok)
    public String getBaseUrl() { return baseUrl; }
    public String getApiKey() { return apiKey; }
    public Boolean getActive() { return isActive; }
}