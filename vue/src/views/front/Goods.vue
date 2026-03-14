<template>
  <div class="goods-container">
    <div class="goods-header">
      <div class="section-title">
        <span class="title-icon">🔥</span>
        <h1>热卖商品</h1>
      </div>
      <div class="search-box">
        <el-input 
          v-model="searchName" 
          placeholder="请输入搜索商品名称" 
          style="width: 250px" 
          clearable
          prefix-icon="el-icon-search"
          @keyup.enter.native="loadGoods(1)">
        </el-input>
        <el-button type="primary" icon="el-icon-search" @click="loadGoods(1)">搜索</el-button>
      </div>
    </div>

    <div class="type-filter">
      <el-button 
        :type="activeType === 0 ? 'primary' : 'default'" 
        @click="selectType(0)"
        :class="{ 'active-filter': activeType === 0 }">
        <i class="el-icon-s-grid"></i> 全部
      </el-button>
      <el-button 
        v-for="item in types" 
        :key="item.id"
        :type="activeType === item.id ? 'primary' : 'default'"
        @click="selectType(item.id)"
        :class="{ 'active-filter': activeType === item.id }">
        {{ item.name }}
      </el-button>
    </div>

    <div class="goods-list" v-loading="loading">
      <el-row :gutter="20">
        <el-col :span="6" v-for="(item, index) in goodsList" :key="index" style="margin-top: 20px">
          <el-card :body-style="{ padding: '0px' }" class="card-item" @click.native="goDetail(item.id)">
            <div class="image-wrapper">
              <img :src="item.cover" alt="" class="goods-image">
              <div class="image-overlay">
                <span class="view-text">查看详情</span>
              </div>
            </div>
            <div class="goods-info">
              <div class="goods-name">
                {{ item.name }}
              </div>
              <div class="goods-descr">
                {{ item.descr }}
              </div>
              <div class="goods-footer">
                <div class="goods-price">
                  <span class="price-symbol">¥</span>{{ item.price }}
                </div>
                <div class="goods-sales">
                  <i class="el-icon-sell"></i> {{ item.sales }}
                </div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
      
      <!-- 空状态 -->
      <el-empty v-if="!loading && goodsList.length === 0" description="暂无商品"></el-empty>
    </div>

    <div class="pagination">
      <el-pagination
        background
        layout="total, prev, pager, next, jumper"
        :total="total"
        :page-size="pageSize"
        :current-page="pageNum"
        @current-change="handlePageChange">
      </el-pagination>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Goods',
  data() {
    return {
      goodsList: [],
      types: [],
      searchName: '',
      activeType: 0,
      pageNum: 1,
      pageSize: 16,
      total: 0,
      loading: false
    }
  },
  created() {
    this.loadTypes()
    // 从URL获取分类参数
    const typeId = this.$route.query.typeId
    if (typeId) {
      this.activeType = parseInt(typeId)
    }
    this.loadGoods(1)
  },
  methods: {
    loadTypes() {
      this.$request.get('/type/selectAll').then(res => {
        this.types = res.data || []
      })
    },
    loadGoods(pageNum) {
      this.loading = true
      this.pageNum = pageNum
      const params = {
        pageNum: this.pageNum,
        pageSize: this.pageSize,
        name: this.searchName
      }
      
      let url = '/goods/selectPage'
      if (this.activeType !== 0) {
        url = '/goods/selectPage/type'
        params.typeId = this.activeType
      }
      
      this.$request.get(url, { params }).then(res => {
        this.goodsList = res.data.records || []
        this.total = res.data.total || 0
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    selectType(typeId) {
      this.activeType = typeId
      this.loadGoods(1)
    },
    handlePageChange(page) {
      this.loadGoods(page)
    },
    goDetail(id) {
      this.$router.push('/front/goodsDetail/' + id)
    }
  }
}
</script>

<style scoped>
/* 动画定义 */
@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes shimmer {
  0% { background-position: -200% 0; }
  100% { background-position: 200% 0; }
}

@keyframes float {
  0%, 100% { transform: translateY(0); }
  50% { transform: translateY(-5px); }
}

.goods-container {
  width: 70%;
  margin: 0 auto;
  min-height: 90vh;
  padding: 20px 0;
}

.goods-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 25px;
  padding: 15px 0;
}

.section-title {
  display: flex;
  align-items: center;
  gap: 10px;
  border-left: 4px solid #f89524;
  padding-left: 15px;
}

.section-title h1 {
  margin: 0;
  font-size: 24px;
  color: #303133;
  font-weight: 600;
}

.title-icon {
  font-size: 28px;
  animation: float 2s ease-in-out infinite;
}

.search-box {
  display: flex;
  gap: 10px;
}

.search-box .el-input >>> .el-input__inner {
  border-radius: 20px;
  transition: all 0.3s ease;
}

.search-box .el-input >>> .el-input__inner:focus {
  box-shadow: 0 4px 12px rgba(248, 149, 36, 0.2);
}

.search-box .el-button {
  border-radius: 20px;
  transition: all 0.3s ease;
}

.search-box .el-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 15px rgba(248, 149, 36, 0.3);
}

