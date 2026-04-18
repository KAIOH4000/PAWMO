<template>
  <div class="manager-container">
    <!--  头部  -->
    <div class="manager-header">
      <div class="manager-header-left">
        <img src="@/assets/logo.svg" />
        <div class="title">PAWMO 商家后台</div>
      </div>

      <div class="manager-header-center">
        <el-breadcrumb separator-class="el-icon-arrow-right">
          <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
          <el-breadcrumb-item :to="{ path: $route.path }">{{ $route.meta.name }}</el-breadcrumb-item>
        </el-breadcrumb>
      </div>

      <div class="manager-header-right">
        <el-dropdown placement="bottom">
          <div class="avatar">
            <!-- FIX: 使用公开可访问的 URL 作为默认头像 -->
            <img :src="user.avatar || 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'" />
            <div>{{ user.name ||  '商家' }}</div>
          </div>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item @click.native="logout">退出登录</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </div>
    </div>

    <!--  主体  -->
    <div class="manager-main">
      <!--  侧边栏  -->
      <div class="manager-main-left">
        <el-menu
            :default-active="$route.path"
            :default-openeds="['/merchant/home', 'info']"
            class="el-menu-demo"
            router
            style="border: none"
        >
          <el-menu-item index="/merchant/home">
            <i class="el-icon-s-home"></i>
            <span slot="title">首页</span>
          </el-menu-item>
          <el-submenu index="info">
            <template slot="title">
              <i class="el-icon-s-data"></i><span>信息管理</span>
            </template>
            <el-menu-item index="/merchant/goods">
              <i class="el-icon-s-goods"></i>
              <span slot="title">商品信息</span>
            </el-menu-item>
            <el-menu-item index="/merchant/orders">
              <i class="el-icon-s-order"></i>
              <span slot="title">订单信息</span>
            </el-menu-item>
            <el-menu-item index="/merchant/afterSales">
              <i class="el-icon-s-comment"></i>
              <span slot="title">售后管理</span>
            </el-menu-item>
          </el-submenu>
        </el-menu>
      </div>

      <!--  数据表格  -->
      <div class="manager-main-right">
        <router-view @update:user="updateUser" />
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "Merchant",
  data() {
    return {
      user: JSON.parse(localStorage.getItem("user") || "{}"),
    };
  },
  mounted() {
    if (!this.user.id || this.user.role !== 'MERCHANT') {
      this.$router.push("/login");
    }
  },
  methods: {
    updateUser() {
      this.user = JSON.parse(localStorage.getItem('user') || '{}')   // 重新获取最新的用户信息
    },
    logout() {
      localStorage.removeItem("user");
      this.$router.push("/login");
    },
  },
};
</script>

<style>
.manager-container {
  height: 100vh;
  display: flex;
  flex-direction: column;
}
.manager-header {
  height: 60px;
  background-color: #fff;
  display: flex;
  align-items: center;
  border-bottom: 1px solid #ddd;
}
.manager-header-left {
  width: 200px;
  display: flex;
  align-items: center;
  padding-left: 20px;
}
.manager-header-left img {
  width: 40px;
  height: 40px;
}
.manager-header-left .title {
  flex: 1;
  margin-left: 10px;
  font-size: 20px;
  font-weight: bold;
  color: #409eff;
}
.manager-header-center {
  flex: 1;
  width: 0;
  display: flex;
  align-items: center;
}
.manager-header-right {
  width: 200px;
  display: flex;
  align-items: center;
  justify-content: flex-end;
  padding-right: 20px;
}
.manager-header-right .avatar {
  display: flex;
  align-items: center;
  cursor: pointer;
}
.manager-header-right .avatar img {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  margin-right: 10px;
}
.manager-main {
  flex: 1;
  width: 100%;
  display: flex;
}
.manager-main-left {
  width: 200px;
  background-color: #fff;
  border-right: 1px solid #ddd;
}
.manager-main-right {
  flex: 1;
  width: 0;
  padding: 10px;
  background-color: #f8f8ff;
}
</style>
