package com.example.springboot.config;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 支付宝配置类
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "alipay")
public class AlipayConfig {

    /**
     * 应用 ID
     */
    private String appId;

    /**
     * 应用私钥
     */
    private String privateKey;

    /**
     * 支付宝公钥
     */
    private String alipayPublicKey;

    /**
     * 网关地址
     */
    private String gatewayUrl = "https://openapi-sandbox.dl.alipaydev.com/gateway.do";

    /**
     * 签名方式
     */
    private String signType = "RSA2";

    /**
     * 字符编码
     */
    private String charset = "utf-8";

    /**
     * 返回格式
     */
    private String format = "json";

    /**
     * 回调地址
     */
    private String notifyUrl;

    /**
     * 跳转回调地址
     */
    private String returnUrl;

    /**
     * 创建支付宝客户端 Bean
     */
    @Bean
    public AlipayClient alipayClient() {
        return new DefaultAlipayClient(
                gatewayUrl,
                appId,
                privateKey,
                format,
                charset,
                alipayPublicKey,
                signType
        );
    }
}
