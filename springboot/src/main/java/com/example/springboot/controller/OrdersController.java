package com.example.springboot.controller;

import com.alipay.api.AlipayApiException;
import com.example.springboot.common.Result;
import com.example.springboot.entity.Orders;
import com.example.springboot.exception.ServiceException;
import com.example.springboot.service.AlipayService;
import com.example.springboot.service.IOrdersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/orders")
public class OrdersController {

    @Autowired
    private IOrdersService ordersService;

    @Autowired
    private AlipayService alipayService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody Orders orders){
        try {
            ordersService.save(orders);
            return Result.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("500", e.getMessage());
        }
   }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result update(@RequestBody Orders orders){
        ordersService.update(orders);
        return Result.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    public Result delete(@RequestParam Integer id){
        ordersService.remove(id);
        return Result.success();
    }

    /**
     * 查询全部数据
     */
    @GetMapping("/selectAll")
    public Result selectAll(){
        return Result.success(ordersService.selectAll());
    }

    /**
     * 根据ID查询
     */
    @GetMapping("/selectById")
    public Result selectById(@RequestParam Integer id){
        return Result.success(ordersService.selectById(id));
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(@RequestParam(defaultValue = "") String name,
                             @RequestParam(defaultValue = "") String orderNo,
                             @RequestParam(defaultValue = "") String state,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize){
        return Result.success(ordersService.selectPage(pageNum,pageSize,name,orderNo,state));
    }
    /**
     * 支付接口
     */
    @PostMapping("/pay")
    public Result pay(@RequestBody Orders orders){
        try {
            // 获取创建前的订单 IDs（如果是批量支付）
            java.util.List<Integer> orderIds = orders.getOrderIds();
            
            // 执行支付操作（会创建订单）
            ordersService.pay(orders);
            
            // 查询刚刚创建的订单并返回
            if (orderIds != null && !orderIds.isEmpty()) {
                // 批量支付：查询这些订单
                java.util.List<Orders> createdOrders = new java.util.ArrayList<>();
                for (Integer orderId : orderIds) {
                    Orders order = ordersService.selectById(orderId);
                    if (order != null) {
                        createdOrders.add(order);
                    }
                }
                return Result.success(createdOrders);
            } else {
                // 单订单支付：查询这个订单
                Orders order = ordersService.selectById(orders.getId());
                return Result.success(order);
            }
        } catch (ServiceException e) {
            return Result.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            return Result.error("支付失败：" + e.getMessage());
        }
    }

    /**
     * 支付宝网页支付
     */
    @PostMapping("/arrival")
    public Result arrival(@RequestBody Orders orders) {
        try {
            if (orders == null || orders.getId() == null) {
                return Result.error("400", "订单ID不能为空");
            }
            ordersService.confirmArrival(orders.getId());
            return Result.success();
        } catch (ServiceException e) {
            return Result.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            return Result.error("500", "确认到货失败：" + e.getMessage());
        }
    }

    @PostMapping("/alipay/web")
    public Result alipayWebPay(@RequestBody Map<String, Object> params) {
        log.info("=== 支付宝网页支付请求开始 ===");
        
        // 从 Map 中获取订单 ID
        Integer id = null;
        if (params != null && params.containsKey("id")) {
            Object idObj = params.get("id");
            if (idObj instanceof Integer) {
                id = (Integer) idObj;
            } else if (idObj instanceof String) {
                id = Integer.parseInt(idObj.toString());
            } else if (idObj instanceof Number) {
                id = ((Number) idObj).intValue();
            }
        }
        
        log.info("接收到的订单 ID: {}", id);
        
        try {
            // 查询订单信息
            Orders order = ordersService.selectById(id);
            
            log.info("订单查询结果：{}", order != null ? "找到订单" : "订单不存在");
            if (order != null) {
                log.info("订单编号：{}", order.getOrderNo());
                log.info("订单名称：{}", order.getName());
                log.info("订单金额：{}", order.getPrice());
                log.info("订单用户 ID: {}", order.getUserId());
            }
            
            if (order == null) {
                log.error("订单不存在：{}", id);
                return Result.error("404", "订单不存在");
            }

            // 生成支付宝支付页面 HTML
            String htmlForm = alipayService.createWebPayHtml(
                order.getOrderNo() != null ? order.getOrderNo() : String.valueOf(order.getId()),
                order.getName(),
                order.getPrice()
            );

            log.info("支付宝支付页面生成成功，订单号：{}", order.getOrderNo());
            log.info("生成的 HTML 长度：{}", htmlForm != null ? htmlForm.length() : 0);
            
            return Result.success(htmlForm);
        } catch (AlipayApiException e) {
            log.error("支付宝支付失败", e);
            return Result.error("500", "支付宝支付失败：" + e.getMessage());
        } catch (Exception e) {
            log.error("支付宝支付异常", e);
            return Result.error("500", "系统异常：" + e.getMessage());
        }
    }

    /**
     * 支付宝同步回调
     */
    @GetMapping("/alipay/return")
    public void alipayReturn(HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.info("=== 支付宝同步回调 ===");
        Map<String, String[]> parameterMap = request.getParameterMap();
        
        // 转换参数格式
        Map<String, String> params = new java.util.HashMap<>();
        parameterMap.forEach((key, values) -> {
            if (values != null && values.length > 0) {
                params.put(key, values[0]);
            }
        });
        
        log.info("同步回调参数：{}", params);
        
        // 验证签名
        boolean verifyResult = alipayService.verifyNotify(params);
        
        // 获取订单号
        String outTradeNo = params.get("out_trade_no");
        String tradeStatus = params.get("trade_status");
        
        if (verifyResult) {
            log.info("支付宝同步回调验证成功");
            
            // 判断交易是否成功
            if ("TRADE_SUCCESS".equals(tradeStatus) || "TRADE_FINISHED".equals(tradeStatus)) {
                // 更新订单状态为"已付款"
                if (outTradeNo != null && !outTradeNo.isEmpty()) {
                    boolean updated = ordersService.updateOrderStatusByOrderNo(outTradeNo, "已付款");
                    if (updated) {
                        log.info("订单状态已更新为已付款 | 订单号：{}", outTradeNo);
                    } else {
                        log.error("订单状态更新失败 | 订单号：{}", outTradeNo);
                    }
                }
            }
            
            // 重定向到前端订单页面（使用 URL 编码传递成功消息）
            response.sendRedirect("http://localhost:8085/front/orders?payment=success");
        } else {
            log.error("支付宝同步回调验证失败");
            // 验证失败也跳转到订单页面，但不带成功参数
            response.sendRedirect("http://localhost:8085/front/orders?payment=failed");
        }
    }

    /**
     * 支付宝异步通知（重要）
     */
    @PostMapping("/alipay/notify")
    public String alipayNotify(HttpServletRequest request) {
        log.info("=== 支付宝异步通知 ===");
        
        Map<String, String[]> parameterMap = request.getParameterMap();
        
        // 转换参数格式
        Map<String, String> params = new java.util.HashMap<>();
        parameterMap.forEach((key, values) -> {
            if (values != null && values.length > 0) {
                params.put(key, values[0]);
            }
        });
        
        log.info("异步通知参数：{}", params);
        
        // 验证签名
        boolean verifyResult = alipayService.verifyNotify(params);
        
        if (verifyResult) {
            log.info("支付宝异步通知验证成功");
            
            // 获取交易状态
            String tradeStatus = params.get("trade_status");
            String outTradeNo = params.get("out_trade_no"); // 商户订单号
            String tradeNo = params.get("trade_no"); // 支付宝交易号
            String totalAmount = params.get("total_amount"); // 交易金额
            
            log.info("交易状态：{}, 商户订单号：{}, 支付宝交易号：{}, 金额：{}", 
                    tradeStatus, outTradeNo, tradeNo, totalAmount);
            
            // 判断交易是否成功
            if ("TRADE_SUCCESS".equals(tradeStatus) || "TRADE_FINISHED".equals(tradeStatus)) {
                try {
                    // 根据订单号更新订单状态为"已付款"
                    if (outTradeNo != null && !outTradeNo.isEmpty()) {
                        boolean updated = ordersService.updateOrderStatusByOrderNo(outTradeNo, "已付款");
                        if (updated) {
                            log.info("订单状态已更新为已付款 | 订单号：{}", outTradeNo);
                        } else {
                            log.error("订单状态更新失败 | 订单号：{}", outTradeNo);
                            return "failure";
                        }
                    }
                } catch (Exception e) {
                    log.error("更新订单状态失败", e);
                    return "failure";
                }
            }
            
            // 返回 success 给支付宝，表示已收到通知
            return "success";
        } else {
            log.error("支付宝异步通知验证失败");
            return "failure";
        }
    }
}