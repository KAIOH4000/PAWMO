﻿<template>
  <div class="homeContainer">
    <!-- 顶部分类Tab 导航 -->
    <div class="category-tabs-wrapper">
      <div class="category-tabs">
        <div 
          v-for="(item, index) in types" 
          :key="index"
          class="tab-item"
          :class="{ 'tab-active': selectedType === item.id }"
          @click="handleTypeClick(item)"
        >
          <i :class="getTypeIcon(item.name)" class="tab-icon"></i>
          <span class="tab-text">{{ item.name }}</span>
        </div>
      </div>
    </div>

    <!-- 轮播区域 -->
    <div class="carousel-wrapper">
      <el-carousel height="400px" :interval="10000">
        <el-carousel-item v-for="item in carousels" :key="item.id">
          <img :src="item.cover" class="carousel-img" @click="goToGoods(item.typeId)" style="cursor: pointer">
        </el-carousel-item>
      </el-carousel>
    </div>

    <!-- 新品上架 -->
    <div class="section">
      <div class="section-header">
        <div class="section-title">
          <span class="title-icon">✨</span>
          <h1>新品上架</h1>
        </div>
        <el-link href="/front/goods" :underline="false" class="view-more">
          查看更多 <i class="el-icon-arrow-right"></i>
        </el-link>
      </div>
      <div class="goods-grid">
        <el-row :gutter="20">
          <el-col :span="6" v-for="(item,index) in timeGoods" :key="index">
            <el-card :body-style="{ padding: '0px' }" class="goods-card">
              <div class="goods-image-wrapper">
                <img :src="item.cover" class="goods-image" @click="goToDetail(item.id)">
                <div class="new-badge">NEW</div>
              </div>
              <div class="goods-info" @click="goToDetail(item.id)">
                <h3 class="goods-name">{{ item.name }}</h3>
                <p class="goods-descr">{{ item.descr }}</p>
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
      </div>
    </div>

    <!-- 热销商品 -->
    <div class="section">
      <div class="section-header">
        <div class="section-title">
          <span class="title-icon">🔥</span>
          <h1>热销商品</h1>
        </div>
        <el-link href="/front/goods" :underline="false" class="view-more">
          查看更多 <i class="el-icon-arrow-right"></i>
        </el-link>
      </div>
      <div class="goods-grid">
        <el-row :gutter="20">
          <el-col :span="6" v-for="(item,index) in salesGoods" :key="index">
            <el-card :body-style="{ padding: '0px' }" class="goods-card">
              <div class="goods-image-wrapper">
                <img :src="item.cover" class="goods-image" @click="goToDetail(item.id)">
                <div class="hot-badge" v-if="index < 3">TOP{{index + 1}}</div>
              </div>
              <div class="goods-info" @click="goToDetail(item.id)">
                <h3 class="goods-name">{{ item.name }}</h3>
                <p class="goods-descr">{{ item.descr }}</p>
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
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Home',
  data() {
    return {
      types: [],
      carousels: [],
      timeGoods: [],
      salesGoods: [],
      selectedType: null
    }
  },
  mounted() {
    this.loadData()
  },
  methods: {
    loadData() {
      // 获取商品分类
      this.$request.get('/type/selectAll').then(res => {
        this.types = res.data
      })

      // 获取轮播图
      this.$request.get('/carousel/selectAll').then(res => {
        this.carousels = res.data
      })

      // 获取新品上架
      this.$request.get('/goods/times').then(res => {
        this.timeGoods = res.data
      })

      // 获取热销商品
      this.$request.get('/goods/sales').then(res => {
        this.salesGoods = res.data
      })
    },
    handleTypeClick(type) {
      this.selectedType = type.id
      // 可以添加跳转到对应分类商品列表的逻辑
    },
    getTypeIcon(typeName) {
      const iconMap = {
        '猫咪': 'el-icon-cpu',
        '狗狗': 'el-icon-s-home',
        '小宠': 'el-icon-s-marketing',
        '水族': 'el-icon-s-finance',
        '爬虫': 'el-icon-s-order'
      }
      return iconMap[typeName] || 'el-icon-s-grid'
    },
    goToGoods(typeId) {
      this.$router.push(`/front/goods?typeId=${typeId}`)
    },
    goToDetail(id) {
      this.$router.push(`/front/goodsDetail/${id}`)
    }
  }
}
</script>

