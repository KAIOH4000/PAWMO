<template>
  <div>
    <el-card>
      <div style="margin-bottom: 10px">
        <el-input style="width: 200px; margin: 0 5px" placeholder="查询商品名称" v-model="name"></el-input>
        <el-input style="width: 200px; margin: 0 5px" placeholder="查询订单号" v-model="orderNo"></el-input>
        <el-select v-model="state" placeholder="订单状态" style="width: 150px; margin: 0 5px" clearable>
          <el-option label="未发货" value="未发货"></el-option>
          <el-option label="已发货" value="已发货"></el-option>
          <el-option label="已到货" value="已到货"></el-option>
        </el-select>
        <el-button type="success" plain @click="load(1)">查询</el-button>
      </div>

      <el-table :data="tableData" stripe>
        <el-table-column prop="orderNo" label="订单号"></el-table-column>
        <el-table-column prop="name" label="商品名称"></el-table-column>
        <el-table-column prop="price" label="金额"></el-table-column>
        <el-table-column prop="nums" label="数量"></el-table-column>
        <el-table-column prop="userName" label="用户"></el-table-column>
        <el-table-column prop="userPhone" label="电话"></el-table-column>
        <el-table-column prop="userAddress" label="地址" :show-overflow-tooltip="true"></el-table-column>
        <el-table-column prop="state" label="状态">
          <template v-slot="scope">
            <el-tag :type="getStateType(scope.row.state)">{{ scope.row.state }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="下单时间"></el-table-column>
        <el-table-column label="操作" width="150">
          <template v-slot="scope">
            <el-button v-if="scope.row.state === '未发货'" size="mini" type="primary" @click="ship(scope.row)">发货</el-button>
            <el-button size="mini" @click="viewDetail(scope.row)">详情</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div style="padding: 10px 0">
        <el-pagination
            @current-change="handleCurrentChange"
            :current-page="pageNum"
            :page-size="pageSize"
            layout="total, prev, pager, next"
            :total="total">
        </el-pagination>
      </div>
    </el-card>

    <el-dialog title="订单详情" :visible.sync="detailVisible" width="50%">
      <el-descriptions :column="2" border v-if="currentOrder">
        <el-descriptions-item label="订单号">{{ currentOrder.orderNo }}</el-descriptions-item>
        <el-descriptions-item label="商品名称">{{ currentOrder.name }}</el-descriptions-item>
        <el-descriptions-item label="金额">¥{{ currentOrder.price }}</el-descriptions-item>
        <el-descriptions-item label="数量">{{ currentOrder.nums }}</el-descriptions-item>
        <el-descriptions-item label="用户">{{ currentOrder.userName }}</el-descriptions-item>
        <el-descriptions-item label="电话">{{ currentOrder.userPhone }}</el-descriptions-item>
        <el-descriptions-item label="地址" :span="2">{{ currentOrder.userAddress }}</el-descriptions-item>
        <el-descriptions-item label="状态">{{ currentOrder.state }}</el-descriptions-item>
        <el-descriptions-item label="下单时间">{{ currentOrder.createTime }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'MerchantOrders',
  data() {
    return {
      tableData: [],
      pageNum: 1,
      pageSize: 10,
      total: 0,
      name: '',
      orderNo: '',
      state: '',
      detailVisible: false,
      currentOrder: null
    }
  },
  created() {
    this.load(1)
  },
  methods: {
    load(pageNum) {
      if (pageNum) this.pageNum = pageNum
      const user = JSON.parse(localStorage.getItem('user') || '{}')
      this.$request.get('/orders/selectPage', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.name,
          orderNo: this.orderNo,
          state: this.state,
          shopId: user.id
        }
      }).then(res => {
        if (res.code === '200') {
          this.tableData = res.data.records
          this.total = res.data.total
        }
      })
    },
    ship(order) {
      this.$confirm('确认发货吗？', '提示', { type: 'warning' }).then(() => {
        this.$request.put('/orders/update', { id: order.id, state: '已发货' }).then(res => {
          if (res.code === '200') {
            this.$message.success('发货成功')
            this.load(this.pageNum)
          }
        })
      })
    },
    viewDetail(order) {
      this.currentOrder = order
      this.detailVisible = true
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
    },
    handleCurrentChange(val) {
      this.pageNum = val
      this.load()
    }
  }
}
</script>