.type-filter {
  margin-bottom: 15px;
  padding: 10px 0;
}

.type-filter .el-button {
  margin-right: 10px;
  margin-bottom: 10px;
  border-radius: 20px;
  transition: all 0.3s ease;
  padding: 10px 20px;
}

.type-filter .el-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0,0,0,0.1);
}

.type-filter .el-button--primary {
  background: linear-gradient(135deg, #f89524 0%, #ff6b35 100%);
  border: none;
  box-shadow: 0 4px 15px rgba(248, 149, 36, 0.3);
}

.type-filter .el-button--primary:hover {
  background: linear-gradient(135deg, #ff9f2e 0%, #ff7b45 100%);
  box-shadow: 0 6px 20px rgba(248, 149, 36, 0.4);
}

.search-box .el-button--primary {
  background: linear-gradient(135deg, #f89524 0%, #ff6b35 100%);
  border: none;
}

.search-box .el-button--primary:hover {
  background: linear-gradient(135deg, #ff9f2e 0%, #ff7b45 100%);
}

.goods-list {
  min-height: 400px;
}

/* 商品卡片样式 */
.card-item {
  border-radius: 12px;
  overflow: hidden;
  transition: all 0.4s cubic-bezier(0.175, 0.885, 0.32, 1.275);
  animation: fadeInUp 0.6s ease-out;
  border: none;
  box-shadow: 0 4px 15px rgba(0,0,0,0.08);
}

.card-item:hover {
  cursor: pointer;
  transform: translateY(-8px) scale(1.02);
  box-shadow: 0 20px 40px rgba(0,0,0,0.15);
}

.image-wrapper {
  position: relative;
  overflow: hidden;
  height: 200px;
}

.goods-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.5s ease;
}

.card-item:hover .goods-image {
  transform: scale(1.1);
}

.image-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0,0,0,0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  opacity: 0;
  transition: all 0.3s ease;
}

.card-item:hover .image-overlay {
  opacity: 1;
}

.view-text {
  color: #fff;
  font-size: 14px;
  padding: 8px 20px;
  border: 2px solid #fff;
  border-radius: 20px;
  transition: all 0.3s ease;
}

.view-text:hover {
  background: #fff;
  color: #333;
}

.goods-info {
  padding: 15px;
  background: #fff;
}

.goods-name {
  font-size: 15px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 8px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.goods-descr {
  font-size: 12px;
  color: #909399;
  margin-bottom: 12px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.goods-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.goods-price {
  font-size: 22px;
  color: #ff6b35;
  font-weight: 700;
}

.price-symbol {
  font-size: 14px;
  margin-right: 2px;
}

.goods-sales {
  font-size: 12px;
  color: #909399;
  display: flex;
  align-items: center;
  gap: 4px;
}

.goods-sales i {
  color: #f89524;
}

.pagination {
  margin-top: 40px;
  display: flex;
  justify-content: center;
}

.pagination >>> .el-pagination.is-background .el-pager li {
  transition: all 0.3s ease;
  border-radius: 8px;
}

.pagination >>> .el-pagination.is-background .el-pager li:hover {
  transform: scale(1.1);
}

.pagination >>> .el-pagination.is-background .el-pager li.active {
  background: linear-gradient(135deg, #f89524 0%, #ff6b35 100%);
}

/* 响应式设计 */
@media (max-width: 1200px) {
  .goods-container {
    width: 90%;
  }
}

@media (max-width: 768px) {
  .goods-container {
    width: 95%;
  }
  
  .goods-header {
    flex-direction: column;
    gap: 15px;
    align-items: flex-start;
  }
  
  .el-col {
    width: 100% !important;
  }
}
</style>
