<template>
  <div class="merchant-home">
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-icon" style="background: linear-gradient(135deg, #667eea 0%, #764ba2 100%)">
            <i class="el-icon-goods"></i>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ stats.goodsCount }}</div>
            <div class="stat-label">商品总数</div>
          </div>
        </el-card>
      </el-col>

      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-icon" style="background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%)">
            <i class="el-icon-s-order"></i>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ stats.orderCount }}</div>
            <div class="stat-label">订单总数</div>
          </div>
        </el-card>
      </el-col>

      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-icon" style="background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%)">
            <i class="el-icon-service"></i>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ stats.afterSalesCount }}</div>
            <div class="stat-label">售后申请</div>
          </div>
        </el-card>
      </el-col>

      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-icon" style="background: linear-gradient(135deg, #fa709a 0%, #fee140 100%)">
            <i class="el-icon-wallet"></i>
          </div>
          <div class="stat-content">
            <div class="stat-value">¥{{ stats.totalSales }}</div>
            <div class="stat-label">销售总额</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-card style="margin-top: 20px">
      <div slot="header">
        <span>店铺信息</span>
      </div>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="店铺名称">{{ shopInfo.shopName }}</el-descriptions-item>
        <el-descriptions-item label="联系人">{{ shopInfo.returnReceiver }}</el-descriptions-item>
        <el-descriptions-item label="联系电话">{{ shopInfo.returnPhone }}</el-descriptions-item>
        <el-descriptions-item label="退货地址" :span="2">{{ shopInfo.returnAddress }}</el-descriptions-item>
        <el-descriptions-item label="店铺介绍" :span="2">{{ shopInfo.shopDesc }}</el-descriptions-item>
      </el-descriptions>
    </el-card>

    <el-card style="margin-top: 20px">
      <div slot="header">
        <span>最近订单</span>
      </div>
      <el-table :data="recentOrders" stripe>
        <el-table-column prop="orderNo" label="订单号"></el-table-column>
        <el-table-column prop="name" label="商品名称"></el-table-column>
        <el-table-column prop="price" label="金额"></el-table-column>
        <el-table-column prop="state" label="状态">
          <template v-slot="scope">
            <el-tag :type="getStateType(scope.row.state)">{{ scope.row.state }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="下单时间"></el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script>
export default {
  name: 'MerchantHome',
  data() {
    return {
      stats: {
        goodsCount: 0,
        orderCount: 0,
        afterSalesCount: 0,
        totalSales: 0
      },
      shopInfo: {},
      recentOrders: []
    }
  },
  created() {
    this.loadStats()
    this.loadShopInfo()
    this.loadRecentOrders()
  },
  methods: {
    loadStats() {
      const user = JSON.parse(localStorage.getItem('user') || '{}')

      this.$request.get('/goods/selectAll').then(res => {
        if (res.code === '200') {
          this.stats.goodsCount = res.data.filter(g => g.shopId === user.id).length
        }
      })

      this.$request.get('/orders/selectAll').then(res => {
        if (res.code === '200') {
          const shopOrders = res.data.filter(o => o.shopId === user.id)
          this.stats.orderCount = shopOrders.length
          this.stats.totalSales = shopOrders.reduce((sum, o) => sum + (o.price || 0), 0).toFixed(2)
        }
      })

      this.$request.get('/afterSales/selectAll').then(res => {
        if (res.code === '200') {
          this.stats.afterSalesCount = res.data.filter(a => a.shopId === user.id).length
        }
      })
    },
    loadShopInfo() {
      const user = JSON.parse(localStorage.getItem('user') || '{}')
      this.shopInfo = {
        shopName: user.shopName,
        returnReceiver: user.returnReceiver,
        returnPhone: user.returnPhone,
        returnAddress: user.returnAddress,
        shopDesc: user.shopDesc
      }
    },
    loadRecentOrders() {
      const user = JSON.parse(localStorage.getItem('user') || '{}')
      this.$request.get('/orders/selectAll').then(res => {
        if (res.code === '200') {
          this.recentOrders = res.data
              .filter(o => o.shopId === user.id)
              .slice(0, 5)
        }
      })
    },
    getStateType(state) {
      const map = {
        '待付款': 'info',
        '未发货': 'warning',
        '已发货': 'primary',
        '已到货': 'success',
        '已完成': 'success'
      }
      return map[state] || ''
    }
  }
}
</script>

<style scoped>
.merchant-home {
  padding: 20px;
}

.stat-card {
  display: flex;
  align-items: center;
  padding: 10px;
  cursor: pointer;
  transition: all 0.3s;
}

.stat-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.1);
}

.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 15px;
}

.stat-icon i {
  font-size: 28px;
  color: #fff;
}

.stat-content {
  flex: 1;
}

.stat-value {
  font-size: 24px;
  font-weight: bold;
  color: #333;
  margin-bottom: 5px;
}

.stat-label {
  font-size: 14px;
  color: #999;
}
</style>