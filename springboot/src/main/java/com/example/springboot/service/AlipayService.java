package com.example.springboot.service;

import com.alipay.api.AlipayApiException;

/**
 * 支付宝支付服务接口
 */
public interface AlipayService {

    /**
     * 创建支付宝网页支付链接
     * @param orderNo 订单号
     * @param subject 订单标题
     * @param totalAmount 订单金额
     * @return 支付宝支付页面 HTML 表单
     * @throws AlipayApiException 支付宝 API 异常
     */
    String createWebPayHtml(String orderNo, String subject, Double totalAmount) throws AlipayApiException;

    /**
     * 验证支付宝回调签名
     * @param params 回调参数
     * @return 验证是否通过
     */
    boolean verifyNotify(java.util.Map<String, String> params);
}
