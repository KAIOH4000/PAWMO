<template>
  <div class="after-sales-container">
    <el-card>
      <div slot="header" style="display: flex; justify-content: space-between; align-items: center;">
        <span style="font-weight: bold; font-size: 16px;">售后服务</span>
      </div>

      <!-- 售后申请表单 -->
        <el-form :model="afterSalesForm" label-width="120px" style="max-width: 800px; margin: 0 auto;">
          <el-form-item label="选择订单">
            <el-select v-model="afterSalesForm.orderId" placeholder="请选择已到货的订单" style="width: 100%;" @change="handleOrderSelect">
              <el-option
                v-for="order in availableOrders"
                :key="order.id"
                :label="order.name + ' (¥' + order.price + ')'"
                :value="order.id">
              </el-option>
            </el-select>
          </el-form-item>
          
          <el-form-item label="商品信息" v-if="selectedOrderInfo">
            <div style="padding: 15px; background-color: #f5f7fa; border-radius: 8px;">
              <div style="font-size: 14px; color: #666;">
                <div><strong>商品名称：</strong>{{ selectedOrderInfo.name }}</div>
                <div style="margin-top: 8px;"><strong>订单金额：</strong><span style="color: #ff6700; font-weight: bold;">¥{{ selectedOrderInfo.price }}</span></div>
                <div style="margin-top: 8px;"><strong>订单状态：</strong>{{ selectedOrderInfo.state }}</div>
              </div>
            </div>
          </el-form-item>
          
          <el-form-item label="售后方式">
            <el-radio-group v-model="afterSalesForm.type">
              <el-radio label="仅退款">仅退款</el-radio>
              <el-radio label="退货退款">退货退款</el-radio>
            </el-radio-group>
          </el-form-item>
          
          <el-form-item label="退款金额">
            <el-input-number v-model="afterSalesForm.refundAmount" :min="0" :max="selectedOrderInfo ? selectedOrderInfo.price : 0" :precision="2" step-strictly style="width: 100%;"></el-input-number>
            <div style="font-size: 12px; color: #999; margin-top: 5px;">最大退款金额：¥{{ selectedOrderInfo ? selectedOrderInfo.price : 0 }}</div>
          </el-form-item>
          
          <el-form-item label="申请原因">
            <el-input type="textarea" v-model="afterSalesForm.reason" placeholder="请描述您的退款原因" rows="4"></el-input>
          </el-form-item>
          
          <el-form-item>
            <el-button type="primary" @click="submitAfterSales" style="width: 200px;">提交申请</el-button>
          </el-form-item>
        </el-form>

      <!-- 售后记录列表 -->
      <el-divider></el-divider>
      <div style="margin-top: 30px;">
        <h3 style="margin-bottom: 20px;">我的售后记录</h3>
        <el-table :data="afterSalesData" border stripe>
          <el-table-column prop="orderNo" label="订单编号" width="180">
            <template slot-scope="scope">
              <span>{{ scope.row.order?.orderNo || scope.row.order?.order_no || '暂无' }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="goodsName" label="商品名称" show-overflow-tooltip>
            <template slot-scope="scope">
              <span>{{ scope.row.order?.goods?.name || scope.row.order?.name || '暂无' }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="type" label="售后方式" width="100">
            <template slot-scope="scope">
              <el-tag :type="scope.row.type === '仅退款' ? 'success' : 'warning'">{{ scope.row.type }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="refundAmount" label="退款金额" width="100">
            <template slot-scope="scope">
              <span style="color: #ff6700; font-weight: bold;">¥{{ scope.row.refundAmount }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="reason" label="申请原因" show-overflow-tooltip></el-table-column>
          <el-table-column prop="status" label="状态" width="100">
            <template slot-scope="scope">
              <el-tag :type="getStateTag(scope.row.status)">{{ scope.row.status || '待审核' }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="applyTime" label="申请时间" width="180">
            <template slot-scope="scope">
              <span>{{ scope.row.applyTime || '暂无' }}</span>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="250" fixed="right">
            <template slot-scope="scope">
              <!-- 用户操作按钮 -->
              <el-button v-if="scope.row.status === '待审核'" type="danger" size="mini" @click="cancelAfterSales(scope.row.id)">取消</el-button>
              <el-button v-if="scope.row.status === '审核通过' && scope.row.type === '退货退款'" type="success" size="mini" @click="userShipGoods(scope.row.id)">我已发货</el-button>
              <el-button v-if="scope.row.status === '已退货'" type="info" size="mini" disabled>已退货，等待商家退款</el-button>

              <!-- 通用状态显示 -->
              <el-button v-if="scope.row.status === '已发货'" type="info" size="mini" disabled>等待商家收货</el-button>
              <el-button v-if="scope.row.status === '已到货'" type="success" size="mini" disabled>退款到账中</el-button>
              <el-button v-if="!scope.row.isReviewed && scope.row.status === '已退款'" type="primary" size="mini" @click="openReviewDialog(scope.row)">评价</el-button>
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
      </div>
    </el-card>

    <!-- 发货对话框 -->
    <el-dialog title="填写物流信息" :visible.sync="shipDialogVisible" width="500px">
      <el-form :model="shipForm" label-width="80px">
        <el-form-item label="物流公司">
          <el-input v-model="shipForm.logisticsCompany" placeholder="请输入物流公司名称"></el-input>
        </el-form-item>
        <el-form-item label="物流单号">
          <el-input v-model="shipForm.logisticsNo" placeholder="请输入物流单号"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="shipDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmShip">确认发货</el-button>
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
  name: "FrontAfterSales",
  data() {
    return {
      afterSalesForm: {
        orderId: '',
        type: '',
        reason: '',
        refundAmount: 0
      },
      availableOrders: [], // 可用于申请售后的订单列表
      selectedOrderInfo: null, // 当前选中的订单信息
      afterSalesData: [],
      myOrdersData: [],
      activeTab: 'afterSales',
      pageNum: 1,
      pageSize: 10,
      orderPageNum: 1,
      orderPageSize: 10,
      total: 0,
      orderTotal: 0,
      user: JSON.parse(localStorage.getItem('user') || '{}'),
      shipDialogVisible: false,
      shipForm: {
        afterSalesId: null,
        logisticsCompany: '',
        logisticsNo: ''
      },
      reviewDialogVisible: false,
      currentOrder: {},
      reviewForm: {
        score: 0,
        content: '',
        orderId: '',
        goodsId: ''
      }
    }
  },
  created() {
    this.initOrderInfo()
    this.loadAfterSalesData()
    this.loadAvailableOrders() // 加载可申请售后的订单
  },
  methods: {
    initOrderInfo() {
      // 从 URL 参数获取订单信息
      const query = this.$route.query
      if (query.orderId) {
        this.afterSalesForm.orderId = query.orderId
        this.afterSalesForm.orderNo = query.orderNo
        this.afterSalesForm.goodsName = query.goodsName
        // 如果有 orderId，需要重新加载可申请订单列表，确保下拉框中有这个订单
        this.loadAvailableOrders()
      }
    },
    loadAfterSalesData(pageNum) {
      if (pageNum) this.pageNum = pageNum
      this.$request.get('/afterSales/selectPage', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          userId: this.user.id
        }
      }).then(res => {
        this.afterSalesData = res.data?.records || []
        this.total = res.data?.total
        
        // 检查当前用户是否已评价该订单
        this.afterSalesData.forEach(order => {
          if (order && order.orderId) { // 添加空值判断
            this.$request.get('/product/selectByUserAndOrder', {
              params: {
                userId: this.user.id,
                orderId: order.orderId
              }
            }).then(res => {
              if (res.data && res.data.isReviewed !== undefined) { // 添加空值判断
                const isReviewed = res.data.isReviewed
                this.$set(order, 'isReviewed', isReviewed)
              }
            }).catch(() => {
              // 查询失败时设置默认值为 false
              this.$set(order, 'isReviewed', false)
            })
          }
        })
      }).catch(() => {
        // 加载失败时不弹出错误提示
      })
    },
    handleUploadSuccess(res) {
      // 已移除图片上传功能
    },
    handleRemove(file, fileList) {
      // 已移除图片上传功能
    },
    loadAvailableOrders() {
      // 加载所有已支付且未申请售后的订单（排除待付款状态）
      this.$request.get('/orders/selectPage', {
        params: {
          pageNum: 1,
          pageSize: 100,
          userId: this.user.id
          // 前端过滤掉待付款订单
        }
      }).then(res => {
        if (res.code === '200') {
          // 过滤掉待付款的订单
          const allOrders = res.data?.records || []
          this.availableOrders = allOrders.filter(order => order.state !== '待付款')
        }
      }).catch(err => {
        console.error('加载可申请订单失败:', err)
      })
    },
    handleOrderSelect(orderId) {
      const order = this.availableOrders.find(o => o.id === orderId)
      this.selectedOrderInfo = order
      // 自动填充退款金额为商品价格
      if (order) {
        this.afterSalesForm.refundAmount = order.price
      }
    },
    submitAfterSales() {
      if (!this.afterSalesForm.orderId) {
        this.$notify.warning({title: '提示', message: '请选择订单', showClose: false, duration: 2000});
        return
      }
      if (!this.afterSalesForm.type) {
        this.$notify.warning({title: '提示', message: '请选择售后方式', showClose: false, duration: 2000});
        return
      }
      if (!this.afterSalesForm.reason) {
        this.$notify.warning({title: '提示', message: '请填写申请原因', showClose: false, duration: 2000});
        return
      }
      if (!this.afterSalesForm.refundAmount || this.afterSalesForm.refundAmount <= 0) {
        this.$notify.warning({title: '提示', message: '退款金额必须大于 0', showClose: false, duration: 2000});
        return
      }
      if (this.selectedOrderInfo && this.afterSalesForm.refundAmount > this.selectedOrderInfo.price) {
        this.$notify.warning({title: '提示', message: '退款金额不能超过商品价格', showClose: false, duration: 2000});
        return
      }
      
      const submitData = {
        orderId: this.afterSalesForm.orderId,
        type: this.afterSalesForm.type,
        reason: this.afterSalesForm.reason,
        refundAmount: this.afterSalesForm.refundAmount,
        userId: this.user.id
      }
      
      this.$request.post('/afterSales/add', submitData).then(res => {
        if (res.code === '200') {
          this.$notify.success({title: '成功', message: '申请提交成功', showClose: false, duration: 2000});
          this.resetForm()
          this.loadAvailableOrders()
          this.loadAfterSalesData()  // 刷新售后记录列表
        } else {
          this.$notify.error({title: '失败', message: res.msg, showClose: false, duration: 2000});
        }
      })
    },
    resetForm() {
      this.afterSalesForm = {
        orderId: '',
        type: '',
        reason: '',
        refundAmount: 0
      }
      this.selectedOrderInfo = null
    },
    cancelAfterSales(id) {
      this.$confirm('您确认取消该售后申请吗？', '确认取消', {type: "warning"}).then(() => {
        this.$request.put('/afterSales/update', {id, state: '已取消'}).then(res => {
          if (res.code === '200') {
            this.$notify.success({title: '成功', message: '取消成功', showClose: false, duration: 2000});
            this.loadAfterSalesData(this.pageNum)
          } else {
            this.$notify.error({title: '失败', message: res.msg, showClose: false, duration: 2000});
          }
        })
      }).catch(() => {})
    },
    shipGoods(id) {
      this.shipForm.afterSalesId = id
      this.shipForm.logisticsCompany = ''
      this.shipForm.logisticsNo = ''
      this.shipDialogVisible = true
    },
    // 用户点击"我已发货"
    userShipGoods(id) {
      this.$confirm('确认已发货吗？请填写物流信息', '确认发货', {type: "warning"}).then(() => {
        this.shipForm.afterSalesId = id
        this.shipForm.logisticsCompany = ''
        this.shipForm.logisticsNo = ''
        this.shipDialogVisible = true
      }).catch(() => {})
    },
    confirmShip() {
      if (!this.shipForm.logisticsCompany || !this.shipForm.logisticsNo) {
        this.$notify.warning({title: '提示', message: '请填写完整的物流信息', showClose: false, duration: 2000});
        return
      }
      // 判断是用户发货还是管理员发货
      const isUserShip = this.user.role !== 'ADMIN'  // 非管理员就是用户发货
      const newState = isUserShip ? '已退货' : '已发货'  // 用户发货后状态为"已退货",管理员发货为"已发货"
      
      this.$request.put('/afterSales/update', {
        id: this.shipForm.afterSalesId,
        state: newState,
        logisticsCompany: this.shipForm.logisticsCompany,
        logisticsNo: this.shipForm.logisticsNo
      }).then(res => {
        if (res.code === '200') {
          const msg = isUserShip ? '退货发货成功，请等待商家确认收货并退款' : '退货发货成功，请等待商家确认收货'
          this.$notify.success({title: '成功', message: msg, showClose: false, duration: 2000});
          this.shipDialogVisible = false
          this.loadAfterSalesData(this.pageNum)
        } else {
          this.$notify.error({title: '失败', message: res.msg, showClose: false, duration: 2000});
        }
      })
    },
    handleSizeChange(pageSize) {
      this.pageSize = pageSize
      this.loadAfterSalesData(1)
    },
    handleCurrentChange(pageNum) {
      this.pageNum = pageNum
      this.loadAfterSalesData(pageNum)
    },
    getStateTag(state) {
      const map = {
        '待审核': 'warning',
        '审核通过': 'success',
        '已通过': 'primary',
        '已退货': 'primary',
        '已发货': 'info',
        '已到货': 'success',
        '已退款': 'success',
        '已拒绝': 'danger',
        '已取消': 'info'
      }
      return map[state] || 'info'
    },
    openReviewDialog(row) {
      this.currentOrder = row
      this.reviewForm = {
        score: 0,
        content: '',
        orderId: row.id || row.orderId, // 兼容两种数据结构
        goodsId: row.goodsId
      }
      this.reviewDialogVisible = true
    },
    submitReview() {
      if (!this.reviewForm.score) {
        this.$notify.warning({title: '提示', message: '请先评分', showClose: false, duration: 2000});
        return
      }
      // 确保传递正确的 productId 和 orderId
      const reviewData = {
        score: this.reviewForm.score,
        content: this.reviewForm.content,
        orderId: this.reviewForm.orderId,
        productId: this.reviewForm.goodsId // 使用 goodsId 作为 productId
      }
      this.$request.post('/product/addReview', reviewData).then(res => {
        if (res.code === '200') {
          this.$notify.success({title: '成功', message: '评价成功', showClose: false, duration: 2000});
          this.reviewDialogVisible = false
          this.loadAfterSalesData()
        } else {
          this.$notify.error({title: '失败', message: res.msg, showClose: false, duration: 2000});
        }
      })
    }
  }
}
</script>

<style scoped>
.after-sales-container {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: calc(100vh - 120px);
}
</style>
