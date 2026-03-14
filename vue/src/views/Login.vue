<template>
  <div class="login-container">
    <!-- 动态背景粒子 -->
    <div class="particles">
      <div class="particle" v-for="n in 20" :key="n" :style="getParticleStyle(n)"></div>
    </div>
    
    <div class="left-section">
      <div class="brand-content">
        <h1 class="title">
          <span class="title-icon">🐾</span>
          PAWMO
        </h1>
        <p class="description">PAWMO，专宠你的每一份热爱</p>
        <p class="descr">一站式宠物用品平台，让宠爱更省心</p>
        <div class="feature-list">
          <div class="feature-item" v-for="(item, index) in features" :key="index" :style="{ animationDelay: index * 0.2 + 's' }">
            <i :class="item.icon"></i>
            <span>{{ item.text }}</span>
          </div>
        </div>
      </div>
      <img src="../assets/login.jpg" alt="登录插画" class="illustration" />
    </div>
    
    <div class="right-section">
      <div class="login-card">
        <div class="card-header">
          <h1 class="welcome-title">欢迎回来</h1>
          <p class="welcome-subtitle">请登录您的账号</p>
        </div>
        
        <div class="login-type-wrapper">
          <p class="login-type">账号密码登录</p>
        </div>
        
        <el-form :model="user" :rules="rules" ref="loginRef" class="login-form" @keyup.enter.native="login">
          <el-form-item prop="username">
            <el-input 
              v-model="user.username" 
              size="medium" 
              placeholder="请输入账号" 
              prefix-icon="el-icon-user"
              class="animated-input"
              @focus="handleFocus"
              @blur="handleBlur">
            </el-input>
          </el-form-item>
          <el-form-item prop="password">
            <el-input 
              v-model="user.password" 
              size="medium" 
              type="password" 
              placeholder="请输入密码" 
              prefix-icon="el-icon-lock" 
              show-password
              class="animated-input"
              @focus="handleFocus"
              @blur="handleBlur">
            </el-input>
          </el-form-item>
          <el-form-item prop="role">
            <el-select v-model="user.role" placeholder="请选择角色" style="width: 100%" class="animated-select">
              <el-option label="管理员" value="ADMIN">
                <i class="el-icon-s-custom" style="margin-right: 8px; color: #409eff;"></i>管理员
              </el-option>
              <el-option label="用户" value="USER">
                <i class="el-icon-user" style="margin-right: 8px; color: #67c23a;"></i>用户
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button 
              type="primary" 
              class="login-btn" 
              @click="login"
              :loading="loading"
              :class="{ 'btn-pulse': !loading }">
              <span v-if="!loading">登 录</span>
              <span v-else>登录中...</span>
            </el-button>
          </el-form-item>

          <div class="links">
            <router-link to="/register" class="register-link">
              <i class="el-icon-circle-plus-outline"></i>
              注册账号
            </router-link>
          </div>
        </el-form>
      </div>
    </div>

    <el-dialog title="忘记密码" :visible.sync="forgetPassDialogVis" width="30%">
      <el-form :model="forgetUserForm" label-width="80px">
        <el-form-item label="用户名">
          <el-input v-model="forgetUserForm.username" autocomplete="off" placeholder="请输入用户名"></el-input>
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="forgetUserForm.phone" autocomplete="off" placeholder="请输入手机号"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="forgetPassDialogVis = false">取 消</el-button>
        <el-button type="primary" @click="resetPassword">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'Login',
  data() {
    return {
      forgetUserForm: {},
      forgetPassDialogVis: false,
      loading: false,
      user: {
        username: '',
        password: '',
        role: ''
      },
      rules: {
        username: [
          { required: true, message: '请输入账号', trigger: 'blur' },
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
        ],
        role: [
          { required: true, message: '请选择角色', trigger: 'blur' },
        ],
      },
      features: [
        { icon: 'el-icon-goods', text: '优质宠物用品' },
        { icon: 'el-icon-truck', text: '快速配送服务' },
        { icon: 'el-icon-service', text: '贴心售后保障' }
      ]
    }
  },
  methods: {
    handleForgetPass() {
      this.forgetUserForm = {}
      this.forgetPassDialogVis = true
    },
    resetPassword() {
      this.$request.put('/password', this.forgetUserForm).then(res => {
        if (res.code === '200') {
          this.$message.success('重置成功')
          this.forgetPassDialogVis = false
        } else {
          this.$notify.error({title: '成功', message: res.msg, showClose: false, duration: 2000});
        }
      })
    },
    handleFocus(e) {
      e.target.closest('.el-form-item').classList.add('is-focused')
    },
    handleBlur(e) {
      e.target.closest('.el-form-item').classList.remove('is-focused')
    },
    getParticleStyle(n) {
      return {
        left: Math.random() * 100 + '%',
        animationDelay: Math.random() * 20 + 's',
        animationDuration: (15 + Math.random() * 10) + 's'
      }
    },
    login() {
      this.$refs['loginRef'].validate((valid) => {
        if (valid) {
          this.loading = true
          this.$request.post('/login', this.user).then(res => {
            if (res.code === '200') {
              localStorage.setItem("user", JSON.stringify(res.data))
              this.$notify.success({title: '成功', message: '登录成功', showClose: false, duration: 2000});
              setTimeout(() => {
                if (this.user.role == 'ADMIN'){
                  this.$router.push('/')
                } else {
                  this.$router.push('/front/home')
                }
              }, 500)
            } else {
              this.$notify.error({message: res.msg, showClose: false, duration: 2000});
              this.loading = false
            }
          }).catch(() => {
            this.loading = false
          })
        }
      })
    }
  }
}
</script>

