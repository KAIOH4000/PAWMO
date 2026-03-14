# 支付宝沙箱支付 - 测试指南

## ✅ 配置已完成

您的支付宝沙箱配置已成功填入系统！

### 配置信息

```yaml
app-id: "9021000162612447"
private-key: MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCoBF/A0co8HaXBE8+pHU6PPTI15kekOmX422dCWmVNM29Qm0c+l3fu4bN9u9QOpH8/WeP2YXrpYez7yE5vse060mL65uEbS0aohMH9L92RR15weTssaN+VjkBiRd1STqhcOnWz9jRF0ep5PJmbKJM19qtV9f8hSnkEpf53v9l3KgENlAX6K6/CVLZO3SMXCn686UAsHDLzg/cKL0qSR632i9vv5S2DbKSdj62pAkROvARBkdWB6vlpBYS+0GLMkAmrT7Ogrd/xEkotU424E2y62iGyl3K8tBK8A5XjVPVfunBPAhmlP6y1CQ+Xakwh8MckIBK8wbrQBXWFWWSmCJKnAgMBAAECggEAN8Kr3JxDV8wnFHr0shVt2w/eOBEiuZg/+2UlUdoaQJLPYJsH+/fc78SsAxBxAiMJsAavJzeWyRmcwrrpfRw5tQnZm5dAIgTLgGX+034tNGfRKdayEr0DZuj86SByoTxFl8Y3BfCxzHIsNOwttlay9HfWyQKJF41VuHs2hpt/GpKmd+LOVQ1OP1LrBVnPBkjKeLrf/519O7HdBQCcA1eISfOBYNDv6kfU7Gqxp85HhE5PclAVQK7wid3yjBAr814DKWVRAQltBAIWpwHlJvTpabzDXDbXwPNCxNiiXBXOkW6tAEbDNxIJZVcb3DKC564s6JF+JYTBJBYI0TN3CaXFQQKBgQDRrVNcIL1iiZoqE+3hF6sb0eQj8CPMci4oq7YjPKr2rNUboAvshclILnMHg9BbPqKSdDeVg7UrAZtlkKoykZiZwKzZmoS8cLiXA0Wn7lVKiYcZRwLPNi4bWJXTBvi5RCfRJau7AmyqhzUdoA8QYPtuHa1R5ybhNGTlmc7YgM2W9wKBgQDNIuRJ/sUaS/Kut2xP2Fy9F+FDZoipflzC9n2cSLUOZdbP/qUBsBNJ0uQV1YfjTe+ZQ0jJa9UgM8IrPm4gpmTeCF+TqLRSDGmcI0DUi5eeLhEOXYJbv6jCuCfkMVJgtQ/LT9yjZOD2I8rh79uYvMUueoL9oDmo6oDxULvbM6F0QKBgH9OXFGQc0xTva2UohbL1xEyE/GaCTJFlS8OaqzkVe0lrNJ0DylRZY+U3DSQ3L25vt1rAKCf8iylciNtfVjsu5mGuav5PGRpkDnoO6+lblWuQL+hNNx4ye/Cjl0tW3CpJ/+9fpIx+6ZiJ2bQWrUYHTCThjs3PIz50dTx2Pvs4GarAoGAfmb0or/RNl38B/lYZ8K1Sn9YaIU0KAt7xS9VVGzsWDdQk5K91f6++guvnTajw/dhlPlxeenbusdQS3lCuzYGaf9JLCpKW4kPxw5tZdF67w2WBbwehruVPmQSe2BaoOxz2qYIBY9FiaWkut7/z4Y7YNcaZ9qyHOxWc9JWZ2/1ZjECgYEAqcazFQPgS9Rr9hpsxcw1evYbqUPGSx+ETlT8WldsuT+T9yVSQ4sJrGB+2TUCQskVhL5loDhTzt8Jhho/Wqzo2Zqfhy9kbv8ux4mLJ5JNcsA74hwgk8yjsEuf8r5aysOLn82WLnwncDi0Cc/QW2neJkkYp3KguiVHzc7mdrP9o4Q=
alipay-public-key: MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAsj6z4xx87ppu2qavFvkTnJcyWsm4lScdeZZs2MRZI/nRsOOAWZzGeao2PLcBeI0tublN0EAxYkhO3St1gW2aRuFOjqn08JpBhp/kBhI/gi4glO5xJ2CZV2kaKo8AaNl7zgHxhJ/Wuo9zO7ddAB4OVVa2Gs7UZg966/KZhsMOlOW/UKtOajVgKjGNyxrMkdLLBupxLcWnSagMfKexGOYHxc6a8UT8sYI8tBmUJxyludlQzD05WYw7+zHuCZv4fyJh9gEe7D+pH1/Y+vfZiUfOWmjpU/WTDHMkutGW8swY/PtTO/LoqMBpDAB5fzpKoaqKiOn5vymxaCqiqTMXyBWJNwIDAQAB
```

