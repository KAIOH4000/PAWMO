package com.example.springboot.service.impl;

import com.example.springboot.entity.Goods;
import com.example.springboot.entity.Orders;
import com.example.springboot.entity.User;
import com.example.springboot.mapper.GoodsMapper;
import com.example.springboot.mapper.OrdersMapper;
import com.example.springboot.mapper.UserMapper;
import com.example.springboot.service.AiService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import okhttp3.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class AiServiceImpl implements AiService {

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private OrdersMapper ordersMapper;

    @Autowired
    private UserMapper userMapper;

    // 日志记录
    private static final Logger logger = LoggerFactory.getLogger(AiServiceImpl.class);

    // 预设问答库
    private final Map<String, String> qaMap = new HashMap<>();
    
    // DeepSeek API 配置
    private final String DEEPSEEK_API_KEY = "sk-22c993f9e898462ea23203878c8de4fe";
    private final String DEEPSEEK_API_URL = "https://api.deepseek.com/chat/completions";
    // OkHttp 客户端 - 增加超时时间到 30 秒
    private final OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build();

    public AiServiceImpl() {
        // 初始化预设问答
        initQaMap();
    }

    private void initQaMap() {
        // 商品相关
        qaMap.put("狗粮推荐", "根据您的狗狗年龄和品种，我们推荐以下几款狗粮：1. 幼犬粮：富含蛋白质和钙，促进生长发育；2. 成犬粮：均衡营养，维持健康体重；3. 老年犬粮：易消化，保护关节。您可以在商品分类中查看详细信息。");
        qaMap.put("猫砂哪种好", "我们推荐以下几种猫砂：1. 膨润土猫砂：结团好，吸味强；2. 豆腐猫砂：可冲厕所，环保；3. 水晶猫砂：吸水快，使用时间长。您可以根据猫咪的喜好和您的需求选择。");
        qaMap.put("宠物玩具", "我们有多种宠物玩具可供选择，包括：1. 猫咪玩具：逗猫棒、激光笔、猫抓板；2. 狗狗玩具：咬胶、飞盘、发声玩具；3. 小宠玩具：跑轮、隧道、磨牙棒。");
        qaMap.put("猫粮", "我们的猫粮系列包括：1. 幼猫粮：适合 2-12 个月幼猫；2. 成猫粮：适合 1-7 岁成猫；3. 老年猫粮：适合 7 岁以上老年猫。所有猫粮都富含牛磺酸，保护猫咪视力健康。");
        qaMap.put("狗零食", "我们有各种狗零食：1. 肉干类：鸡肉干、牛肉干、鸭肉干；2. 咬胶类：洁齿骨、磨牙棒；3. 训练零食：小颗粒奖励零食。适量喂食，注意不要影响正餐。");

        // 订单相关
        qaMap.put("如何下单", "您可以按照以下步骤下单：1. 浏览商品，将喜欢的商品加入购物车；2. 进入购物车，确认商品数量和价格；3. 点击结算，填写收货地址和联系方式；4. 选择支付方式，完成支付。");
        qaMap.put("订单状态", "订单状态包括：待支付、待发货、待收货、已完成、已取消。您可以在个人中心的订单管理中查看订单状态。");
        qaMap.put("发货时间", "一般情况下，我们会在您下单后 48 小时内发货。特殊情况（如节假日、促销活动）可能会有所延迟，我们会提前通知您。");
        qaMap.put("配送范围", "我们支持全国配送，偏远地区可能需要额外运费。具体运费会在结算页面显示。");

        // 售后相关
        qaMap.put("如何申请售后", "您可以按照以下步骤申请售后：1. 进入个人中心，点击订单管理；2. 找到需要售后的订单，点击申请售后；3. 填写售后原因和上传相关凭证；4. 提交申请，等待审核。");
        qaMap.put("退款时间", "售后审核通过后，我们会在 1-3 个工作日内为您处理退款。退款到账时间取决于您的支付方式，一般为 1-7 个工作日。");
        qaMap.put("退换货政策", "我们支持 7 天无理由退换货（商品需保持完好，不影响二次销售）。质量问题我们承担来回运费，非质量问题运费由您承担。");
        qaMap.put("商品破损", "如果您收到的商品有破损，请在签收后 24 小时内联系我们客服，并提供照片凭证，我们会为您免费更换或退款。");

        // 其他常见问题
        qaMap.put("营业时间", "我们的客服工作时间为周一至周日 9:00-21:00，您可以在此期间联系我们。");
        qaMap.put("联系方式", "您可以通过以下方式联系我们：1. 在线客服：点击页面右侧的 AI 客服；2. 电话：400-123-4567；3. 邮箱：service@petshop.com。");
        qaMap.put("会员权益", "成为我们的会员可以享受以下权益：1. 积分累计，可兑换商品；2. 会员专享折扣；3. 生日特权；4. 优先购买限量商品。");
        qaMap.put("如何注册会员", "您可以在个人中心点击注册，填写基本信息即可成为会员。消费可以累积积分，积分可以兑换商品或抵扣现金。");
    }

    @Override
    public String getAnswer(String question) {
        logger.info(">>>>>>>>>> 1. 进入 AiServiceImpl.getAnswer 方法，接收到问题：{} <<<<<<<<<<", question);

        // 1. 检测是否是商品推荐类问题
        if (question.contains("推荐") || question.contains("哪种好") || 
            question.contains("怎么样") || question.contains("好吗") ||
            question.contains("猫粮") || question.contains("狗粮") ||
            question.contains("猫砂") || question.contains("玩具") ||
            question.contains("零食") || question.contains("笼子") ||
            question.contains("窝") || question.contains("衣服")) {
            
            logger.info(">>>>>>>>>> 2. 检测到商品咨询问题，查询数据库... <<<<<<<<<<");
            
            // 提取关键词查询数据库
            String[] keywords = {"猫粮", "狗粮", "猫砂", "玩具", "零食", "笼子", "窝", "衣服"};
            String searchKeyword = null;
            
            for (String kw : keywords) {
                if (question.contains(kw)) {
                    searchKeyword = kw;
                    break;
                }
            }
            
            if (searchKeyword != null) {
                List<Goods> goodsList = goodsMapper.selectList(
                        new LambdaQueryWrapper<Goods>()
                                .like(Goods::getName, searchKeyword)
                                .or()
                                .like(Goods::getDescr, searchKeyword)
                                .last("LIMIT 5")
                );
                
                if (!goodsList.isEmpty()) {
                    logger.info(">>>>>>>>>> 3. 数据库找到 {} 个商品，调用 AI 生成推荐... <<<<<<<<<<", goodsList.size());
                    try {
                        return callDeepSeekWithContext(question, goodsList);
                    } catch (Exception e) {
                        logger.error("AI 调用失败，返回数据库结果", e);
                        return buildGoodsRecommendation(goodsList);
                    }
                }
            }
            
            // 如果没找到具体关键词或数据库为空，直接调用 AI
            logger.info(">>>>>>>>>> 4. 数据库无匹配商品，调用 AI 通用推荐... <<<<<<<<<<");
            try {
                return callDeepSeek(question);
            } catch (Exception e) {
                logger.error("AI 调用失败", e);
                return "抱歉，我暂时无法回答您的问题，请稍后再试。";
            }
        }
        
        // 2. 检查是否是订单相关问题
        if (question.contains("订单") || question.contains("购买") || question.contains("支付") ||
            question.contains("下单") || question.contains("怎么买")) {
            logger.info(">>>>>>>>>> 匹配到订单相关问题 <<<<<<<<<<");
            return queryDatabase("orders", question);
        }
        
        // 3. 检查是否是用户相关问题
        if (question.contains("用户") || question.contains("账户") || question.contains("个人") ||
            question.contains("密码") || question.contains("修改")) {
            logger.info(">>>>>>>>>> 匹配到用户相关问题 <<<<<<<<<<");
            return queryDatabase("user", question);
        }

        // 4. 检查预设问答库
        logger.info(">>>>>>>>>> 5. 准备匹配预设问答库... <<<<<<<<<<");
        for (Map.Entry<String, String> entry : qaMap.entrySet()) {
            if (question.contains(entry.getKey())) {
                logger.info(">>>>>>>>>> 6. 在预设问答库中找到匹配项：key='{}' <<<<<<<<<<", entry.getKey());
                String answer = entry.getValue();
                logger.info(">>>>>>>>>> 7. 准备返回答案：{} <<<<<<<<<<", answer);
                return answer;
            }
        }

        // 5. 调用 DeepSeek AI
        logger.info(">>>>>>>>>> 8. 未匹配到预设问答，准备调用 DeepSeek AI... <<<<<<<<<<");
        try {
            // 最多重试 2 次
            for (int i = 0; i < 2; i++) {
                try {
                    String aiAnswer = callDeepSeek(question);
                    logger.info(">>>>>>>>>> 9. DeepSeek AI 调用成功，返回答案：{} <<<<<<<<<<", aiAnswer);
                    return aiAnswer;
                } catch (IOException e) {
                    if (i == 1) { // 最后一次重试失败
                        throw e;
                    }
                    logger.warn("第 {} 次调用 DeepSeek API 失败，准备重试：{}", i + 1, e.getMessage());
                    Thread.sleep(1000); // 等待 1 秒后重试
                }
            }
        } catch (IOException e) {
            logger.error("调用 DeepSeek API 时发生 IO 异常：{}", e.getMessage());
            if (e.getMessage() != null && e.getMessage().contains("Insufficient Balance")) {
                return "抱歉，AI 客服服务暂时不可用（余额不足）。您可以尝试询问商品、订单或售后相关问题。";
            } else if (e.getMessage() != null && e.getMessage().contains("timeout")) {
                return "抱歉，AI 客服响应超时，请稍后再试或简化您的问题。";
            }
            return "抱歉，我暂时无法回答您的问题，请稍后再试。";
        } catch (Exception e) {
            logger.error("调用 DeepSeek API 时发生异常", e);
            return "抱歉，我暂时无法回答您的问题，请稍后再试。";
        }
        
        return "抱歉，我暂时无法回答您的问题，请稍后再试。";
    }

    // 辅助方法：构建商品推荐文本
    private String buildGoodsRecommendation(List<Goods> goodsList) {
        StringBuilder sb = new StringBuilder("根据您的要求，为您推荐以下商品：\n");
        for (Goods goods : goodsList) {
            sb.append("• ").append(goods.getName())
              .append(" - ¥").append(goods.getPrice())
              .append("\n  ").append(goods.getDescr()).append("\n");
        }
        sb.append("\n您可以在商品分类中查看更多详情！");
        return sb.toString();
    }

    // 调用 DeepSeek API（带上下文信息）
    private String callDeepSeekWithContext(String question, List<Goods> goodsList) throws IOException {
        JSONObject requestBody = new JSONObject();
        JSONArray messages = new JSONArray();
        
        // 系统提示词
        JSONObject systemMessage = new JSONObject();
        systemMessage.put("role", "system");
        systemMessage.put("content", "你是一个宠物用品店的智能客服，请专业、友好地回答用户问题。");
        messages.add(systemMessage);
        
        // 构建用户消息
        StringBuilder userContent = new StringBuilder();
        
        // 如果有商品信息，添加到上下文中
        if (goodsList != null && !goodsList.isEmpty()) {
            userContent.append("以下是我们店铺的部分商品信息：\n");
            for (int i = 0; i < goodsList.size() && i < 5; i++) {
                Goods goods = goodsList.get(i);
                userContent.append(String.format("%d. %s - ¥%s - %s\n", 
                    i + 1, goods.getName(), goods.getPrice(), goods.getDescr()));
            }
            userContent.append("\n");
        }
        
        userContent.append("用户问题：").append(question);
        
        JSONObject userMessage = new JSONObject();
        userMessage.put("role", "user");
        userMessage.put("content", userContent.toString());
        messages.add(userMessage);
        
        requestBody.put("messages", messages);
        requestBody.put("model", "deepseek-chat");

        logger.info("发送给 DeepSeek 的请求体：{}", requestBody.toJSONString());

        RequestBody body = RequestBody.create(
                requestBody.toJSONString(),
                MediaType.get("application/json; charset=utf-8")
        );
        Request request = new Request.Builder()
                .url(DEEPSEEK_API_URL)
                .header("Authorization", "Bearer " + DEEPSEEK_API_KEY)
                .post(body)
                .build();

        Response response = null;
        try {
            response = okHttpClient.newCall(request).execute();
            String responseContent = response.body().string();
            logger.info("收到 DeepSeek 的响应：{}", responseContent);

            if (!response.isSuccessful()) {
                throw new IOException("调用 DeepSeek API 失败，状态码：" + response.code() + ", 响应：" + responseContent);
            }

            JSONObject responseBody = JSON.parseObject(responseContent);
            if (responseBody.containsKey("error")) {
                throw new IOException("DeepSeek API 返回错误：" + responseBody.getJSONObject("error").getString("message"));
            }

            String answer = responseBody.getJSONArray("choices").getJSONObject(0).getJSONObject("message").getString("content");
            logger.info("解析后的答案：{}", answer);
            return answer;
        } finally {
            if (response != null) {
                response.close();
            }
        }
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
        // 提取关键词进行查询
        String[] keywords = {"猫粮", "狗粮", "猫砂", "玩具", "零食", "笼子", "窝", "衣服"};
        String searchKeyword = null;
        
        for (String kw : keywords) {
            if (keyword.contains(kw)) {
                searchKeyword = kw;
                break;
            }
        }
        
        if (searchKeyword == null) {
            searchKeyword = keyword;
        }
        
        List<Goods> goodsList = goodsMapper.selectList(
                new LambdaQueryWrapper<Goods>()
                        .like(Goods::getName, searchKeyword)
                        .or()
                        .like(Goods::getDescr, searchKeyword)
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

    // 调用 DeepSeek API
    private String callDeepSeek(String question) throws IOException {
        JSONObject requestBody = new JSONObject();
        JSONArray messages = new JSONArray();
        JSONObject userMessage = new JSONObject();
        userMessage.put("role", "user");
        userMessage.put("content", "你是一个宠物用品店的智能客服，请根据用户问题回答：" + question);
        messages.add(userMessage);
        requestBody.put("messages", messages);
        requestBody.put("model", "deepseek-chat");

        logger.info("发送给 DeepSeek 的请求体：{}", requestBody.toJSONString());

        RequestBody body = RequestBody.create(
                requestBody.toJSONString(),
                MediaType.get("application/json; charset=utf-8")
        );
        Request request = new Request.Builder()
                .url(DEEPSEEK_API_URL)
                .header("Authorization", "Bearer " + DEEPSEEK_API_KEY)
                .post(body)
                .build();

        Response response = null;
        try {
            response = okHttpClient.newCall(request).execute();
            String responseContent = response.body().string();
            logger.info("收到 DeepSeek 的响应：{}", responseContent);

            if (!response.isSuccessful()) {
                throw new IOException("调用 DeepSeek API 失败，状态码：" + response.code() + ", 响应：" + responseContent);
            }

            JSONObject responseBody = JSON.parseObject(responseContent);
            if (responseBody.containsKey("error")) {
                throw new IOException("DeepSeek API 返回错误：" + responseBody.getJSONObject("error").getString("message"));
            }

            String answer = responseBody.getJSONArray("choices").getJSONObject(0).getJSONObject("message").getString("content");
            logger.info("解析后的答案：{}", answer);
            return answer;
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }


}