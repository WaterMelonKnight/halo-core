package com.watermelon.halo.ghost.skills.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

import java.util.function.Function;

@Configuration
public class SkillsConfig {

    // 1. 定义函数的输入参数结构 (Record 是最佳实践)
    public record AlertRequest(String coinName, String reason, String urgency) {}

    // 2. 定义函数的返回结构
    public record AlertResponse(String status) {}

    @Bean("sendAlert")
    @Description("当市场行情出现剧烈波动（如跌幅超过阈值）、趋势反转或通过分析认为存在风险时，调用此工具进行报警通知") // <--- 这句话是给 DeepSeek 看的！
    public Function<AlertRequest, AlertResponse> sendAlert() {
        return request -> {
            // 这里写实际的业务逻辑，比如调用 Telegram Bot 或发邮件
            System.err.println("========================================");
            System.err.println("🚨 [SKILL TRIGGERED] 警报触发！");
            System.err.println("币种: " + request.coinName());
            System.err.println("原因: " + request.reason());
            System.err.println("紧急程度: " + request.urgency());
            System.err.println("========================================");
            
            return new AlertResponse("警报已发送给管理员");
        };
    }
}