---

## 🚀 立即测试

### 步骤 1: 启动后端服务

```bash
cd D:\springboot\d1\code-scaffold\springboot
mvn clean install
mvn spring-boot:run
```

**确认启动成功**:
- 看到 `Started SpringbootApplication` 字样
- 服务运行在 `http://localhost:9999`

### 步骤 2: 启动前端服务

```bash
cd D:\springboot\d1\code-scaffold\vue
npm run serve
```

**确认启动成功**:
- 看到 `App running at: http://localhost:8085/`
- 前端运行在 `http://localhost:8085`

### 步骤 3: 创建测试订单

1. **登录系统**
   - 访问 http://localhost:8085
   - 使用您的账号登录

2. **浏览商品**
   - 进入商品列表页
   - 选择任意商品查看详情

3. **下单购买**
   - 点击"立即购买"或加入购物车后结算
   - 填写收货地址信息
   - 点击"确认支付"

### 步骤 4: 选择支付宝支付

在支付对话框中:
1. 选择 **"支付宝支付"** 选项
2. 点击 **"确认支付"** 按钮

### 步骤 5: 跳转支付宝页面

系统会自动打开新窗口，显示支付宝支付页面

### 步骤 6: 登录支付宝沙箱

**重要**: 您需要先查看沙箱账号信息

#### 获取沙箱账号

1. 访问：https://open.alipay.com/develop/sandbox/app
2. 登录后进入您的应用
3. 找到"沙箱账号"部分
4. 查看买家账号信息（用于支付）

典型格式:
```
买家账号：xxxxx@sandbox.com
登录密码：111111
支付密码：111111
```

#### 登录并支付

1. **登录支付宝**
   - 输入买家账号：`xxxxx@sandbox.com`
   - 输入登录密码：`111111`

2. **确认支付**
   - 核对订单信息
   - 输入支付密码：`111111`
   - 点击确认支付

3. **等待跳转**
   - 支付成功后会跳转回商户页面
   - 显示"支付成功"提示

---

## 🔍 验证支付结果

### 1. 前端验证

访问：http://localhost:8085/front/orders

查看订单状态应该变为：**"已付款"**

### 2. 后端日志验证

在后端控制台查看日志，应该看到:

```
=== 支付宝同步回调 ===
同步回调参数：{out_trade_no=..., trade_status=TRADE_SUCCESS, ...}
支付宝同步回调验证成功

=== 支付宝异步通知 ===
异步通知参数：{out_trade_no=..., trade_status=TRADE_SUCCESS, ...}
支付宝异步通知验证成功
交易状态：TRADE_SUCCESS, 商户订单号：..., 支付宝交易号：..., 金额：...
订单支付成功，订单号：...
```

---

## ⚠️ 重要提示

### 1. 内网穿透问题

由于您是本地开发，支付宝的**异步通知**可能无法访问到您的本地服务器。

**解决方案**:

#### 方案 A: 使用内网穿透工具（推荐）

**使用 natapp**:

