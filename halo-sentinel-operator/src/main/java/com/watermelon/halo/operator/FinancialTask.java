package com.watermelon.halo.operator;

import io.fabric8.kubernetes.api.model.Namespaced;

public class FinancialTask implements Namespaced {
    private String apiVersion = "halo.watermelon.io/v1";
    private String kind = "FinancialTask";

    private Spec spec;

    public static class Spec {
        private String targetToken;
        private double threshold;

        // getters/setters
        public String getTargetToken() { return targetToken; }
        public void setTargetToken(String targetToken) { this.targetToken = targetToken; }
        public double getThreshold() { return threshold; }
        public void setThreshold(double threshold) { this.threshold = threshold; }
    }

    public Spec getSpec() { return spec; }
    public void setSpec(Spec spec) { this.spec = spec; }
}
