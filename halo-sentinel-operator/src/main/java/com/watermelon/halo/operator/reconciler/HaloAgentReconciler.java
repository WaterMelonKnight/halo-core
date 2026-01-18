package com.watermelon.halo.operator.reconciler;

import io.fabric8.kubernetes.api.model.Container;
import io.fabric8.kubernetes.api.model.ContainerBuilder;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.javaoperatorsdk.operator.api.reconciler.Context;
import io.javaoperatorsdk.operator.api.reconciler.ControllerConfiguration;
import io.javaoperatorsdk.operator.api.reconciler.Reconciler;
import io.javaoperatorsdk.operator.api.reconciler.UpdateControl;

import com.watermelon.halo.operator.crd.HaloAgent;


@ControllerConfiguration
public class HaloAgentReconciler implements Reconciler<HaloAgent> {

    private final KubernetesClient client; // SDK 自动注入

    public HaloAgentReconciler(KubernetesClient client) {
        this.client = client;
    }

    @Override
    public UpdateControl<HaloAgent> reconcile(HaloAgent agent, Context<HaloAgent> context) {
        String targetName = agent.getSpec().getTargetDeployment();
        
        // 1. 找到目标 Deployment
        Deployment deployment = client.apps().deployments()
                .inNamespace(agent.getMetadata().getNamespace())
                .withName(targetName)
                .get();

        if (deployment != null) {
            // 2. 检查是否已经注入过 Sidecar (防止重复注入)
            boolean hasSidecar = deployment.getSpec().getTemplate().getSpec().getContainers()
                    .stream().anyMatch(c -> c.getName().equals("halo-sidecar"));

            if (!hasSidecar) {
                // 3. 创建 Sidecar 容器对象
                Container sidecar = new ContainerBuilder()
                        .withName("halo-sidecar")
                        .withImage("halo-sidecar:v1") // 用你刚才打的镜像
                        .withImagePullPolicy("IfNotPresent")
                        .build();

                // 4. 修改 Deployment，加入 Sidecar
                deployment.getSpec().getTemplate().getSpec().getContainers().add(sidecar);
                
                // 5. 应用更新到 K8s
                client.apps().deployments().resource(deployment).update();
                
                System.out.println("✅ 成功注入 Sidecar 到: " + targetName);
            }
        }
        
        return UpdateControl.noUpdate();
    }


}
