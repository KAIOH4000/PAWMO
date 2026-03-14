<template>
  <div class="orders-container">
    <!-- 搜索栏 -->
    <el-card style="margin-bottom: 20px;">
      <div style="display: flex; justify-content: space-between; align-items: center;">
        <div>
          <el-input v-model="name" placeholder="商品名称" style="width: 200px; margin-right: 10px;" clearable></el-input>
          <el-input v-model="orderNo" placeholder="订单编号" style="width: 200px; margin-right: 10px;" clearable></el-input>
          <el-select v-model="state" placeholder="订单状态" style="width: 150px; margin-right: 10px;" clearable>
            <el-option label="待付款" value="待付款"></el-option>
            <el-option label="未发货" value="未发货"></el-option>
            <el-option label="已发货" value="已发货"></el-option>
            <el-option label="已到货" value="已到货"></el-option>
            <el-option label="已完成" value="已完成"></el-option>
          </el-select>
          <el-button type="primary" @click="load(1)">查询</el-button>
          <el-button @click="reset">重置</el-button>
        </div>
      </div>
    </el-card>

    <!-- 订单列表 -->
    <el-card>
      <el-tabs v-model="activeTab" @tab-click="handleTabClick">
        <el-tab-pane label="全部订单" name="all"></el-tab-pane>
        <el-tab-pane label="待付款" name="待付款"></el-tab-pane>
        <el-tab-pane label="未发货" name="未发货"></el-tab-pane>
        <el-tab-pane label="已发货" name="已发货"></el-tab-pane>
        <el-tab-pane label="已完成" name="已完成"></el-tab-pane>
      </el-tabs>

      <el-table :data="tableData" border stripe>
        <el-table-column prop="orderNo" label="订单编号" width="180"></el-table-column>
        <el-table-column prop="name" label="商品信息" width="300">
          <template slot-scope="scope">
            <div style="display: flex; align-items: center; gap: 10px;">
              <el-image
                  v-if="scope.row.cover"
                  style="width: 60px; height: 60px; border-radius: 4px;"
                  :src="scope.row.cover"
                  fit="cover">
              </el-image>
              <div style="flex: 1; overflow: hidden; text-overflow: ellipsis; white-space: nowrap;">
                {{ scope.row.name }}
              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="price" label="价格" width="100">
          <template slot-scope="scope">
            <span style="color: #ff6700; font-weight: bold;">¥{{ scope.row.price }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="nums" label="数量" width="80"></el-table-column>
        <el-table-column prop="state" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getStateType(scope.row.state)">{{ scope.row.state }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="time" label="下单时间" width="180">
          <template slot-scope="scope">
            <span>{{ scope.row.time || '暂无' }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="280" fixed="right">
          <template slot-scope="scope">
            <el-button v-if="scope.row.state === '待付款'" type="warning" size="mini" @click="openPayDialog(scope.row)">支付</el-button>
            <el-button v-if="scope.row.state === '未发货'" type="info" size="mini" @click="goToAfterSales(scope.row)">查看/售后</el-button>
            <el-button v-if="scope.row.state === '已发货'" type="success" size="mini" @click="confirmArrival(scope.row)">确认到货</el-button>
            <el-button v-if="!scope.row.isReviewed && (scope.row.state === '已到货' || scope.row.state === '已完成')" type="primary" size="mini" @click="openReviewDialog(scope.row)">评价</el-button>
            <el-button type="danger" size="mini" @click="del(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <el-pagination
          style="margin-top: 20px; text-align: right;"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pageNum"
          :page-sizes="[5, 10, 20]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
      </el-pagination>
    </el-card>

    <!-- 支付对话框 -->
    <el-dialog title="确认支付" :visible.sync="payDialogVisible" width="500px" :close-on-click-modal="false">
      <div style="padding: 20px;">
        <div style="font-size: 16px; margin-bottom: 15px;">订单编号：{{ currentOrder.orderNo }}</div>
        <div style="font-size: 16px; margin-bottom: 15px;">商品名称：{{ currentOrder.name }}</div>
        <div style="display: flex; justify-content: space-between; align-items: center; padding: 15px; background-color: #fff5f0; border-radius: 8px; margin-bottom: 20px;">
          <span style="font-size: 14px;">应付金额:</span>
          <span style="color: #ff5000; font-size: 28px; font-weight: bold;">¥{{ currentOrder.price }}</span>
        </div>
        <div style="margin-bottom: 15px;">
          <div style="font-weight: bold; margin-bottom: 10px;"><i class="el-icon-wallet"></i> 支付方式</div>
          <el-radio-group v-model="payMethod">
            <el-radio label="balance">余额支付 (可用：¥{{ user.account || 0 }})</el-radio>
            <el-radio label="wechat">微信支付</el-radio>
            <el-radio label="alipay">支付宝支付</el-radio>
            <el-radio label="bank">银行卡支付</el-radio>
          </el-radio-group>
        </div>
      </div>
      <div slot="footer">
        <el-button @click="payDialogVisible = false">取消</el-button>
        <el-button type="warning" style="background-color: #ff5000; border-color: #ff5000;" @click="confirmOrderPay">确认支付</el-button>
      </div>
    </el-dialog>

    <!-- 评价对话框 -->
    <el-dialog title="商品评价" :visible.sync="reviewDialogVisible" width="600px" :close-on-click-modal="false">
      <el-form :model="reviewForm" label-width="80px">
        <el-form-item label="评分">
          <el-rate v-model="reviewForm.score" show-text></el-rate>
        </el-form-item>
        <el-form-item label="评价内容">
          <el-input type="textarea" v-model="reviewForm.content" placeholder="请输入评价内容" rows="4"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="reviewDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitReview">提交评价</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "FrontOrders",
  data() {
    return {
      tableData: [],
      pageNum: 1,
      pageSize: 10,
      name: '',
      orderNo: '',
      state: '',
      total: 0,
      user: JSON.parse(localStorage.getItem('user') || '{}'),
      activeTab: 'all',
      payDialogVisible: false,
      payMethod: 'alipay',  // 修改为支付宝支付
      currentOrder: {},
      reviewDialogVisible: false,
      reviewForm: {
        orderId: null,
        goodsId: null,
        score: 5,
        content: ''
      }
    }
  },
  created() {
    this.load()
  },
  methods: {
    load(pageNum) {
      if (pageNum) this.pageNum = pageNum
      const params = {
        pageNum: this.pageNum,
        pageSize: this.pageSize,
        name: this.name,
        orderNo: this.orderNo
      }
      // 如果当前不是全部订单 tab，则添加状态过滤
      if (this.state) {
        params.state = this.state
      }
      this.$request.get('/orders/selectPage', {
        params: params
      }).then(res => {
        this.tableData = res.data?.records || []
        this.total = res.data?.total
      })
    },
    handleTabClick(tab) {
      this.state = tab.name === 'all' ? '' : tab.name
      this.load(1)
    },
    del(id) {
      this.$confirm('您确认删除吗？', '确认删除', {type: "warning"}).then(response => {
        this.$request.delete('/orders/delete?id=' + id).then(res => {
          if (res.code === '200') {
            this.$notify.success({title: '成功', message: '操作成功', showClose: false, duration: 2000});
            this.load(1)
          } else {
            this.$notify.error({title: '成功', message: res.msg, showClose: false, duration: 2000});
          }
        })
      }).catch(() => {
      })
    },
    reset() {
      this.name = ''
      this.orderNo = ''
      this.state = ''
      this.activeTab = 'all'
      this.load()
    },
    handleCurrentChange(pageNum) {
      this.pageNum = pageNum
      this.load()
    },
    handleSizeChange(pageSize) {
      this.pageSize = pageSize
      this.load()
    },
    openPayDialog(row) {
      this.currentOrder = row
      this.payMethod = 'alipay'  // 强制设置为支付宝支付
      this.loadUserInfo()
      this.payDialogVisible = true
    },
    loadUserInfo() {
      const oldToken = this.user.token // 保存原有的 token
      this.$request.get('/user/selectById/' + this.user.id).then(res => {
        if (res.code === '200') {
          this.user = res.data
          this.user.token = oldToken // 恢复 token
          localStorage.setItem('user', JSON.stringify(this.user))
        }
      })
    },
    confirmOrderPay() {
      // 余额支付需要检查余额
      if (this.payMethod === 'balance' && this.user.account < this.currentOrder.price) {
        this.$notify.warning({title: '提示', message: '余额不足，请充值', showClose: false, duration: 2000});
        return
      }

      // 非余额支付时提示用户
      if (this.payMethod !== 'balance') {
        const payMethodNames = {
          'wechat': '微信支付',
          'alipay': '支付宝支付',
          'bank': '银行卡支付'
        }
        this.$confirm(`即将跳转至${payMethodNames[this.payMethod]}，请确认订单信息`, '支付提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.processPayment()
        }).catch(() => {})
      } else {
        this.processPayment()
      }
    },

    /**
     * 处理支付
     */
    processPayment() {
      console.log('=== 开始处理支付 ===')
      console.log('当前选择的支付方式:', this.payMethod)
      console.log('当前订单:', this.currentOrder)

      // 如果是支付宝支付，调用后端接口生成支付页面
      if (this.payMethod === 'alipay') {
        console.log('进入支付宝支付分支')

        // 确保 currentOrder 和 id 存在
        if (!this.currentOrder || !this.currentOrder.id) {
          console.error('订单信息不完整')
          this.$notify.error({title: '错误', message: '订单信息不完整', showClose: false, duration: 2000})
          return
        }

        console.log('准备发送请求，订单 ID:', this.currentOrder.id)

        this.$request.post('/orders/alipay/web', { id: this.currentOrder.id }, { responseType: 'text' }).then(res => {
          console.log('支付宝接口响应:', res)
          console.log('响应 code:', res.code)
          console.log('响应 data:', res.data)

          if (res.code === '200') {
            console.log('支付页面 HTML 长度:', res.data?.length)
            if (res.data) {
              console.log('HTML 内容预览:', res.data.substring(0, 200))

              // 打开新窗口显示支付宝支付页面
              const win = window.open('', '_blank')
              console.log('新窗口对象:', win)

              if (win) {
                // 先写入空白页面，避免跨域问题
                win.document.write('<html><head><title>支付宝支付</title></head><body></body></html>')
                win.document.close()

                // 然后写入表单
                win.document.write(res.data)
                console.log('已写入 HTML 到新窗口')
                this.payDialogVisible = false
              } else {
                console.error('窗口打开失败或被浏览器拦截')
                this.$notify.error({title: '错误', message: '无法打开支付窗口，请检查浏览器是否拦截弹窗', showClose: false, duration: 3000})
              }
            } else {
              console.error('响应数据为空')
              this.$notify.error({title: '错误', message: '支付页面生成失败', showClose: false, duration: 2000})
            }
          } else {
            console.error('支付宝接口返回错误:', res.msg)
            this.$notify.error({title: '失败', message: res.msg || '支付失败', showClose: false, duration: 2000})
          }
        }).catch(err => {
          console.error('支付宝接口调用失败:', err)
          console.error('完整错误信息:', err.response || err)
          let errorMsg = '支付宝支付失败'
          if (err.response && err.response.data && err.response.data.msg) {
            errorMsg = err.response.data.msg
          }
          this.$notify.error({title: '错误', message: errorMsg, showClose: false, duration: 3000})
        })
      } else {
        console.log('进入其他支付方式分支:', this.payMethod)
        // 其他支付方式（余额、微信、银行卡）
        this.$request.post('/orders/pay', {
          id: this.currentOrder.id,
          payMethod: this.payMethod
        }).then(res => {
          if (res.code === '200') {
            this.$notify.success({title: '成功', message: '支付成功', showClose: false, duration: 2000});
            this.payDialogVisible = false
            this.load(1)

            // 如果是非余额支付，模拟跳转第三方支付
            if (this.payMethod !== 'balance') {
              this.$message.success('正在跳转至支付平台...')
            }
          } else {
            this.$notify.error({title: '失败', message: res.msg, showClose: false, duration: 2000});
          }
        }).catch(err => {
          this.$notify.error({title: '错误', message: '支付失败', showClose: false, duration: 2000});
        })
      }
    },
    confirmArrival(row) {
      this.$confirm('确认商品已到货吗？', '确认到货', {type: "warning"}).then(() => {
        this.$request.post('/orders/arrival', {id: row.id}).then(res => {
          if (res.code === '200') {
            this.$notify.success({title: '成功', message: '确认到货成功', showClose: false, duration: 2000});
            this.load()
          } else {
            this.$notify.error({title: '错误', message: res.msg, showClose: false, duration: 2000});
          }
        })
      }).catch(() => {})
    },
    openReviewDialog(row) {
      this.reviewForm.orderId = row.id
      this.reviewForm.goodsId = row.goodsId
      this.reviewForm.score = 5
      this.reviewForm.content = ''
      this.reviewDialogVisible = true
    },
    submitReview() {
      if (!this.reviewForm.content) {
        this.$notify.warning({title: '提示', message: '请填写评价内容', showClose: false, duration: 2000});
        return
      }
      const reviewData = {
        score: this.reviewForm.score,
        content: this.reviewForm.content,
        orderId: this.reviewForm.orderId,
        productId: this.reviewForm.goodsId
      }
      this.$request.post('/product/addReview', reviewData).then(res => {
        if (res.code === '200') {
          this.$notify.success({title: '成功', message: '评价成功', showClose: false, duration: 2000});
          this.reviewDialogVisible = false
          this.load()
        } else {
          this.$notify.error({title: '失败', message: res.msg, showClose: false, duration: 2000});
        }
      })
    },
    goToAfterSales(row) {
      this.$router.push({
        path: '/front/afterSales',
        query: { orderId: row.id, orderNo: row.orderNo, goodsName: row.name }
      })
    },
    getStateType(state) {
      const map = {
        '待付款': 'warning',
        '未发货': 'info',
        '已发货': 'primary',
        '已到货': 'success',
        '已完成': 'success'
      }
      return map[state] || 'info'
    }
  }
}
</script>

<style scoped>
.orders-container {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: calc(100vh - 120px);
}
</style>