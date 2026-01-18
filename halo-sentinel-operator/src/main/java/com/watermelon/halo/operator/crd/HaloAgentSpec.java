package com.watermelon.halo.operator.crd;

import java.util.List;

import lombok.Data;

// 定义 Spec (用户填的配置)
@Data
public class HaloAgentSpec {
    private String targetDeployment; // 目标 Deployment 名字
    private List<String> enabledSkills; // 启用的技能
    // getters & setters
}