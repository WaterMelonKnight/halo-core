package com.watermelon.halo.operator;

import lombok.Data;

@Data
public class FinancialTaskSpec {
    
    // 核心字段：监听的目标代币
    private String targetToken;
    
    // 核心字段：触发阈值
    private Double threshold;

    // ==========================================
    // 手动补全 Getter/Setter (双重保险，防止Lombok失效)
    // ==========================================
    
    public String getTargetToken() {
        return targetToken;
    }

    public void setTargetToken(String targetToken) {
        this.targetToken = targetToken;
    }

    public Double getThreshold() {
        return threshold;
    }

    public void setThreshold(Double threshold) {
        this.threshold = threshold;
    }
}