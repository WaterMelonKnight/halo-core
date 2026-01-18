
package com.watermelon.halo.operator.crd;

// 1. 注意这里使用的是 fabric8 的 model annotation，而不是 javaoperatorsdk 的 api
import io.fabric8.kubernetes.model.annotation.Group;
import io.fabric8.kubernetes.model.annotation.Version;
import io.fabric8.kubernetes.model.annotation.Kind;
import io.fabric8.kubernetes.client.CustomResource;
import io.fabric8.kubernetes.api.model.Namespaced; // 加上这个，表示资源是属于某个 namespace 的

@Group("halo.cloud")
@Version("v1")
@Kind("HaloAgent")
public class HaloAgent extends CustomResource<HaloAgentSpec, HaloAgentStatus> {
    // 这是一个标准 CRD 类，继承 CustomResource
}
