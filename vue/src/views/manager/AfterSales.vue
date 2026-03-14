<template>
  <div>
    <el-card>
      <div style="margin-bottom: 10px">
        <el-select style="width: 150px; margin-right: 5px" v-model="type" placeholder="售后类型" clearable>
          <el-option label="仅退款" value="仅退款"></el-option>
          <el-option label="退货退款" value="退货退款"></el-option>
        </el-select>
        <el-select style="width: 150px; margin-right: 5px" v-model="status" placeholder="处理状态" clearable>
          <el-option label="待审核" value="待审核"></el-option>
          <el-option label="审核通过" value="审核通过"></el-option>
          <el-option label="审核拒绝" value="审核拒绝"></el-option>
          <el-option label="已退货" value="已退货"></el-option>
          <el-option label="已退款" value="已退款"></el-option>
        </el-select>
        <el-button type="success" plain @click="load(1)">查询</el-button>
        <el-button type="info" plain @click="reset">重置</el-button>
      </div>

      <el-table :data="tableData" stripe>
        <el-table-column prop="id" label="售后单号" width="80"></el-table-column>
        <el-table-column prop="orderId" label="订单ID" width="80"></el-table-column>
        <el-table-column prop="user.name" label="用户" width="100"></el-table-column>
        <el-table-column prop="type" label="售后方式" width="100">
          <template v-slot="scope">
            <el-tag :type="scope.row.type === '退货退款' ? 'danger' : 'warning'">{{ scope.row.type }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="reason" label="申请原因" :show-overflow-tooltip="true"></el-table-column>
        <el-table-column prop="refundAmount" label="退款金额" width="100">
          <template v-slot="scope">
            <span style="color: #ff6700;">¥{{ scope.row.refundAmount }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template v-slot="scope">
            <el-tag :type="getStatusType(scope.row.status)">{{ scope.row.status }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="applyTime" label="申请时间" :show-overflow-tooltip="true"></el-table-column>
        <el-table-column prop="auditTime" label="审核时间" :show-overflow-tooltip="true"></el-table-column>
        <el-table-column label="操作" align="center" width="280">
          <template v-slot="scope">
            <el-button size="mini" type="success" @click="audit(scope.row, '审核通过')" v-if="scope.row.status === '待审核'">通过</el-button>
            <el-button size="mini" type="danger" @click="audit(scope.row, '审核拒绝')" v-if="scope.row.status === '待审核'">拒绝</el-button>
            <!-- 退款按钮：仅退款审核通过后显示，退货退款在用户发货后显示 -->
            <el-button size="mini" type="warning" @click="refund(scope.row)"
              v-if="(scope.row.type && scope.row.type.includes('退款') && scope.row.status === '审核通过') ||
                   (scope.row.type === '退货' && scope.row.status === '已退货')">退款</el-button>
            <el-button size="mini" type="primary" @click="viewDetail(scope.row)">详情</el-button>
            <el-button size="mini" type="danger" plain @click="del(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div style="margin: 10px 0">
        <el-pagination
            background
            @current-change="handleCurrentChange"
            :current-page="pageNum"
            :page-size="pageSize"
            layout="total, prev, pager, next"
            :total="total">
        </el-pagination>
      </div>
    </el-card>

    <!-- 审核对话框 -->
    <el-dialog title="售后审核" :visible.sync="auditDialogVisible" width="500px">
      <div style="margin-bottom: 15px;">
        <span style="font-weight: bold;">售后类型:</span> {{ currentItem.type }}
      </div>
      <div style="margin-bottom: 15px;">
        <span style="font-weight: bold;">申请原因:</span> {{ currentItem.reason }}
      </div>
      <div style="margin-bottom: 15px;">
        <span style="font-weight: bold;">退款金额:</span> <span style="color: #ff6700;">¥{{ currentItem.refundAmount }}</span>
      </div>
      <el-form label-width="80px">
        <el-form-item label="审核结果">
          <el-radio-group v-model="auditForm.status">
            <el-radio label="审核通过">通过</el-radio>
            <el-radio label="审核拒绝">拒绝</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="审核备注">
          <el-input type="textarea" v-model="auditForm.auditRemark" placeholder="请输入审核备注"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="auditDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitAudit">提交</el-button>
      </div>
    </el-dialog>

    <!-- 详情对话框 -->
    <el-dialog title="售后详情" :visible.sync="detailDialogVisible" width="600px">
      <div v-if="currentItem">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="售后单号">{{ currentItem.id }}</el-descriptions-item>
          <el-descriptions-item label="订单ID">{{ currentItem.orderId }}</el-descriptions-item>
          <el-descriptions-item label="用户">{{ currentItem.user ? currentItem.user.name : '' }}</el-descriptions-item>
          <el-descriptions-item label="售后类型">{{ currentItem.type }}</el-descriptions-item>
          <el-descriptions-item label="申请原因" :span="2">{{ currentItem.reason }}</el-descriptions-item>
          <el-descriptions-item label="退款金额">
            <span style="color: #ff6700;">¥{{ currentItem.refundAmount }}</span>
          </el-descriptions-item>
          <el-descriptions-item label="当前状态">
            <el-tag :type="getStatusType(currentItem.status)">{{ currentItem.status }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="申请时间">{{ currentItem.applyTime }}</el-descriptions-item>
          <el-descriptions-item label="审核时间">{{ currentItem.auditTime || '-' }}</el-descriptions-item>
          <el-descriptions-item label="审核备注" :span="2">{{ currentItem.auditRemark || '-' }}</el-descriptions-item>
          <el-descriptions-item label="物流公司">{{ currentItem.logisticsCompany || '-' }}</el-descriptions-item>
          <el-descriptions-item label="物流单号">{{ currentItem.logisticsNo || '-' }}</el-descriptions-item>
        </el-descriptions>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "AfterSales",
  data() {
    return {
      tableData: [],
      pageNum: 1,
      pageSize: 10,
      type: '',
      status: '',
      total: 0,
      auditDialogVisible: false,
      detailDialogVisible: false,
      currentItem: {},
      auditForm: {
        status: '审核通过',
        auditRemark: ''
      }
    }
  },
  created() {
    this.load()
  },
  methods: {
    load(pageNum) {
      if (pageNum) this.pageNum = pageNum
      this.$request.get('/afterSales/selectPage', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          type: this.type,
          status: this.status
        }
      }).then(res => {
        this.tableData = res.data?.records || []
        this.total = res.data?.total
      })
    },
    reset() {
      this.type = ''
      this.status = ''
      this.load()
    },
    getStatusType(status) {
      const map = {
        '待审核': 'warning',
        '审核通过': 'success',
        '审核拒绝': 'danger',
        '已退货': 'primary',
        '已退款': 'success'
      }
      return map[status] || 'info'
    },
    audit(row, defaultStatus) {
      this.currentItem = row
      this.auditForm.status = defaultStatus
      this.auditForm.auditRemark = ''
      this.auditDialogVisible = true
    },
    submitAudit() {
      const data = {
        ...this.currentItem,
        status: this.auditForm.status,
        auditRemark: this.auditForm.auditRemark
      }
      this.$request.post('/afterSales/audit', data).then(res => {
        if (res.code === '200') {
          this.$notify.success({title: '成功', message: '审核完成', showClose: false, duration: 2000})
          this.auditDialogVisible = false
          this.load()
        } else {
          this.$notify.error({title: '错误', message: res.msg, showClose: false, duration: 2000})
        }
      })
    },
    viewDetail(row) {
      this.currentItem = row
      this.detailDialogVisible = true
    },
    del(id) {
      this.$confirm('确认删除该售后申请吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$request.delete('/afterSales/delete?id=' + id).then(res => {
          if (res.code === '200') {
            this.$notify.success({title: '成功', message: '删除成功', showClose: false, duration: 2000})
            this.load()
          } else {
            this.$notify.error({title: '错误', message: res.msg, showClose: false, duration: 2000})
          }
        })
      }).catch(() => {})
    },
    refund(row) {
      this.$confirm('确认退款给该用户吗？退款金额：¥' + row.refundAmount + '，将退还至用户账户余额', '确认退款', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$request.put('/afterSales/refund', {id: row.id}).then(res => {
          if (res.code === '200') {
            this.$notify.success({title: '成功', message: '退款成功', showClose: false, duration: 2000})
            this.load()
          } else {
            this.$notify.error({title: '错误', message: res.msg, showClose: false, duration: 2000})
          }
        })
      }).catch(() => {})
    },
    handleCurrentChange(pageNum) {
      this.pageNum = pageNum
      this.load()
    }
  }
}
</script>

<style scoped>
</style>
