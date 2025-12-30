package com.watermelon.halo.operator;

import io.fabric8.kubernetes.api.model.Namespaced;
import io.fabric8.kubernetes.client.CustomResource;
import io.fabric8.kubernetes.model.annotation.Group;
import io.fabric8.kubernetes.model.annotation.Version;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Group("halo.watermelon.io") // CRD 的 Group
@Version("v1alpha1")         // CRD 的 Version
@Data
@EqualsAndHashCode(callSuper = true)
// 关键修复：必须继承 CustomResource，并指定 Spec 和 Status（如果没有 Status 用 Void）
public class FinancialTask extends CustomResource<FinancialTaskSpec, Void> implements Namespaced {
    
    // 这是一个空的构造函数，Fabric8 需要它
    public FinancialTask() {
        super();
    }
}