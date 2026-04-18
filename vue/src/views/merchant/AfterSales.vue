<template>
  <div>
    <el-card>
      <div style="margin-bottom: 10px">
        <el-select v-model="status" placeholder="售后状态" style="width: 150px; margin: 0 5px" clearable>
          <el-option label="待审核" value="待审核"></el-option>
          <el-option label="已通过" value="已通过"></el-option>
          <el-option label="已拒绝" value="已拒绝"></el-option>
          <el-option label="已退款" value="已退款"></el-option>
        </el-select>
        <el-button type="success" plain @click="load(1)">查询</el-button>
      </div>

      <el-table :data="tableData" stripe>
        <el-table-column prop="id" label="ID" width="60"></el-table-column>
        <el-table-column prop="order.orderNo" label="订单号"></el-table-column>
        <el-table-column prop="order.name" label="商品名称"></el-table-column>
        <el-table-column prop="type" label="售后类型"></el-table-column>
        <el-table-column prop="reason" label="原因" :show-overflow-tooltip="true"></el-table-column>
        <el-table-column prop="refundAmount" label="退款金额"></el-table-column>
        <el-table-column prop="status" label="状态">
          <template v-slot="scope">
            <el-tag :type="getStatusType(scope.row.status)">{{ scope.row.status }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="returnStatus" label="退货状态">
          <template v-slot="scope">
            <el-tag v-if="scope.row.returnStatus" :type="getReturnStatusType(scope.row.returnStatus)">
              {{ scope.row.returnStatus }}
            </el-tag>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="applyTime" label="申请时间"></el-table-column>
        <el-table-column label="操作" width="280">
          <template v-slot="scope">
            <el-button v-if="scope.row.status === '待审核'" size="mini" type="success" @click="audit(scope.row, '已通过')">通过</el-button>
            <el-button v-if="scope.row.status === '待审核'" size="mini" type="danger" @click="audit(scope.row, '已拒绝')">拒绝</el-button>
            <el-button v-if="scope.row.status === '已通过' && scope.row.type !== '仅退款'" size="mini" type="warning" @click="updateReturnStatus(scope.row, '已签收')">确认收货</el-button>
            <el-button v-if="scope.row.status === '已通过' && scope.row.returnStatus === '已签收'" size="mini" type="primary" @click="refund(scope.row)">退款</el-button>
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

    <el-dialog title="售后详情" :visible.sync="detailVisible" width="60%">
      <el-descriptions :column="2" border v-if="currentRecord">
        <el-descriptions-item label="售后ID">{{ currentRecord.id }}</el-descriptions-item>
        <el-descriptions-item label="订单号">{{ currentRecord.order?.orderNo }}</el-descriptions-item>
        <el-descriptions-item label="商品名称">{{ currentRecord.order?.name }}</el-descriptions-item>
        <el-descriptions-item label="售后类型">{{ currentRecord.type }}</el-descriptions-item>
        <el-descriptions-item label="退款金额">¥{{ currentRecord.refundAmount }}</el-descriptions-item>
        <el-descriptions-item label="状态">{{ currentRecord.status }}</el-descriptions-item>
        <el-descriptions-item label="退货状态">{{ currentRecord.returnStatus || '-' }}</el-descriptions-item>
        <el-descriptions-item label="物流单号">{{ currentRecord.logisticsNo || '-' }}</el-descriptions-item>
        <el-descriptions-item label="物流公司">{{ currentRecord.logisticsCompany || '-' }}</el-descriptions-item>
        <el-descriptions-item label="申请时间">{{ currentRecord.applyTime }}</el-descriptions-item>
        <el-descriptions-item label="审核时间">{{ currentRecord.auditTime || '-' }}</el-descriptions-item>
        <el-descriptions-item label="寄回时间">{{ currentRecord.returnTime || '-' }}</el-descriptions-item>
        <el-descriptions-item label="签收时间">{{ currentRecord.receiveTime || '-' }}</el-descriptions-item>
        <el-descriptions-item label="原因" :span="2">{{ currentRecord.reason }}</el-descriptions-item>
        <el-descriptions-item label="审核备注" :span="2">{{ currentRecord.auditRemark || '-' }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'MerchantAfterSales',
  data() {
    return {
      tableData: [],
      pageNum: 1,
      pageSize: 10,
      total: 0,
      status: '',
      detailVisible: false,
      currentRecord: null
    }
  },
  created() {
    this.load(1)
  },
  methods: {
    load(pageNum) {
      if (pageNum) this.pageNum = pageNum
      const user = JSON.parse(localStorage.getItem('user') || '{}')
      this.$request.get('/afterSales/selectPage', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          status: this.status,
          shopId: user.id
        }
      }).then(res => {
        if (res.code === '200') {
          this.tableData = res.data.records
          this.total = res.data.total
        }
      })
    },
    audit(record, resultStatus) {
      const remark = prompt('请输入审核备注（可选）：')
      this.$request.put('/afterSales/update', {
        id: record.id,
        status: resultStatus,
        auditRemark: remark
      }).then(res => {
        if (res.code === '200') {
          this.$message.success('审核成功')
          this.load(this.pageNum)
        }
      })
    },
    updateReturnStatus(record, returnStatus) {
      this.$confirm('确认已收到退货吗？', '提示', { type: 'warning' }).then(() => {
        this.$request.put(`/afterSales/return-status/${record.id}`, null, {
          params: { returnStatus }
        }).then(res => {
          if (res.code === '200') {
            this.$message.success('操作成功')
            this.load(this.pageNum)
          }
        })
      })
    },
    refund(record) {
      this.$confirm('确认退款吗？退款后将无法撤销', '提示', { type: 'warning' }).then(() => {
        this.$request.post(`/afterSales/refund/${record.id}`).then(res => {
          if (res.code === '200') {
            this.$message.success('退款成功')
            this.load(this.pageNum)
          }
        })
      })
    },
    viewDetail(record) {
      this.currentRecord = record
      this.detailVisible = true
    },
    getStatusType(status) {
      const map = {
        '待审核': 'warning',
        '已通过': 'primary',
        '已拒绝': 'danger',
        '已退款': 'success'
      }
      return map[status] || ''
    },
    getReturnStatusType(status) {
      const map = {
        '待寄回': 'info',
        '已寄回': 'primary',
        '运输中': 'warning',
        '已签收': 'success'
      }
      return map[status] || ''
    },
    handleCurrentChange(val) {
      this.pageNum = val
      this.load()
    }
  }
}
</script>