<template>
  <div style="margin: 10px auto;min-height: 90vh;width: 70%">
    <!--顶部 + 搜索框-->
    <div style="display: flex;justify-content: space-between;align-items: center;margin-bottom: 20px">
      <div>
        <h1 style="border-left: 5px solid #ff6700;padding-left: 7px;font-size: 22px;color:#303133;">我的收藏</h1>
      </div>
      <div v-if="collects.length > 0" style="display: flex;gap: 10px;align-items: center">
        <el-button 
          v-if="!isBatchMode" 
          type="primary" 
          size="small" 
          @click="startBatchMode"
          icon="el-icon-edit">
          批量管理
        </el-button>
        <div v-else style="display: flex;gap: 10px">
          <el-button 
            type="default" 
            size="small" 
            @click="selectAll"
            :disabled="selectList.length === collects.length">
            {{ selectList.length === collects.length ? '已全选' : '全选' }}
          </el-button>
          <el-button 
            type="danger" 
            size="small" 
            @click="batchDelete"
            :disabled="selectList.length === 0"
            icon="el-icon-delete">
            删除 ({{ selectList.length }})
          </el-button>
          <el-button 
            type="warning" 
            size="mini"
            @click="batchAddToCart"
            :disabled="selectList.length === 0"
            icon="el-icon-shopping-cart-2">
            加入购物车 ({{ selectList.length }})
          </el-button>
          <el-button 
            type="info" 
            size="small" 
            @click="cancelBatchMode">
            取消
          </el-button>
        </div>
      </div>
    </div>

    <div>
      <el-row :gutter="20" v-if="collects.length > 0">
        <el-col :span="6" v-for="(item,index) in collects" :key="index" style="margin-top: 10px">
          <el-card :body-style="{ padding: '0px' }" class="card-item" :class="{ 'selected': selectList.includes(item.id) }">
            <div v-if="isBatchMode" style="position: absolute;top: 10px;left: 10px;z-index: 10;">
              <el-checkbox 
                v-model="item.isSelected" 
                @change="toggleSelect(item)"
                style="transform: scale(1.2);cursor: pointer;">
              </el-checkbox>
            </div>
            <img :src="item.goods.cover" alt="" style="width: 100%;height: 200px" @click="goPage('/front/goodsDetail/' + item.goodsId)">
            <div style="padding: 10px" >
              <div style="margin-top: 3px;font-size: 13px" @click="goPage('/front/goodsDetail/' + item.goodsId)">
                {{ item.goods.name }}
              </div>
              <div style="margin-top: 5px;font-size: 11px;color: #909399;white-space: nowrap;overflow: hidden;text-overflow: ellipsis;" @click="goPage('/front/goodsDetail/' + item.goodsId)">
                {{ item.goods.descr }}
              </div>
              <div style="display: flex;justify-content: space-between;align-items: center;margin-top: 10px">
                <div style="font-size: 20px;color: #FFA500;font-weight: 600">
                  {{ item.goods.price }}
                </div>

                <div style="display: flex;gap: 6px">
                  <el-button 
                    v-if="!isBatchMode" 
                    type="warning" 
                    size="mini" 
                    @click="addToCart(item)"
                    icon="el-icon-shopping-cart-2"
                    style="padding: 5px 8px; font-size: 11px; height: auto; line-height: 1.2;">
                    加入购物车
                  </el-button>
                  <el-button 
                    type="text" 
                    style="color: orangered; font-size: 13px;" 
                    @click="del(item)">
                    {{ isBatchMode ? '移除' : '取消收藏' }}
                  </el-button>
                </div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <div v-if="collects.length == 0">
      <el-empty :image-size="200" description="您还未收藏哟~"></el-empty>
    </div>
  </div>
</template>

