package com.example.springboot.service.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.example.springboot.config.AlipayConfig;
import com.example.springboot.service.AlipayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 支付宝支付服务实现类
 */
@Slf4j
@Service
public class AlipayServiceImpl implements AlipayService {

    @Autowired
    private AlipayConfig alipayConfig;

    @Autowired
    private AlipayClient alipayClient;

    /**
     * 创建支付宝网页支付链接
     */
    @Override
    public String createWebPayHtml(String orderNo, String subject, Double totalAmount) throws AlipayApiException {
        // 创建支付请求对象
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        
        // 设置同步回调地址
        request.setReturnUrl(alipayConfig.getReturnUrl());
        // 设置异步通知地址
        request.setNotifyUrl(alipayConfig.getNotifyUrl());
        
        // 构建订单参数
        String productCode = "FAST_INSTANT_TRADE_PAY"; // 销售产品码，默认 FAST_INSTANT_TRADE_PAY
        String body = "商品购买"; // 订单描述
        
        // JSON 格式的业务参数
        String orderJson = String.format(
            "{" +
            "\"out_trade_no\":\"%s\"," +
            "\"product_code\":\"%s\"," +
            "\"total_amount\":\"%.2f\"," +
            "\"subject\":\"%s\"," +
            "\"body\":\"%s\"" +
            "}",
            orderNo,
            productCode,
            totalAmount,
            subject,
            body
        );
        
        request.setBizContent(orderJson);
        
        log.info("支付宝支付请求 - 订单号：{}, 金额：{}, 标题：{}", orderNo, totalAmount, subject);
        
        // 调用 SDK 生成支付页面 HTML
        String htmlForm = alipayClient.pageExecute(request).getBody();
        
        log.info("支付宝支付页面生成成功");
        return htmlForm;
    }

    /**
     * 验证支付宝回调签名
     */
    @Override
    public boolean verifyNotify(Map<String, String> params) {
        try {
            log.info("支付宝回调参数验证开始");
            log.info("回调参数：{}", params);
            
            boolean result = AlipaySignature.rsaCheckV1(
                params,
                alipayConfig.getAlipayPublicKey(),
                alipayConfig.getCharset(),
                alipayConfig.getSignType()
            );
            
            log.info("支付宝回调验证结果：{}", result ? "成功" : "失败");
            return result;
        } catch (AlipayApiException e) {
            log.error("支付宝回调验证失败", e);
            return false;
        }
    }
}
