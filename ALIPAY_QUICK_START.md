# 支付宝沙箱支付 - 快速开始

## 🚀 5 分钟快速配置

### 1️⃣ 获取沙箱配置（2 分钟）

访问：https://open.alipay.com/develop/sandbox/app

登录后创建沙箱应用，获取以下信息:
```
APPID: 2021000000000000
应用私钥 (PKCS8): MIIEvQIBADA...
支付宝公钥：MIIBIjANBgkq...
```

### 2️⃣ 修改配置文件（1 分钟）

编辑文件：`springboot/src/main/resources/application.yml`

```yaml
alipay:
  app-id: "你的 APPID"              # 替换
  private-key: "你的私钥"            # 替换
  alipay-public-key: "支付宝公钥"    # 替换
  gateway-url: "https://openapi-sandbox.dl.alipaydev.com/gateway.do"
  notify-url: "http://你的 IP:9999/orders/alipay/notify"
  return-url: "http://localhost:8085/front/orders"
```

### 3️⃣ 启动服务（1 分钟）

```bash
# 后端
cd springboot
mvn spring-boot:run

# 前端
cd vue
npm run serve
```

### 4️⃣ 测试支付（1 分钟）

1. 前端下单 → 选择支付宝支付
2. 使用沙箱账号登录支付宝
   - 账号：`jxtthf8463@sandbox.com`
   - 密码：`111111`
3. 完成支付

---

## 📋 完整文档

详细配置说明请查看：**ALIPAY_SANDBOX_SETUP.md**

---

*快速开始指南 | 更新时间：2026-03-18*