<script>
export default {
  name: "FrontCollect",
  data(){
    return{
      collects: [],
      isBatchMode: false,
      selectList: [] // 存储选中的收藏 ID
    }
  },
  created() {
    this.loadCollect()
  },
  methods:{
    loadCollect(){
      this.$request.get('/collect/myCollect').then(res => {
        this.collects = res.data
      })
    },
    goPage(url){
      location.href = url
    },
    del(row){
      const action = this.isBatchMode ? '移除' : '取消收藏';
      this.$confirm(`确认${action}该商品吗？`, '提示', {type: "warning"}).then(() => {
        this.$request.delete('/collect/delete?id=' + row.id).then(res => {
          if (res.code == '200'){
            this.$notify.success({title: '成功', message: `${action}成功`, showClose: false, duration: 2000});
            // 如果在批量模式下，从选中列表中移除
            if (this.isBatchMode) {
              this.selectList = this.selectList.filter(id => id !== row.id)
              row.isSelected = false
            }
          } else {
            this.$notify.error({title: '失败', message: res.msg, showClose: false, duration: 2000});
          }
          this.loadCollect()
        })
      }).catch(() => {})
    },
    // 开始批量管理模式
    startBatchMode() {
      this.isBatchMode = true
      this.selectList = []
    },
    // 取消批量管理模式
    cancelBatchMode() {
      this.isBatchMode = false
      this.selectList = []
    },
    // 全选/取消全选
    selectAll() {
      if (this.selectList.length === this.collects.length) {
        // 取消全选
        this.selectList = []
        this.collects.forEach(item => item.isSelected = false)
      } else {
        // 全选
        this.selectList = this.collects.map(item => item.id)
        this.collects.forEach(item => item.isSelected = true)
      }
    },
    // 切换单个商品的选中状态
    toggleSelect(item) {
      const index = this.selectList.indexOf(item.id)
      if (index > -1) {
        // 已选中，现在取消
        this.selectList.splice(index, 1)
        item.isSelected = false
      } else {
        // 未选中，现在选中
        this.selectList.push(item.id)
        item.isSelected = true
      }
    },
    // 批量删除
    batchDelete() {
      if (this.selectList.length === 0) {
        this.$notify.warning({title: '提示', message: '请选择要删除的商品', showClose: false, duration: 2000});
        return
      }
      
      this.$confirm(`确认删除选中的 ${this.selectList.length} 个商品吗？`, '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        // 批量删除
        const deletePromises = this.selectList.map(id => 
          this.$request.delete('/collect/delete?id=' + id)
        )
        
        Promise.all(deletePromises).then(results => {
          const successCount = results.filter(r => r.code === '200').length
          this.$notify.success({
            title: '成功', 
            message: `已成功删除 ${successCount}/${this.selectList.length} 个商品`,
            showClose: false, 
            duration: 2000
          });
          this.cancelBatchMode()
          this.loadCollect()
        }).catch(err => {
          this.$notify.error({title: '错误', message: '批量删除失败', showClose: false, duration: 2000});
        })
      }).catch(() => {})
    },
    // 批量加入购物车
    batchAddToCart() {
      if (this.selectList.length === 0) {
        this.$notify.warning({title: '提示', message: '请选择要加入购物车的商品', showClose: false, duration: 2000});
        return
      }
      
      const user = JSON.parse(localStorage.getItem('user') || '{}')
      if (!user.id) {
        this.$notify.error({title: '提示', message: '请先登录', showClose: false, duration: 2000});
        this.$router.push('/login')
        return
      }
      
      this.$confirm(`将选中的 ${this.selectList.length} 件商品加入购物车？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        // 获取选中的商品
        const selectedItems = this.collects.filter(item => this.selectList.includes(item.id))
        
        // 批量添加到购物车
        const addPromises = selectedItems.map(item => {
          const data = {
            name: item.goods.name,
            goodsId: item.goods.id,
            price: item.goods.price,
            nums: 1,
            userName: user.name || user.username,
            userPhone: user.phone || '',
            userAddress: user.address || '',
            userId: user.id
          }
          return this.$request.post('/orders/add', data)
        })
        
        Promise.all(addPromises).then(results => {
          const successCount = results.filter(r => r.code === '200').length
          if (successCount > 0) {
            this.$notify.success({
              title: '成功', 
              message: `已成功添加 ${successCount}/${selectedItems.length} 件商品到购物车`,
              showClose: false, 
              duration: 2000
            });
            // 跳转到购物车
            setTimeout(() => {
              this.$router.push('/front/shopping-cart')
            }, 500)
          } else {
            this.$notify.error({title: '错误', message: '添加失败，请稍后重试', showClose: false, duration: 2000});
          }
        }).catch(err => {
          this.$notify.error({title: '错误', message: '添加失败', showClose: false, duration: 2000});
        })
      }).catch(() => {})
    },
    // 单个商品加入购物车
    addToCart(item) {
      const user = JSON.parse(localStorage.getItem('user') || '{}')
      if (!user.id) {
        this.$notify.error({title: '提示', message: '请先登录', showClose: false, duration: 2000});
        this.$router.push('/login')
        return
      }
      
      const data = {
        name: item.goods.name,
        goodsId: item.goods.id,
        price: item.goods.price,
        nums: 1,
        userName: user.name || user.username,
        userPhone: user.phone || '',
        userAddress: user.address || '',
        userId: user.id
      }
      
      this.$request.post('/orders/add', data).then(res => {
        if (res.code == '200'){
          this.$notify.success({title: '成功', message: '已加入购物车', showClose: false, duration: 2000});
          // 跳转到购物车页面
          this.$router.push('/front/shopping-cart')
        } else {
          this.$notify.error({title: '错误', message: res.msg, showClose: false, duration: 2000});
        }
      })
    }
  }
}
</script>

<style scoped>
.card-item:hover{
  cursor: pointer;
  transform: scale(1.03);
}
</style>