<style scoped>
/* 动画定义 */
@keyframes float {
  0%, 100% { transform: translateY(0px) rotate(0deg); }
  50% { transform: translateY(-20px) rotate(5deg); }
}

@keyframes pulse {
  0%, 100% { box-shadow: 0 0 0 0 rgba(255, 159, 67, 0.4); }
  50% { box-shadow: 0 0 0 10px rgba(255, 159, 67, 0); }
}

@keyframes slideInLeft {
  from { opacity: 0; transform: translateX(-50px); }
  to { opacity: 1; transform: translateX(0); }
}

@keyframes slideInRight {
  from { opacity: 0; transform: translateX(50px); }
  to { opacity: 1; transform: translateX(0); }
}

@keyframes fadeInUp {
  from { opacity: 0; transform: translateY(30px); }
  to { opacity: 1; transform: translateY(0); }
}

@keyframes particle-float {
  0%, 100% { 
    transform: translateY(100vh) rotate(0deg); 
    opacity: 0;
  }
  10% { opacity: 1; }
  90% { opacity: 1; }
  100% { 
    transform: translateY(-100vh) rotate(720deg); 
    opacity: 0;
  }
}

.login-container {
  display: flex;
  height: 100vh;
  position: relative;
  overflow: hidden;
  background: linear-gradient(135deg, #ff9f43 0%, #ffcc99 100%);
}

/* 粒子背景 */
.particles {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  overflow: hidden;
  pointer-events: none;
  z-index: 0;
}

.particle {
  position: absolute;
  width: 10px;
  height: 10px;
  background: rgba(255, 255, 255, 0.3);
  border-radius: 50%;
  animation: particle-float linear infinite;
}

.left-section {
  flex: 6;
  position: relative;
  z-index: 1;
  color: #fff;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 40px;
  animation: slideInLeft 0.8s ease-out;
}

.brand-content {
  text-align: center;
  margin-bottom: 40px;
}

.title {
  font-size: 48px;
  font-weight: bold;
  margin-bottom: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 15px;
  text-shadow: 2px 2px 4px rgba(0,0,0,0.2);
}

.title-icon {
  font-size: 52px;
  animation: float 3s ease-in-out infinite;
}

.description {
  font-size: 22px;
  margin-bottom: 10px;
  opacity: 0.95;
  font-weight: 300;
}

.descr {
  font-size: 18px;
  margin-bottom: 30px;
  opacity: 0.8;
}

.feature-list {
  display: flex;
  flex-direction: column;
  gap: 15px;
  margin-top: 20px;
}

.feature-item {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  font-size: 16px;
  opacity: 0;
  animation: fadeInUp 0.6s ease-out forwards;
}

.feature-item i {
  font-size: 20px;
  color: #ffd700;
}

.illustration {
  width: 380px;
  height: auto;
  filter: drop-shadow(0 20px 40px rgba(0,0,0,0.2));
  animation: float 6s ease-in-out infinite;
}

.right-section {
  flex: 4;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 40px;
  position: relative;
  z-index: 1;
  animation: slideInRight 0.8s ease-out;
}

.login-card {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  border-radius: 20px;
  padding: 40px;
  width: 100%;
  max-width: 400px;
  box-shadow: 0 25px 50px -12px rgba(0, 0, 0, 0.25);
  animation: fadeInUp 0.6s ease-out 0.3s both;
}

.card-header {
  text-align: center;
  margin-bottom: 25px;
}

.welcome-title {
  font-size: 28px;
  font-weight: bold;
  margin-bottom: 8px;
  color: #1e293b;
}

.welcome-subtitle {
  font-size: 14px;
  color: #64748b;
  margin: 0;
}

.login-type-wrapper {
  display: flex;
  align-items: center;
  margin-bottom: 25px;
}

.login-type-wrapper::before,
.login-type-wrapper::after {
  content: '';
  flex: 1;
  height: 1px;
  background: linear-gradient(90deg, transparent, #e2e8f0, transparent);
}

.login-type-wrapper::before {
  margin-right: 15px;
}

.login-type-wrapper::after {
  margin-left: 15px;
}

.login-type {
  font-size: 13px;
  color: #64748b;
  white-space: nowrap;
  background: #f1f5f9;
  padding: 4px 12px;
  border-radius: 12px;
}

.login-form {
  width: 100%;
}

.login-form >>> .el-form-item {
  margin-bottom: 20px;
  transition: all 0.3s ease;
}

.login-form >>> .el-form-item.is-focused {
  transform: translateY(-2px);
}

.login-form >>> .el-input__inner {
  border-radius: 10px;
  height: 44px;
  transition: all 0.3s ease;
  border: 1px solid #e2e8f0;
}

.login-form >>> .el-input__inner:focus {
  border-color: #ff9f43;
  box-shadow: 0 0 0 3px rgba(255, 159, 67, 0.1);
  transform: translateY(-1px);
}

.login-btn {
  width: 100%;
  height: 46px;
  font-size: 16px;
  border-radius: 10px;
  font-weight: 500;
  transition: all 0.3s ease;
  background: linear-gradient(135deg, #ff9f43 0%, #ffcc99 100%);
  border: none;
}

.login-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 10px 20px -5px rgba(255, 159, 67, 0.4);
}

.login-btn:active {
  transform: translateY(0);
}

.btn-pulse {
  animation: pulse 2s infinite;
}

.links {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}

.register-link {
  display: flex;
  align-items: center;
  gap: 6px;
  text-decoration: none;
  color: #ff9f43;
  font-size: 14px;
  font-weight: 500;
  padding: 8px 16px;
  border-radius: 8px;
  transition: all 0.3s ease;
}

.register-link:hover {
  background: rgba(255, 159, 67, 0.1);
  transform: translateX(5px);
}

.register-link i {
  font-size: 16px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .login-container {
    flex-direction: column;
  }
  
  .left-section {
    flex: none;
    padding: 30px 20px;
    min-height: 40vh;
  }
  
  .illustration {
    width: 200px;
  }
  
  .title {
    font-size: 32px;
  }
  
  .right-section {
    flex: 1;
    padding: 20px;
  }
  
  .login-card {
    padding: 30px 20px;
  }
}
</style>
