<template>
  <div class="form-container">
    <el-card class="form-card">
      <div slot="header" class="card-header">
        <span>个人信息</span>
      </div>

      <el-upload
        class="avatar-uploader"
        action="/api/file/upload"
        :show-file-list="false"
        :on-success="handleAvatarSuccess"
        style="text-align: center; margin-bottom: 20px;">
        <img v-if="user.avatar" :src="user.avatar" class="avatar">
        <i v-else class="el-icon-plus avatar-uploader-icon"></i>
      </el-upload>

      <el-form :model="user" label-width="100px" class="user-form">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="user.username" placeholder="用户名" disabled />
        </el-form-item>
        <el-form-item label="姓名" prop="name">
          <el-input v-model="user.name" placeholder="姓名" />
        </el-form-item>
        <el-form-item label="手机" prop="phone">
          <el-input v-model="user.phone" placeholder="手机" />
        </el-form-item>
        <el-form-item label="年龄" prop="age">
          <el-input v-model="user.age" placeholder="年龄" />
        </el-form-item>
        <el-form-item label="个人介绍" prop="infos">
          <el-input type="textarea" v-model="user.infos" placeholder="个人介绍" />
        </el-form-item>
        <el-form-item label="余额" prop="account">
          {{user.account}}
        </el-form-item>
        <div class="form-actions">
          <el-button type="primary" @click="update">保存</el-button>
          <el-button type="success" @click="$router.push('/front/password')">修改密码</el-button>
          <el-button type="warning" @click="handleOpen">充值</el-button>
        </div>
      </el-form>
    </el-card>

    <el-dialog title="充值" :visible.sync="dialogFormVisible" width="30%" :close-on-click-modal="false">
      <el-form label-width="100px" style="padding-right: 40px" :model="user" ref="ruleForm">
        <el-form-item prop="account" label="金额">
          <el-input v-model="account" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item prop="type" label="支付方式">
          <el-radio-group v-model="type" size="small">
            <el-radio-button label="微信支付"></el-radio-button>
            <el-radio-button label="支付宝支付"></el-radio-button>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="save">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "Person",
  data() {
    return {
      user: JSON.parse(localStorage.getItem('user') || '{}'),
      dialogFormVisible: false,
      account: 0,
      type: '微信支付',
      loading: false
    }
  },
  created() {
    this.loadUser()
  },
  methods: {
    loadUser(){
      if (!this.user || !this.user.id){
        this.$notify.warning({title: '提示', message: '请登录', showClose: false, duration: 2000});
        this.$router.push('/login')
        return;
      }
      this.loading = true
      this.$request.get('/user/selectById/'+ this.user.id).then(res => {
        if (res.code === '200' && res.data) {
          // 保存当前的 token
          const currentToken = this.user.token;
          // 更新用户信息，同时保留 token
          this.user = { ...res.data, token: currentToken }
        } else {
          this.$notify.error({title: '错误', message: res.msg || '获取用户信息失败', showClose: false, duration: 2000});
        }
      }).finally(() => {
        this.loading = false
      })
    },
    update() {
      this.$request.put('/user/update', this.user).then(res => {
        if (res.code === '200') {
          this.$message.success('保存成功')
          localStorage.setItem('user', JSON.stringify(this.user))
          this.$emit('update:user', this.user)
        } else {
          this.$notify.error({title: '错误', message: res.msg, showClose: false, duration: 2000});
        }
      })
    },
    handleAvatarSuccess(response) {
      // 如果 response.data 是对象，取其中的 data 字段
      let avatarUrl = response.data
      if (typeof avatarUrl === 'object' && avatarUrl !== null) {
        avatarUrl = avatarUrl.data || avatarUrl.url || JSON.stringify(avatarUrl)
      }
      this.user.avatar = avatarUrl || response
    },
    handleOpen(){
      this.dialogFormVisible = true
    },
    save() {
      this.user.account = Number(this.user.account) + Number(this.account)
      this.$request.put('/user/update', this.user).then(res => {
        if (res.code === '200') {
          this.$notify.success({title: '成功', message: '充值成功', showClose: false, duration: 2000});
          this.dialogFormVisible = false
        } else {
          this.$notify.error({title: '错误', message: res.msg, showClose: false, duration: 2000});
        }
      })
    },
  }
}
</script>

<style scoped>
.form-container {
  display: flex;
  justify-content: center;
  padding: 10px;
  box-sizing: border-box;
}

.form-card {
  width: 30%;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  border-radius: 12px;
  transition: box-shadow 0.3s ease;
}

.card-header {
  font-size: 18px;
  font-weight: 600;
  color: #303133;
  padding: 16px 20px;
  border-bottom: 1px solid #f0f0f0;
  background-color: #fff;
}

.form-card:hover {
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.15);
}

.user-form {
  padding: 20px 30px;
}

.avatar-section {
  text-align: center;
  margin: 20px 0;
}

:deep(.el-form-item__label) {
  font-weight: bold;
  color: #333;
}

:deep(.avatar-uploader .el-upload) {
  border: 1px dashed #d9d9d9;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  border-radius: 50%;
  width: 78px;
  height: 78px;
  display: inline-block;
  line-height: 178px;
  text-align: center;
  transition: border-color 0.3s;
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
  border-radius: 50%;
  background-color: #f8f8f8;
}

/* 已上传头像样式 */
.avatar {
  width: 78px;
  height: 78px;
  display: block;
  border-radius: 50%;
  object-fit: cover;
  transition: transform 0.3s ease;
}

.avatar:hover {
  transform: scale(1.05);
}

.form-actions {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 20px;
  margin-top: 30px;
}

.form-actions .el-button {
  min-width: 100px;
}
</style>
