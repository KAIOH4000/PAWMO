<template>
  <div>
    <el-card>
      <div style="margin-bottom: 10px">
        <span style="font-size: 16px; font-weight: bold; color: #606266;">首页分类宣传图管理</span>
        <span style="font-size: 12px; color: #909399; margin-left: 10px;">上传对应分类的宣传图片，将在首页展示</span>
      </div>
      <el-table :data="tableData" stripe>
        <el-table-column prop="id" label="序号" width="70" align="center">
          <template slot-scope='scope'>
            <span>{{ scope.$index + 1 }}</span>
          </template>
        </el-table-column>
        <el-table-column align="center" prop="categoryName" label="分类名称" width="120"></el-table-column>
        <el-table-column label="宣传图" align="center">
          <template slot-scope="scope">
            <div v-if="scope.row.imageUrl" style="display: inline-block;">
              <el-image 
                style="width: 200px; height: 150px; border-radius: 8px;"
                :src="scope.row.imageUrl" 
                :preview-src-list="[scope.row.imageUrl]"
                fit="cover">
                <div slot="error" class="image-slot">
                  <i class="el-icon-picture-outline"></i>
                </div>
              </el-image>
            </div>
            <div v-else class="no-image">
              <i class="el-icon-picture-outline"></i>
              <span>暂无图片</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" align="center">
          <template slot-scope="scope">
            <el-button plain type="primary" @click="handleEdit(scope.row)" size="mini">更换图片</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog title="更换宣传图" :visible.sync="dialogFormVisible" width="500px" :close-on-click-modal="false">
      <el-form label-width="100px" :model="form">
        <el-form-item label="分类名称">
          <el-input v-model="form.categoryName" disabled></el-input>
        </el-form-item>
        <el-form-item label="宣传图">
          <div v-if="form.imageUrl" style="margin-bottom: 10px;">
            <el-image 
              style="width: 200px; height: 150px; border-radius: 8px;"
              :src="form.imageUrl" 
              fit="cover">
            </el-image>
          </div>
          <el-upload
            class="upload-demo"
            action="http://localhost:9999/file/upload"
            :headers="{ token: user.token }"
            :on-success="handleUploadSuccess"
            :show-file-list="false"
            accept="image/*">
            <el-button size="small" type="primary">点击上传</el-button>
            <div slot="tip" class="el-upload__tip">建议上传4:3比例的竖版图片，推荐尺寸400x300或更大</div>
          </el-upload>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "PromotionImage",
  data() {
    return {
      tableData: [],
      form: {},
      dialogFormVisible: false,
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
    }
  },
  created() {
    this.load()
  },
  methods: {
    load() {
      this.$request.get("/promotion/selectAll").then(res => {
        this.tableData = res.data || []
      })
    },
    handleEdit(row) {
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogFormVisible = true
    },
    handleUploadSuccess(res) {
      if (res.code === '200') {
        this.form.imageUrl = res.data
        this.$notify.success({title: '成功', message: '图片上传成功', showClose: false, duration: 2000});
      } else {
        this.$notify.error({title: '失败', message: '图片上传失败', showClose: false, duration: 2000});
      }
    },
    save() {
      this.$request.put("/promotion/update", this.form).then(res => {
        if (res.code === '200') {
          this.$notify.success({title: '成功', message: '更新成功', showClose: false, duration: 2000});
          this.dialogFormVisible = false
          this.load()
        } else {
          this.$notify.error({title: '失败', message: res.msg, showClose: false, duration: 2000});
        }
      })
    },
  },
}
</script>

<style scoped>
.no-image {
  width: 200px;
  height: 150px;
  background: #f5f7fa;
  border-radius: 8px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  color: #909399;
}

.no-image i {
  font-size: 40px;
  margin-bottom: 8px;
}

.no-image span {
  font-size: 12px;
}

.image-slot {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
  background: #f5f7fa;
  color: #909399;
}

.image-slot i {
  font-size: 40px;
}

.upload-demo {
  width: 100%;
}

.upload-demo >>> .el-upload-list {
  max-height: 100px;
  overflow-y: auto;
}
</style>
