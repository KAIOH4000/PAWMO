# PAWMO 宠物用品商城 (PAWMO Pet Supplies Mall)

## 1. 项目简介

**PAWMO 宠物用品商城** 是一个基于 Spring Boot 和 Vue.js 构建的前后端分离的电商项目。项目实现了完整的电商核心业务流程，包括用户注册登录、商品浏览、购物车、下单支付、订单管理、售后服务以及后台管理系统等功能。

该项目旨在作为一套功能完善、代码结构清晰的 Spring Boot + Vue 全栈开发学习与实践的脚手架。

## 2. 主要功能模块

- **前台商城 (vue)**
    - 用户注册与登录
    - 商品首页、分类与搜索
    - 商品详情展示
    - 购物车功能
    - 下单与在线支付 (集成支付宝沙箱)
    - 个人中心 (订单管理、售后申请)
    - AI 客服智能问答

- **后台管理系统 (springboot + vue)**
    - 管理员登录
    - 首页数据看板
    - 用户管理
    - 商品管理 (分类、信息)
    - 订单管理 (发货、详情查看)
    - 售后管理 (审核、退款)
    - 公告管理

## 3. 技术栈

### 后端 (springboot)

- **核心框架**: Spring Boot 2.7.0
- **持久层**: MyBatis-Plus
- **数据库**: MySQL 8.0
- **安全认证**: JWT (JSON Web Token)
- **支付集成**: Alipay Sandbox
- **工具库**: Lombok, Hutool

### 前端 (vue)

- **核心框架**: Vue.js 2
- **UI 框架**: Element UI
- **HTTP 通信**: Axios
- **路由**: Vue Router
- **状态管理**: Vuex (少量使用)

## 4. 本地开发运行指南

### 准备工作

1.  **环境**: 确保已安装 `Java 8+`, `Maven 3.x`, `Node.js 14+`, `MySQL 8.0`。
2.  **数据库**:
    - 创建一个名为 `pawmo_db` 的数据库。
    - 将项目根目录下的 `pawmo_db.sql` 文件导入到该数据库中。
3.  **克隆/下载项目**: 将项目代码放置在本地。

### 启动后端服务

1.  使用 IntelliJ IDEA 打开 `springboot` 目录。
2.  修改 `src/main/resources/application.yml` 文件中的数据库连接信息 (用户名和密码)。
3.  找到 `src/main/java/com/example/springboot/Application.java` 文件，直接运行 `main` 方法。
4.  服务默认启动在 `http://localhost:9090`。

### 启动前端项目

1.  使用 VS Code 或其他编辑器打开 `vue` 目录。
2.  在 `vue` 目录下打开终端，执行以下命令安装依赖：
    ```bash
    npm install
    ```
3.  安装完成后，执行以下命令启动项目：
    ```bash
    npm run serve
    ```
4.  项目默认启动在 `http://localhost:8080`。在浏览器中打开此地址即可访问。

## 5. 项目演示
<img width="1360" height="615" alt="图片1" src="https://github.com/user-attachments/assets/fcabc78b-a727-4d16-b8ac-970e3720f50e" />
<img width="1360" height="615" alt="图片2" src="https://github.com/user-attachments/assets/659c3649-3d80-449f-b238-edc1e04065be" />
<img width="1360" height="615" alt="图片3" src="https://github.com/user-attachments/assets/82e692fb-13a5-4fab-885f-5a1afa21d2c1" />
<img width="1360" height="615" alt="图片4" src="https://github.com/user-attachments/assets/e0f8195c-b723-434c-a89e-c8a440076115" />
<img width="1360" height="615" alt="图片5" src="https://github.com/user-attachments/assets/799f886a-e149-4703-b83d-6d23097ef6e4" />
<img width="593" height="269" alt="图片7" src="https://github.com/user-attachments/assets/15184f42-d562-4fb6-8dd6-e8179255f9c4" />

- **登录页面**: `http://localhost:8080/login`
- **默认管理员账号**: `admin` / `123`
- **默认普通用户账号**: `tset3` / `123456`