<style scoped>
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

@keyframes pulse {
  0%, 100% { transform: scale(1); }
  50% { transform: scale(1.05); }
}

.homeContainer {
  width: 70%;
  margin: 0 auto;
  min-height: 90vh;
  padding: 20px 40px 40px;
}

/* 分类Tab 样式 */
.category-tabs-wrapper {
  margin-bottom: 20px;
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 4px 15px rgba(0,0,0,0.08);
  overflow: hidden;
}

.category-tabs {
  display: flex;
  width: 100%;
}

.tab-item {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 18px 10px;
  cursor: pointer;
  transition: all 0.3s ease;
  border-bottom: 3px solid transparent;
  background: #fff;
  position: relative;
}

.tab-item:hover {
  background: #fff8f0;
  color: #f89524;
}

.tab-item:hover .tab-icon {
  transform: scale(1.1);
}

.tab-active {
  background: linear-gradient(180deg, #fff8f0 0%, #fff 100%);
  border-bottom-color: #f89524;
  color: #f89524;
}

.tab-active .tab-icon {
  color: #f89524;
}

.tab-icon {
  font-size: 28px;
  margin-bottom: 8px;
  color: #909399;
  transition: all 0.3s ease;
}

.tab-text {
  font-size: 14px;
  font-weight: 500;
  white-space: nowrap;
}

/* 轮播图样式 */
.carousel-wrapper {
  width: 100%;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 10px 30px rgba(0,0,0,0.1);
  margin-bottom: 30px;
}

.carousel-img {
  width: 100%;
  height: 400px;
  object-fit: cover;
  transition: transform 0.5s ease;
}

.carousel-img:hover {
  transform: scale(1.05);
}

/* 商品区域样式 */
.section {
  margin-top: 50px;
  animation: fadeInUp 0.6s ease-out;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 25px;
  padding: 10px 0;
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

.view-more {
  font-size: 14px;
  color: #909399;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  gap: 5px;
}

.view-more:hover {
  color: #f89524;
  transform: translateX(5px);
}

/* 商品网格样式 */
.goods-grid .el-col {
  margin-top: 20px;
}

.goods-card {
  border-radius: 12px;
  overflow: hidden;
  transition: all 0.4s cubic-bezier(0.175, 0.885, 0.32, 1.275);
  animation: fadeInUp 0.6s ease-out;
  border: none;
  box-shadow: 0 4px 15px rgba(0,0,0,0.08);
}

.goods-card:hover {
  cursor: pointer;
  transform: translateY(-8px) scale(1.02);
  box-shadow: 0 20px 40px rgba(0,0,0,0.15);
}

.goods-image-wrapper {
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

.goods-card:hover .goods-image {
  transform: scale(1.1);
}

/* 标签 */
.new-badge, .hot-badge {
  position: absolute;
  top: 10px;
  left: 10px;
  padding: 4px 12px;
  border-radius: 20px;
  font-size: 12px;
  font-weight: 600;
  color: #fff;
  z-index: 2;
}

.new-badge {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  animation: pulse 2s infinite;
}

.hot-badge {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
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

/* 响应式设计 */
@media (max-width: 1200px) {
  .homeContainer {
    width: 90%;
  }

  .carousel-wrapper {
    height: 300px;
  }
}

@media (max-width: 768px) {
  .homeContainer {
    width: 95%;
  }

  .el-col {
    width: 100% !important;
  }

  .carousel-wrapper {
    height: 200px;
  }
}
</style>
