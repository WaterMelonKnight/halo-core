package com.watermelon.halo.operator;

import io.javaoperatorsdk.operator.api.reconciler.Context;
import io.javaoperatorsdk.operator.api.reconciler.ControllerConfiguration;
import io.javaoperatorsdk.operator.api.reconciler.Reconciler;
import io.javaoperatorsdk.operator.api.reconciler.UpdateControl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@ControllerConfiguration
public class FinancialTaskReconciler implements Reconciler<FinancialTask> {

    private static final Logger log = LoggerFactory.getLogger(FinancialTaskReconciler.class);

    @Override
    public UpdateControl<FinancialTask> reconcile(FinancialTask task, Context<FinancialTask> context) {
        // 1. 获取资源名称
        String taskName = task.getMetadata().getName();
        
        // 2. 这里的 getSpec() 可能为空，要做个防空判断
        if (task.getSpec() != null) {
            String token = task.getSpec().getTargetToken();
            log.info("⚡️ Reconciling FinancialTask: {} -> Monitoring Token: {}", taskName, token);
            
            // TODO: 这里未来会调用 Sidecar 注入逻辑
        } else {
            log.warn("⚠️ FinancialTask {} has no spec!", taskName);
        }

        // 3. 返回 NoUpdate 表示状态已同步，无需更新 CRD
        return UpdateControl.noUpdate();
    }
}