1. 下载 natapp: https://natapp.cn/
2. 注册并获取免费隧道 token
3. 配置文件 `config.ini`:
   ```ini
   [tunnel]
   token = 你的 token
   ```
4. 启动 natapp:
   ```bash
   natapp.exe
   ```
5. 获得外网地址，如：`https://abc123.natappfree.cc`
6. 修改 `application.yml`:
   ```yaml
   notify-url: "https://abc123.natappfree.cc/orders/alipay/notify"
   ```

**使用 ngrok**:

```bash
ngrok http 9999
```

会得到类似地址：`https://1a2b3c4d.ngrok.io`

#### 方案 B: 暂时忽略异步通知

在开发阶段，可以暂时只依赖同步回调。但要注意：
- 同步回调主要用于页面跳转展示
- 真正的订单状态更新应该在异步通知中处理
- 生产环境必须使用异步通知

### 2. 密钥格式

您提供的私钥已经是 PKCS8 格式，可以直接使用 ✅

### 3. 测试账号

请确保使用**支付宝沙箱后台**查看到的实际账号，而不是示例账号。

---

## 🎯 测试清单

请按顺序检查以下项目:

- [ ] 后端服务启动成功 (端口 9999)
- [ ] 前端服务启动成功 (端口 8085)
- [ ] application.yml 配置正确
- [ ] 能够正常创建订单
- [ ] 支付对话框显示支付宝选项
- [ ] 点击支付宝支付后打开新窗口
- [ ] 能够访问支付宝沙箱页面
- [ ] 使用沙箱账号成功登录
- [ ] 完成支付流程
- [ ] 支付成功后订单状态更新
- [ ] 后端日志显示回调信息

---

## 📊 常见问题

### Q1: 打不开支付宝页面

**原因**: 
- 网络问题
- 配置错误
- 浏览器拦截

**解决方法**:
1. 检查网络连接
2. 查看后端接口返回（F12 开发者工具）
3. 允许浏览器弹窗

### Q2: 签名验证失败

**错误**: "Invalid Sign" 或 "签名验证失败"

**原因**:
- 密钥配置错误
- 公钥和私钥不匹配

**解决方法**:
1. 检查 application.yml 中的密钥是否正确复制
2. 确保没有多余的空格或换行
3. 重新从支付宝沙箱后台复制密钥

### Q3: 支付成功但订单状态未更新

**原因**:
- 异步通知未收到（本地开发常见问题）
- 订单号传递错误

**解决方法**:
1. 检查后端日志是否收到异步通知
2. 如果是内网问题，使用内网穿透工具
3. 或者暂时通过同步回调更新订单状态（不推荐生产环境）

### Q4: 余额不足提示

**注意**: 这是余额支付的检查逻辑

如果选择支付宝支付，不会检查余额。请确保:
- 选择了正确的支付方式（支付宝）
- 前端代码正确处理了支付宝支付逻辑

---

## 📝 下一步建议

### 完成基本测试后:

1. **完善异步通知处理**
   - 在 `OrdersController.alipayNotify()` 方法中添加订单更新逻辑
   - 根据订单号更新订单状态为"已付款"

2. **添加支付记录**
   - 创建支付流水表
   - 记录每次支付的详细信息

3. **异常处理**
   - 支付超时处理
   - 重复支付检查
   - 退款功能实现

4. **安全性提升**
   - 添加防重放攻击机制
   - 支付金额双重校验
   - IP 白名单限制

---

## 🎉 恭喜！

配置已完成，现在您可以开始测试支付宝沙箱支付了！

**关键文件位置**:
- 后端配置：`springboot/src/main/resources/application.yml`
- 前端页面：`vue/src/views/front/Orders.vue`
- 支付服务：`springboot/src/main/java/com/example/springboot/service/impl/AlipayServiceImpl.java`

**如有问题，请查看**:
- 详细文档：`ALIPAY_SANDBOX_SETUP.md`
- 快速指南：`ALIPAY_QUICK_START.md`

---

*测试指南 | 更新时间：2026-03-18*  
*配置状态：✅ 已完成*
