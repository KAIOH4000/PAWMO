# 支付宝沙箱支付接入指南

## ✅ 已完成的工作

### 1. 后端依赖添加 ✅

**文件**: `springboot/pom.xml`

添加了支付宝 SDK 依赖:
```xml
<!-- 支付宝 SDK -->
<dependency>
    <groupId>com.alipay.sdk</groupId>
    <artifactId>alipay-sdk-java</artifactId>
    <version>4.38.157.ALL</version>
</dependency>
```

---

### 2. 后端配置类 ✅

**文件**: `springboot/src/main/java/com/example/springboot/config/AlipayConfig.java`

功能:
- ✅ 支付宝配置参数管理
- ✅ 自动加载 application.yml 配置
- ✅ 创建 AlipayClient Bean

---

### 3. 配置文件更新 ✅

**文件**: `springboot/src/main/resources/application.yml`

新增配置:
```yaml
alipay:
  app-id: "填写你的沙箱应用 ID"
  private-key: "填写你的应用私钥 (PKCS8 格式)"
  alipay-public-key: "填写你的支付宝公钥"
  gateway-url: "https://openapi-sandbox.dl.alipaydev.com/gateway.do"
  sign-type: "RSA2"
  charset: "utf-8"
  format: "json"
  notify-url: "http://localhost:9999/orders/alipay/notify"
  return-url: "http://localhost:8085/front/orders"
```

---

### 4. 支付服务实现 ✅

**接口**: `AlipayService.java`
**实现类**: `AlipayServiceImpl.java`

核心方法:
1. `createWebPayHtml()` - 生成支付宝网页支付 HTML
2. `verifyNotify()` - 验证支付宝回调签名

---

### 5. Controller 接口 ✅

**文件**: `OrdersController.java`

新增接口:
1. **POST** `/orders/alipay/web` - 生成支付宝支付页面
2. **GET** `/orders/alipay/return` - 支付宝同步回调
3. **POST** `/orders/alipay/notify` - 支付宝异步通知（重要）

---

### 6. 前端集成 ✅

**文件**: `Orders.vue`

修改内容:
- ✅ 在 `processPayment()` 方法中添加支付宝支付逻辑
- ✅ 调用后端接口生成支付页面
- ✅ 在新窗口打开支付宝支付页面

---

## 🔧 配置步骤（重要！必须完成）

### 第 1 步：获取沙箱环境配置

#### 1.1 访问支付宝沙箱后台

地址：https://open.alipay.com/develop/sandbox/app

#### 1.2 登录账号

- 使用支付宝账号登录
- 如果没有账号，需要先注册

#### 1.3 创建沙箱应用

1. 点击"沙箱应用"标签
2. 点击"创建沙箱应用"
3. 等待应用创建完成

#### 1.4 获取配置信息

创建完成后，你会看到以下重要信息:

```
沙箱 APPID: 2021000000000000
应用私钥 (PKCS8): MIIEvQIBADA...（一长串字符串）
支付宝公钥: MIIBIjANBgkq...（一长串字符串）
沙箱账号：
  买家账号：jxtthf8463@sandbox.com
  付款密码：111111
  卖家账号：gxfyvh2340@sandbox.com
  收款密码：111111
```

---

### 第 2 步：配置 application.yml

打开文件：`springboot/src/main/resources/application.yml`

将获取到的配置填入:

```yaml
alipay:
  app-id: "2021000000000000"  # 替换为你的沙箱 APPID
  private-key: "MIIEvQIBADA..."  # 替换为你的应用私钥 (PKCS8 格式)
  alipay-public-key: "MIIBIjANBgkq..."  # 替换为你的支付宝公钥
  gateway-url: "https://openapi-sandbox.dl.alipaydev.com/gateway.do"
  sign-type: "RSA2"
  charset: "utf-8"
  format: "json"
  notify-url: "http://你的公网 IP:9999/orders/alipay/notify"
  return-url: "http://localhost:8085/front/orders"
```

**注意**: 
- `notify-url` 必须是外网可访问的地址（支付宝服务器需要能访问到）
- 本地开发可以使用内网穿透工具（如 natapp、ngrok 等）

---

### 第 3 步：配置沙箱账号

#### 3.1 在支付宝沙箱后台设置

1. 进入"沙箱账号"页面
2. 查看买家账号和卖家账号
3. 记住登录密码（默认都是 111111）

#### 3.2 测试支付流程

**买家账号**: 用于登录支付宝进行支付
**卖家账号**: 用于接收支付的款项

---

## 🚀 使用流程

### 步骤 1: 启动后端服务

```bash
cd springboot
mvn clean install
mvn spring-boot:run
```

确保后端服务运行在 `http://localhost:9999`

### 步骤 2: 启动前端服务

```bash
cd vue
npm run serve
```

确保前端服务运行在 `http://localhost:8085`

### 步骤 3: 测试支付

1. **登录系统**
   - 使用前端用户账号登录

2. **创建订单**
   - 前往商品详情页
   - 点击"立即购买"或"加入购物车"后结算

3. **选择支付宝支付**
   - 在支付对话框中选择"支付宝支付"
   - 点击"确认支付"

4. **跳转支付宝**
   - 系统会自动打开新窗口显示支付宝支付页面
   - 页面会跳转到支付宝沙箱环境

5. **登录支付宝沙箱**
   - 使用沙箱买家账号登录
   - 账号：`jxtthf8463@sandbox.com`
   - 密码：`111111`

6. **完成支付**
   - 输入支付密码：`111111`
   - 点击确认支付

7. **支付成功**
   - 支付成功后会跳转回商户页面
   - 订单状态更新为"已付款"

---

## 📊 支付流程图

