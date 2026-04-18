<template>
  <div>
    <el-card>
      <div slot="header">
        <span>店铺信息设置</span>
      </div>

      <el-form :model="form" label-width="120px" style="max-width: 600px">
        <el-form-item label="店铺名称">
          <el-input v-model="form.shopName" placeholder="请输入店铺名称"></el-input>
        </el-form-item>
        <el-form-item label="店铺介绍">
          <el-input v-model="form.shopDesc" type="textarea" :rows="3" placeholder="请输入店铺介绍"></el-input>
        </el-form-item>
        <el-form-item label="退货收货人">
          <el-input v-model="form.returnReceiver" placeholder="请输入退货收货人"></el-input>
        </el-form-item>
        <el-form-item label="退货联系电话">
          <el-input v-model="form.returnPhone" placeholder="请输入退货联系电话"></el-input>
        </el-form-item>
        <el-form-item label="退货地址">
          <el-input v-model="form.returnAddress" type="textarea" :rows="2" placeholder="请输入详细退货地址"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="save">保存</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card style="margin-top: 20px">
      <div slot="header">
        <span>个人信息</span>
      </div>

      <el-form :model="personForm" label-width="100px" style="max-width: 600px">
        <el-form-item label="用户名">
          <el-input v-model="personForm.username" disabled></el-input>
        </el-form-item>
        <el-form-item label="姓名">
          <el-input v-model="personForm.name"></el-input>
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="personForm.phone"></el-input>
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="personForm.email"></el-input>
        </el-form-item>
        <el-form-item label="个人介绍">
          <el-input v-model="personForm.infos" type="textarea"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="updatePerson">更新个人信息</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
export default {
  name: 'MerchantPerson',
  data() {
    return {
      form: {},
      personForm: {}
    }
  },
  created() {
    this.loadInfo()
  },
  methods: {
    loadInfo() {
      const user = JSON.parse(localStorage.getItem('user') || '{}')
      this.$request.get('/user/selectById/' + user.id).then(res => {
        if (res.code === '200') {
          this.form = JSON.parse(JSON.stringify(res.data))
          this.personForm = JSON.parse(JSON.stringify(res.data))
        }
      })
    },
    save() {
      this.$request.put('/user/update', this.form).then(res => {
        if (res.code === '200') {
          this.$message.success('保存成功')
          const user = JSON.parse(localStorage.getItem('user') || '{}')
          Object.assign(user, this.form)
          localStorage.setItem('user', JSON.stringify(user))
          this.$emit('update:user', user)
        }
      })
    },
    updatePerson() {
      this.$request.put('/user/update', this.personForm).then(res => {
        if (res.code === '200') {
          this.$message.success('更新成功')
          const user = JSON.parse(localStorage.getItem('user') || '{}')
          Object.assign(user, this.personForm)
          localStorage.setItem('user', JSON.stringify(user))
          this.$emit('update:user', user)
        }
      })
    }
  }
}
</script>