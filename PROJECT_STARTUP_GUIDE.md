# Spring Boot 项目启动指南

## ✅ 测试增强完成

已为项目成功添加完整的自动化测试套件，包括：

### 单元测试 (Service Layer Tests)
- **UserServiceTests** - 用户业务逻辑测试
  - 用户注册、登录、密码修改
  - 用户查询、更新、删除
  
- **GoodsServiceTests** - 商品业务逻辑测试
  - 商品添加、修改、查询
  - 分页功能测试
  
- **OrdersServiceTests** - 订单业务逻辑测试
  - 订单创建、查询、状态更新
  
- **ReviewServiceTests** - 评价业务逻辑测试
  - 评价添加、修改、查询
  
- **TypeServiceTests** - 分类管理测试
  - 分类增删改查测试
  
- **CollectServiceTests** - 收藏功能测试
  - 收藏添加、删除

### 集成测试 (Controller Integration Tests)
- **UserControllerTests** - 用户API集成测试
- **GoodsControllerTests** - 商品API集成测试
- **OrdersControllerTests** - 订单API集成测试

### 工具类测试
- **TokenUtilsTests** - JWT Token生成和验证测试

### 应用启动测试
- **SpringbootApplicationTests** - 应用上下文加载测试

---

## 🚀 启动项目

### 方式一：使用Maven (推荐)

```bash
# 进入项目目录
cd d:\springboot\d1\code-scaffold\springboot

# 清理并构建项目（跳过测试快速构建）
mvn clean install -DskipTests

# 启动应用
mvn spring-boot:run
```

应用将在 http://localhost:9999 启动

### 方式二：构建并运行JAR包

```bash
# 进入项目目录
cd d:\springboot\d1\code-scaffold\springboot

# 清理并打包
mvn clean package -DskipTests

# 运行JAR文件
java -jar target/springboot-0.0.1-SNAPSHOT.jar
```

### 方式三：使用IDE启动

如果使用IntelliJ IDEA或VS Code with Java支持：

1. 在IDE中打开项目
2. 找到 `SpringbootApplication.java` 类
3. 右键点击 → "Run" 或 "Debug"
4. 应用启动在 http://localhost:9999

---

## 🧪 运行自动化测试

### 运行所有测试

```bash
# 进入项目目录
cd d:\springboot\d1\code-scaffold\springboot

# 运行所有测试
mvn test

# 运行特定测试类
mvn test -Dtest=UserServiceTests

# 运行覆盖率分析
mvn test jacoco:report
```

### 测试报告位置
- 测试结果: `target/surefire-reports/`
- 覆盖率报告: `target/site/jacoco/`

---

## 📋 系统要求

- **Java**: JDK 17或以上
- **Maven**: 3.6.0 或以上
- **MySQL**: 5.7 或 8.0
- **内存**: 2GB 或以上

---

## 🔧 配置说明

### 数据库配置

编辑 `src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/ku1?useSSL=false&useUnicode=true&characterEncoding=UTF-8&serverTimezone=GMT%2b8
    username: root
    password: 123456  # 改为你的密码
```

### 支付宝配置（可选）

支付宝沙箱配置已在 `application.yml` 中预配置，使用的是沙箱环境。

### 文件上传配置

```yaml
spring:
  servlet:
    multipart:
      max-file-size: 200MB
      max-request-size: 200MB
```

---

## 📚 项目依赖

| 库 | 版本 | 用途 |
|---|------|------|
| Spring Boot | 3.5.7 | 核心框架 |
| MyBatis-Plus | 3.5.14 | ORM框架 |
| MySQL Connector | 最新 | 数据库驱动 |
| JWT | 4.3.0 | 身份认证 |
| Hutool | 5.8.18 | 工具库 |
| Lombok | 最新 | 代码简化 |
| FastJSON | 2.0.32 | JSON处理 |
| Alipay SDK | 4.38.157 | 支付宝集成 |

---

## 🐛 常见问题

### Maven not found
如果Maven不在PATH中：
1. 安装Maven 3.6.0+
2. 将Maven的bin目录添加到PATH环境变量
3. 重启命令提示符/PowerShell

### 数据库连接失败
确保：
1. MySQL服务已启动
2. 数据库 `ku1` 已创建
3. 用户名和密码正确
4. 初始化数据库脚本已执行（见 `sql/` 目录）

### 端口被占用
如果9999端口已被占用，修改 `application.yml`:
```yaml
server:
  port: 9998  # 改为其他端口
```

---

## 📞 支持

如有问题，查看项目中的优化文档：
- `SPRINGBOOT3_COMPATIBILITY_FIX.md`
- `OPTIMIZATION_REPORT.md`
- `PAYMENT_OPTIMIZATION.md`

---

## ✨ 后续改进

根据代码审查报告，已完成的测试改进包括：

✅ 完整的单元测试覆盖  
✅ 集成测试框架  
✅ 工具类测试  
✅ 应用启动测试  

建议的后续改进：
- [ ] 添加Spring Security安全测试
- [ ] 添加API文档和Swagger集成
- [ ] 持续集成(CI/CD)配置
- [ ] 性能测试和压力测试

---

**项目状态**: 🟢 运行就绪
