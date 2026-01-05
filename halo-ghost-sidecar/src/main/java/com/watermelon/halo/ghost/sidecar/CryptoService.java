package com.watermelon.halo.ghost.sidecar;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import java.util.Map;

@Service
public class CryptoService {

    private final RestClient restClient;

    public CryptoService() {
        // 使用 Binance 的公开 API，不需要 Key
        this.restClient = RestClient.create();
    }

    public String getBitcoinPrice() {
        try {
            // 获取 BTC/USDT 实时价格
            // 返回格式: {"symbol":"BTCUSDT","price":"42000.00"}
            Map response = restClient.get()
                    .uri("https://api.binance.com/api/v3/ticker/price?symbol=BTCUSDT")
                    .retrieve()
                    .body(Map.class);
            
            if (response != null && response.get("price") != null) {
                return response.get("price").toString();
            }
        } catch (Exception e) {
            // 容错处理：如果连不上币安，返回一个默认值或者 null
            System.err.println("Failed to fetch price: " + e.getMessage());
        }
        return "未知";
    }
}