package com.example.springboot.service.impl;

import com.example.springboot.entity.Goods;
import com.example.springboot.entity.Orders;
import com.example.springboot.entity.User;
import com.example.springboot.mapper.GoodsMapper;
import com.example.springboot.mapper.OrdersMapper;
import com.example.springboot.mapper.UserMapper;
import com.example.springboot.service.AiService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AiServiceImpl implements AiService {

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private OrdersMapper ordersMapper;

    @Autowired
    private UserMapper userMapper;

    // 预设问答库
    private final Map<String, String> qaMap = new HashMap<>();

    public AiServiceImpl() {
        // 初始化预设问答
        initQaMap();
    }

    private void initQaMap() {
        // 商品相关
        qaMap.put("狗粮推荐", "根据您的狗狗年龄和品种，我们推荐以下几款狗粮：1. 幼犬粮：富含蛋白质和钙，促进生长发育；2. 成犬粮：均衡营养，维持健康体重；3. 老年犬粮：易消化，保护关节。您可以在商品分类中查看详细信息。");
        qaMap.put("猫砂哪种好", "我们推荐以下几种猫砂：1. 膨润土猫砂：结团好，吸味强；2. 豆腐猫砂：可冲厕所，环保；3. 水晶猫砂：吸水快，使用时间长。您可以根据猫咪的喜好和您的需求选择。");
        qaMap.put("宠物玩具", "我们有多种宠物玩具可供选择，包括：1. 猫咪玩具：逗猫棒、激光笔、猫抓板；2. 狗狗玩具：咬胶、飞盘、发声玩具；3. 小宠玩具：跑轮、隧道、磨牙玩具。");

        // 订单相关
        qaMap.put("如何下单", "您可以按照以下步骤下单：1. 浏览商品，将喜欢的商品加入购物车；2. 进入购物车，确认商品数量和价格；3. 点击结算，填写收货地址和联系方式；4. 选择支付方式，完成支付。");
        qaMap.put("订单状态", "订单状态包括：待支付、待发货、待收货、已完成、已取消。您可以在个人中心的订单管理中查看订单状态。");
        qaMap.put("发货时间", "一般情况下，我们会在您下单后48小时内发货。特殊情况（如节假日、促销活动）可能会有所延迟，我们会提前通知您。");

        // 售后相关
        qaMap.put("如何申请售后", "您可以按照以下步骤申请售后：1. 进入个人中心，点击订单管理；2. 找到需要售后的订单，点击申请售后；3. 填写售后原因和上传相关凭证；4. 提交申请，等待审核。");
        qaMap.put("退款时间", "售后审核通过后，我们会在1-3个工作日内为您处理退款。退款到账时间取决于您的支付方式，一般为1-7个工作日。");
        qaMap.put("退换货政策", "我们支持7天无理由退换货（商品需保持完好，不影响二次销售）。质量问题我们承担来回运费，非质量问题运费由您承担。");

        // 其他常见问题
        qaMap.put("营业时间", "我们的客服工作时间为周一至周日 9:00-21:00，您可以在此期间联系我们。");
        qaMap.put("联系方式", "您可以通过以下方式联系我们：1. 在线客服：点击页面右侧的AI客服；2. 电话：400-123-4567；3. 邮箱：service@petshop.com。");
        qaMap.put("会员权益", "成为我们的会员可以享受以下权益：1. 积分累计，可兑换商品；2. 会员专享折扣；3. 生日特权；4. 优先购买限量商品。");
    }

    @Override
    public String getAnswer(String question) {
        // 分析问题类型
        if (question.contains("商品") || question.contains("产品") || question.contains("物品")) {
            return queryDatabase("goods", question);
        } else if (question.contains("订单") || question.contains("购买") || question.contains("支付")) {
            return queryDatabase("orders", question);
        } else if (question.contains("用户") || question.contains("账户") || question.contains("个人")) {
            return queryDatabase("user", question);
        }

        // 简单的关键词匹配
        for (Map.Entry<String, String> entry : qaMap.entrySet()) {
            if (question.contains(entry.getKey())) {
                return entry.getValue();
            }
        }

        // 通用回答
        return "感谢您的咨询。关于\"" + question + "\"，我们的客服人员会尽快为您解答。您也可以尝试询问以下问题：\n1. 狗粮推荐\n2. 如何下单\n3. 如何申请售后\n4. 营业时间";
    }

    @Override
    public String queryDatabase(String questionType, String keyword) {
        switch (questionType) {
            case "goods":
                return queryGoods(keyword);
            case "orders":
                return queryOrders(keyword);
            case "user":
                return queryUser(keyword);
            default:
                return "抱歉，我暂时无法回答这个问题。";
        }
    }

    private String queryGoods(String keyword) {
        // 查询商品
        List<Goods> goodsList = goodsMapper.selectList(
                new LambdaQueryWrapper<Goods>()
                        .like(Goods::getName, keyword)
                        .or()
                        .like(Goods::getDescr, keyword)
                        .last("LIMIT 5")
        );

        if (goodsList.isEmpty()) {
            return "没有找到相关商品，您可以尝试其他关键词。";
        }

        StringBuilder sb = new StringBuilder("找到以下相关商品：\n");
        for (Goods goods : goodsList) {
            sb.append(goods.getName())
                    .append(" - ¥")
                    .append(goods.getPrice())
                    .append(" - ")
                    .append(goods.getDescr())
                    .append("\n");
        }
        return sb.toString();
    }

    private String queryOrders(String keyword) {
        // 查询订单（这里简化处理，实际应该根据用户ID查询）
        List<Orders> ordersList = ordersMapper.selectList(
                new LambdaQueryWrapper<Orders>()
                        .like(Orders::getOrderNo, keyword)
                        .last("LIMIT 5")
        );

        if (ordersList.isEmpty()) {
            return "没有找到相关订单，您可以尝试其他关键词。";
        }

        StringBuilder sb = new StringBuilder("找到以下相关订单：\n");
        for (Orders orders : ordersList) {
            sb.append("订单号：")
                    .append(orders.getOrderNo())
                    .append(" - 金额：¥")
                    .append(orders.getPrice())
                    .append(" - 状态：")
                    .append(orders.getState())
                    .append("\n");
        }
        return sb.toString();
    }

    private String queryUser(String keyword) {
        // 查询用户（这里简化处理，实际应该根据当前登录用户查询）
        List<User> userList = userMapper.selectList(
                new LambdaQueryWrapper<User>()
                        .like(User::getUsername, keyword)
                        .or()
                        .like(User::getPhone, keyword)
                        .last("LIMIT 5")
        );

        if (userList.isEmpty()) {
            return "没有找到相关用户信息。";
        }

        StringBuilder sb = new StringBuilder("找到以下用户信息：\n");
        for (User user : userList) {
            sb.append("用户名：")
                    .append(user.getUsername())
                    .append(" - 手机号：")
                    .append(user.getPhone())
                    .append(" - 角色：")
                    .append(user.getRole())
                    .append("\n");
        }
        return sb.toString();
    }
}