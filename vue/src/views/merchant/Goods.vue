<template>
  <div>
    <el-card>
      <div style="margin-bottom: 10px">
        <el-input style="width: 200px; margin: 0 5px" placeholder="查询商品名称" v-model="name"></el-input>
        <el-button type="success" plain @click="load(1)">查询</el-button>
        <el-button type="primary" plain @click="handleAdd">新增</el-button>
      </div>

      <el-table :data="tableData" stripe>
        <el-table-column prop="name" label="商品名称" :show-overflow-tooltip="true"></el-table-column>
        <el-table-column prop="cover" label="封面">
          <template v-slot="scope">
            <el-image style="width: 60px; height: 60px; border-radius: 4px" :src="scope.row.cover" fit="cover"></el-image>
          </template>
        </el-table-column>
        <el-table-column prop="price" label="价格"></el-table-column>
        <el-table-column prop="store" label="库存"></el-table-column>
        <el-table-column prop="sales" label="销量"></el-table-column>
        <el-table-column prop="state" label="状态"></el-table-column>
        <el-table-column label="操作" width="180">
          <template v-slot="scope">
            <el-button size="mini" type="primary" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="mini" type="danger" @click="del(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div style="padding: 10px 0">
        <el-pagination
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            :current-page="pageNum"
            :page-sizes="[5, 10, 20]"
            :page-size="pageSize"
            layout="total, sizes, prev, pager, next"
            :total="total">
        </el-pagination>
      </div>
    </el-card>

    <el-dialog title="商品信息" :visible.sync="formDialogVisible" width="50%">
      <el-form :model="form" label-width="100px" :rules="rules" ref="formRef">
        <el-form-item label="商品名称" prop="name">
          <el-input v-model="form.name"></el-input>
        </el-form-item>
        <el-form-item label="商品描述" prop="descr">
          <el-input v-model="form.descr" type="textarea"></el-input>
        </el-form-item>
        <el-form-item label="商品价格" prop="price">
          <el-input-number v-model="form.price" :min="0" :precision="2"></el-input-number>
        </el-form-item>
        <el-form-item label="库存" prop="store">
          <el-input-number v-model="form.store" :min="0"></el-input-number>
        </el-form-item>
        <el-form-item label="商品分类" prop="typeId">
          <el-select v-model="form.typeId" placeholder="请选择分类" style="width: 100%">
            <el-option v-for="item in typeList" :key="item.id" :label="item.name" :value="item.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="商品封面">
          <el-upload
              class="upload-demo"
              action="http://localhost:9090/file/upload"
              :on-success="handleUploadSuccess"
              :show-file-list="false">
            <el-button size="small" type="primary">点击上传</el-button>
          </el-upload>
          <el-image v-if="form.cover" style="width: 100px; height: 100px; margin-top: 10px" :src="form.cover" fit="cover"></el-image>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="formDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="save">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'MerchantGoods',
  data() {
    return {
      tableData: [],
      pageNum: 1,
      pageSize: 10,
      total: 0,
      name: '',
      formDialogVisible: false,
      form: {},
      typeList: [],
      rules: {
        name: [{ required: true, message: '请输入商品名称', trigger: 'blur' }],
        price: [{ required: true, message: '请输入价格', trigger: 'blur' }],
        typeId: [{ required: true, message: '请选择分类', trigger: 'change' }]
      }
    }
  },
  created() {
    this.load(1)
    this.loadTypes()
  },
  methods: {
    load(pageNum) {
      if (pageNum) this.pageNum = pageNum
      const user = JSON.parse(localStorage.getItem('user') || '{}')
      this.$request.get('/goods/selectPage', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.name,
          shopId: user.id
        }
      }).then(res => {
        if (res.code === '200') {
          this.tableData = res.data.records
          this.total = res.data.total
        }
      })
    },
    loadTypes() {
      this.$request.get('/type/selectAll').then(res => {
        if (res.code === '200') {
          this.typeList = res.data
        }
      })
    },
    handleAdd() {
      this.form = {}
      this.formDialogVisible = true
    },
    handleEdit(row) {
      this.form = JSON.parse(JSON.stringify(row))
      this.formDialogVisible = true
    },
    handleUploadSuccess(res) {
      if (res.code === '200') {
        this.form.cover = res.data
        this.$message.success('上传成功')
      }
    },
    save() {
      this.$refs.formRef.validate(valid => {
        if (valid) {
          const url = this.form.id ? '/goods/update' : '/goods/add'
          this.$request.post(url, this.form).then(res => {
            if (res.code === '200') {
              this.$message.success('操作成功')
              this.formDialogVisible = false
              this.load(this.pageNum)
            } else {
              this.$message.error(res.msg)
            }
          })
        }
      })
    },
    del(id) {
      this.$confirm('确定删除吗？', '提示', { type: 'warning' }).then(() => {
        this.$request.delete('/goods/delete/' + id).then(res => {
          if (res.code === '200') {
            this.$message.success('删除成功')
            this.load(this.pageNum)
          }
        })
      })
    },
    handleSizeChange(val) {
      this.pageSize = val
      this.load(1)
    },
    handleCurrentChange(val) {
      this.pageNum = val
      this.load()
    }
  }
}
</script>