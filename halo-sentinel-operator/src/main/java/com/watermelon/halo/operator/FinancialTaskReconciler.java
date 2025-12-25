package com.watermelon.halo.operator;

import io.javaoperatorsdk.operator.api.reconciler.Reconciler;
import io.javaoperatorsdk.operator.api.reconciler.UpdateControl;
import io.javaoperatorsdk.operator.api.reconciler.ReconciliationContext;
import io.javaoperatorsdk.operator.api.reconciler.ControllerConfiguration;

@ControllerConfiguration
public class FinancialTaskReconciler implements Reconciler<FinancialTask> {
    @Override
    public UpdateControl<FinancialTask> reconcile(FinancialTask resource, ReconciliationContext context) throws Exception {
        // reconciliation skeleton: decide actions for FinancialTask
        return UpdateControl.noUpdate();
    }
}