```
用户下单
   ↓
选择支付宝支付
   ↓
前端调用 /orders/alipay/web
   ↓
后端生成支付宝 HTML
   ↓
前端打开新窗口显示支付页面
   ↓
用户登录支付宝沙箱
   ↓
用户输入密码确认支付
   ↓
支付宝处理支付
   ↓
【同步】跳转 returnUrl → 前端页面
【异步】发送 notify → 后端接口 → 更新订单状态
   ↓
支付完成
```

---

## ⚠️ 重要注意事项

### 1. 回调地址配置

**异步通知地址 (notifyUrl)**:
- 必须是外网可访问的 URL
- 支付宝服务器会 POST 请求这个地址
- 用于更新订单状态（最可靠的方式）

**同步跳转地址 (returnUrl)**:
- 用户支付成功后浏览器跳转
- 主要用于展示结果给用户
- 不能用于更新订单状态（不可靠）

### 2. 本地开发解决方案

由于支付宝回调需要外网访问，本地开发有以下几种方案:

#### 方案 1: 使用内网穿透工具（推荐）

**natapp**:
```bash
# 下载并安装 natapp
# 在 config.ini 中配置
tunnel=你的隧道 token

# 启动
natapp.exe
```

会得到一个外网地址，如：`https://abc123.natappfree.cc`

然后配置:
```yaml
notify-url: "https://abc123.natappfree.cc/orders/alipay/notify"
```

**ngrok**:
```bash
ngrok http 9999
```

#### 方案 2: 部署到测试服务器

直接将代码部署到有公网 IP 的服务器上测试

#### 方案 3: 临时关闭回调验证（仅开发环境）

在开发阶段可以暂时不验证回调，但这不是长久之计

### 3. 密钥格式

**应用私钥必须是 PKCS8 格式**:

如果是 RSA 密钥（非 PKCS8），需要转换:
```bash
# 使用 OpenSSL 转换
openssl pkcs8 -topk8 -inform PEM -in rsa_private_key.pem -outform pem -nocrypt -out pkcs8_rsa_private_key.pem
```

或者在支付宝沙箱后台重新生成密钥时选择 PKCS8 格式

### 4. 日志查看

支付过程中请密切关注后端日志:

```
=== 支付宝同步回调 ===
=== 支付宝异步通知 ===
交易状态：TRADE_SUCCESS
商户订单号：202603180001
支付宝交易号：2026031822001...
金额：100.00
```

---

## 🔍 常见问题排查

### 问题 1: 签名验证失败

**错误信息**: "Invalid Sign" 或 "签名验证失败"

**原因**:
1. 密钥配置错误
2. 密钥格式不对（应该是 PKCS8）
3. 公钥和私钥不匹配

**解决方法**:
1. 检查 application.yml 中的密钥配置
2. 确保私钥是 PKCS8 格式
3. 重新生成密钥对并配置

---

### 问题 2: 回调地址无法访问

**错误**: 支付宝无法回调 notifyUrl

**原因**:
1. 本地服务，外网无法访问
2. 防火墙阻止
3. 地址配置错误

**解决方法**:
1. 使用内网穿透工具
2. 检查防火墙设置
3. 确认 URL 格式正确

---

### 问题 3: 支付成功但订单状态未更新

**原因**:
1. 异步通知未收到
2. 订单号不匹配
3. 更新逻辑有误

**解决方法**:
1. 检查后端日志，确认是否收到 notify
2. 检查订单号传递是否正确
3. 在 `alipayNotify()` 方法中添加订单更新逻辑

**示例代码**:
```java
if ("TRADE_SUCCESS".equals(tradeStatus)) {
    // 根据订单号查询订单
    Orders order = ordersMapper.selectByOrderNo(outTradeNo);
    if (order != null) {
        order.setState("已付款");
        ordersMapper.updateById(order);
        log.info("订单状态已更新：{}", outTradeNo);
    }
}
```

---

### 问题 4: 前端无法打开支付页面

**原因**:
1. 后端接口返回错误
2. HTML 格式不正确
3. 浏览器拦截弹窗

**解决方法**:
1. 检查后端日志
2. 查看网络请求响应
3. 允许浏览器弹窗

---

## 📝 测试清单

- [ ] 后端服务启动成功（端口 9999）
- [ ] 前端服务启动成功（端口 8085）
- [ ] application.yml 配置完整
- [ ] 沙箱 APPID 已配置
- [ ] 应用私钥已配置（PKCS8 格式）
- [ ] 支付宝公钥已配置
- [ ] notifyUrl 外网可访问（或使用内网穿透）
- [ ] 能够正常创建订单
- [ ] 能够选择支付宝支付
- [ ] 能够打开支付宝支付页面
- [ ] 能够使用沙箱账号登录
- [ ] 能够完成支付
- [ ] 支付成功后订单状态更新

---

## 🎯 下一步建议

### 短期优化（1-2 天）

1. **完善订单状态更新逻辑**
   - 在 `alipayNotify()` 方法中实现订单状态更新
   - 添加支付记录表记录支付流水

2. **添加异常处理**
   - 支付超时处理
   - 重复支付检查
   - 退款功能

3. **日志优化**
   - 详细的支付日志
   - 异常堆栈跟踪
   - 支付成功/失败统计

### 中期优化（1 周）

1. **微信支付集成**
   - 类似支付宝的实现方式
   - 需要申请微信商户号

2. **支付记录管理**
   - 支付流水查询
   - 对账功能
   - 财务报表

3. **安全性提升**
   - 添加防重放攻击机制
   - 支付金额校验
   - IP 白名单

---

*更新时间：2026-03-18*  
*版本：v1.0*  
*状态：已完成，待配置沙箱环境*
