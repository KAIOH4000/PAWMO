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
        <p class="description">加入PAWMO大家庭</p>
        <p class="descr">开启您的宠物用品购物之旅</p>
        <div class="feature-list">
          <div class="feature-item" v-for="(item, index) in features" :key="index" :style="{ animationDelay: index * 0.2 + 's' }">
            <i :class="item.icon"></i>
            <span>{{ item.text }}</span>
          </div>
        </div>
      </div>
      <img src="../assets/login.jpg" alt="注册插画" class="illustration" />
    </div>
    
    <div class="right-section">
      <div class="login-card">
        <div class="card-header">
          <h1 class="welcome-title">创建账号</h1>
          <p class="welcome-subtitle">填写以下信息完成注册</p>
        </div>
        
        <div class="login-type-wrapper">
          <p class="login-type">账号密码注册</p>
        </div>
        
        <el-form :model="user" :rules="rules" ref="registerRef" class="login-form" @keyup.enter.native="register">
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
          <el-form-item prop="phone">
            <el-input 
              v-model="user.phone" 
              size="medium" 
              placeholder="请输入手机号" 
              prefix-icon="el-icon-phone"
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
          <el-form-item prop="confirmPass">
            <el-input 
              prefix-icon="el-icon-check" 
              size="medium" 
              show-password 
              placeholder="请确认密码" 
              v-model="user.confirmPass"
              class="animated-input"
              @focus="handleFocus"
              @blur="handleBlur">
            </el-input>
          </el-form-item>
          <el-form-item prop="role">
            <el-select 
              v-model="user.role" 
              size="medium" 
              placeholder="请选择角色" 
              style="width: 100%"
              class="animated-select">
              <el-option label="用户" value="USER">
                <i class="el-icon-user" style="margin-right: 8px; color: #67c23a;"></i>用户
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button 
              type="success" 
              class="login-btn" 
              @click="register"
              :loading="loading"
              :class="{ 'btn-pulse': !loading }">
              <span v-if="!loading">创 建 账 号</span>
              <span v-else>注册中...</span>
            </el-button>
          </el-form-item>

          <div class="links">
            <router-link to="/login" class="login-link">
              <i class="el-icon-arrow-left"></i>
              已有账号？立即登录
            </router-link>
          </div>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Register',
  data() {
    const validatePassword = (rule, confirmPass, callback) => {
      if (confirmPass === '') {
        callback(new Error('请确认密码'))
      } else if (confirmPass !== this.user.password) {
        callback(new Error('两次输入的密码不一致'))
      } else {
        callback()
      }
    }
    return {
      loading: false,
      user: {
        username: '',
        phone: '',
        password: '',
        confirmPass: '',
        role: ''
      },
      rules: {
        username: [
          { required: true, message: '请输入账号', trigger: 'blur' },
          { min: 4, max: 20, message: '账号长度为4~20个字符', trigger: 'blur' }
        ],
        phone: [
          { required: true, message: '请输入手机号', trigger: 'blur' },
          { pattern: /^1[3-9]\d{9}$/, message: '手机号格式不正确', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          { min: 6, max: 20, message: '密码长度为6~20个字符', trigger: 'blur' }
        ],
        confirmPass: [
          { validator: validatePassword, trigger: 'blur' }
        ],
        role: [
          { required: true, message: '请选择角色', trigger: 'blur' },
        ],
      },
      features: [
        { icon: 'el-icon-present', text: '新用户专享优惠' },
        { icon: 'el-icon-collection-tag', text: '积分奖励体系' },
        { icon: 'el-icon-bell', text: '专属活动通知' }
      ]
    }
  },
  methods: {
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
    register() {
      this.$refs['registerRef'].validate((valid) => {
        if (valid) {
          this.loading = true
          this.$request.post('/register', this.user).then(res => {
            const code = String(res.code)
            if (code === '200') {
              this.$notify.success({title: '成功', message: '注册成功', showClose: false, duration: 2000});
              setTimeout(() => {
                this.$router.push('/login')
              }, 1000)
            } else if (code === '400') {
              this.$notify.warning({message: res.msg || '手机号已被注册', showClose: false, duration: 2000});
              this.loading = false
            } else {
              this.$notify.error({message: res.msg || '系统错误', showClose: false, duration: 2000});
              this.loading = false
            }
          }).catch(() => {
            this.$notify.error({message: '系统错误，请稍后重试', showClose: false, duration: 2000});
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
  0%, 100% { box-shadow: 0 0 0 0 rgba(103, 194, 58, 0.4); }
  50% { box-shadow: 0 0 0 10px rgba(103, 194, 58, 0); }
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
  background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%);
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
  opacity: 0.85;
  filter: drop-shadow(0 20px 40px rgba(0,0,0,0.15)) brightness(1.05) contrast(0.95);
  animation: float 6s ease-in-out infinite;
  mix-blend-mode: multiply;
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
  background: #f0fdf4;
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
  border-color: #67c23a;
  box-shadow: 0 0 0 3px rgba(103, 194, 58, 0.1);
  transform: translateY(-1px);
}

.login-btn {
  width: 100%;
  height: 46px;
  font-size: 16px;
  border-radius: 10px;
  font-weight: 500;
  transition: all 0.3s ease;
  background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%);
  border: none;
}

.login-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 10px 20px -5px rgba(17, 153, 142, 0.4);
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

.login-link {
  display: flex;
  align-items: center;
  gap: 6px;
  text-decoration: none;
  color: #11998e;
  font-size: 14px;
  font-weight: 500;
  padding: 8px 16px;
  border-radius: 8px;
  transition: all 0.3s ease;
}

.login-link:hover {
  background: rgba(17, 153, 142, 0.1);
  transform: translateX(-5px);
}

.login-link i {
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
    min-height: 35vh;
  }
  
  .illustration {
    width: 180px;
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
