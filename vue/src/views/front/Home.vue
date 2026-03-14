<template>
  <div class="homeContainer">
    <!-- 顶部分类Tab导航 -->
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
            <el-card :body-style="{ padding: '0px' }" class="card-item" @click.native="goDetail(item.id)">
              <div class="image-wrapper">
                <img :src="item.cover" alt="" class="goods-image">
                <div class="new-badge">NEW</div>
                <div class="image-overlay">
                  <span class="view-text">查看详情</span>
                </div>
              </div>
              <div class="goods-info">
                <div class="goods-name">{{item.name}}</div>
                <div class="goods-descr">{{item.descr}}</div>
                <div class="goods-footer">
                  <div class="goods-price">
                    <span class="price-symbol">¥</span>{{item.price}}
                  </div>
                  <div class="goods-sales">
                    <i class="el-icon-sell"></i> {{item.sales}}
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
            <el-card :body-style="{ padding: '0px' }" class="card-item" @click.native="goDetail(item.id)">
              <div class="image-wrapper">
                <img :src="item.cover" alt="" class="goods-image">
                <div class="hot-badge" v-if="index < 3">TOP{{index + 1}}</div>
                <div class="image-overlay">
                  <span class="view-text">查看详情</span>
                </div>
              </div>
              <div class="goods-info">
                <div class="goods-name">{{item.name}}</div>
                <div class="goods-descr">{{item.descr}}</div>
                <div class="goods-footer">
                  <div class="goods-price">
                    <span class="price-symbol">¥</span>{{item.price}}
                  </div>
                  <div class="goods-sales">
                    <i class="el-icon-sell"></i> {{item.sales}}
                  </div>
                </div>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </div>
    </div>

    <!-- 猫咪模块 -->
    <div class="category-section">
      <div class="category-left">
        <img :src="promotionImages.cat" alt="猫咪用品" class="promo-image">
        <div class="promo-overlay">
          <h3>猫咪专区</h3>
          <p>精选猫咪用品</p>
          <el-button class="promo-more-btn" size="small" @click="goToGoods(1)">查看更多</el-button>
        </div>
      </div>
      <div class="category-right">
        <el-row :gutter="15">
          <el-col :span="8" v-for="(item, index) in catGoods" :key="index">
            <el-card class="category-card" @click.native="goDetail(item.id)">
              <img :src="item.cover" class="category-goods-image">
              <div class="category-goods-name">{{ item.name }}</div>
              <div class="category-goods-price">¥{{ item.price }}</div>
            </el-card>
          </el-col>
        </el-row>
      </div>
    </div>

    <!-- 狗狗模块 -->
    <div class="category-section">
      <div class="category-left">
        <img :src="promotionImages.dog" alt="狗狗用品" class="promo-image">
        <div class="promo-overlay">
          <h3>狗狗专区</h3>
          <p>精选狗狗用品</p>
          <el-button class="promo-more-btn" size="small" @click="goToGoods(2)">查看更多</el-button>
        </div>
      </div>
      <div class="category-right">
        <el-row :gutter="15">
          <el-col :span="8" v-for="(item, index) in dogGoods" :key="index">
            <el-card class="category-card" @click.native="goDetail(item.id)">
              <img :src="item.cover" class="category-goods-image">
              <div class="category-goods-name">{{ item.name }}</div>
              <div class="category-goods-price">¥{{ item.price }}</div>
            </el-card>
          </el-col>
        </el-row>
      </div>
    </div>

    <!-- 小宠模块 -->
    <div class="category-section">
      <div class="category-left">
        <img :src="promotionImages.small_pet" alt="小宠用品" class="promo-image">
        <div class="promo-overlay">
          <h3>小宠专区</h3>
          <p>精选小宠用品</p>
          <el-button class="promo-more-btn" size="small" @click="goToGoods(3)">查看更多</el-button>
        </div>
      </div>
      <div class="category-right">
        <el-row :gutter="15">
          <el-col :span="8" v-for="(item, index) in smallPetGoods" :key="index">
            <el-card class="category-card" @click.native="goDetail(item.id)">
              <img :src="item.cover" class="category-goods-image">
              <div class="category-goods-name">{{ item.name }}</div>
              <div class="category-goods-price">¥{{ item.price }}</div>
            </el-card>
          </el-col>
        </el-row>
      </div>
    </div>

    <!-- 水族模块 -->
    <div class="category-section">
      <div class="category-left">
        <img :src="promotionImages.aquarium" alt="水族用品" class="promo-image">
        <div class="promo-overlay">
          <h3>水族专区</h3>
          <p>精选水族用品</p>
          <el-button class="promo-more-btn" size="small" @click="goToGoods(4)">查看更多</el-button>
        </div>
      </div>
      <div class="category-right">
        <el-row :gutter="15">
          <el-col :span="8" v-for="(item, index) in aquariumGoods" :key="index">
            <el-card class="category-card" @click.native="goDetail(item.id)">
              <img :src="item.cover" class="category-goods-image">
              <div class="category-goods-name">{{ item.name }}</div>
              <div class="category-goods-price">¥{{ item.price }}</div>
            </el-card>
          </el-col>
        </el-row>
      </div>
    </div>

    <!-- 爬宠模块 -->
    <div class="category-section">
      <div class="category-left">
        <img :src="promotionImages.reptile" alt="爬宠用品" class="promo-image">
        <div class="promo-overlay">
          <h3>爬宠专区</h3>
          <p>精选爬宠用品</p>
          <el-button class="promo-more-btn" size="small" @click="goToGoods(5)">查看更多</el-button>
        </div>
      </div>
      <div class="category-right">
        <el-row :gutter="15">
          <el-col :span="8" v-for="(item, index) in reptileGoods" :key="index">
            <el-card class="category-card" @click.native="goDetail(item.id)">
              <img :src="item.cover" class="category-goods-image">
              <div class="category-goods-name">{{ item.name }}</div>
              <div class="category-goods-price">¥{{ item.price }}</div>
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
      carousels: [],
      types: [],
      timeGoods: [],
      salesGoods: [],
      selectedType: null, // 当前选中的分类ID
      // 分类商品
      catGoods: [],
      dogGoods: [],
      smallPetGoods: [],
      aquariumGoods: [],
      reptileGoods: [],
      // 宣传图
      promotionImages: {
        cat: '',
        dog: '',
        small_pet: '',
        aquarium: '',
        reptile: ''
      }
    }
  },
  created() {
    // 分批加载，避免并发请求过多导致429错误
    this.loadType()
    this.loadCarousel()
    // 延迟加载商品数据
    setTimeout(() => {
      this.loadTimeGoods()
    }, 500)
    setTimeout(() => {
      this.loadSaleGoods()
    }, 1000)
    // 加载分类商品和宣传图
    setTimeout(() => {
      this.loadPromotionImages()
    }, 1500)
    setTimeout(() => {
      this.loadCategoryGoods()
    }, 2000)
    // 从URL参数或本地存储恢复选中的分类
    this.restoreSelectedType()
  },
  watch: {
    // 监听路由变化，更新选中状态
    '$route.query.typeId': {
      handler(newVal) {
        if (newVal) {
          this.selectedType = parseInt(newVal)
        }
      },
      immediate: true
    }
  },
  methods: {
    loadCarousel(){
      this.$request.get('/carousel/selectAll').then(res => {
        this.carousels = res.data
      })
    },
    loadType(){
      this.$request.get('/type/selectAll').then(res => {
        this.types = res.data
      })
    },
    loadTimeGoods(){
      this.$request.get('/goods/times').then(res => {
        this.timeGoods = res.data || []
      })
    },
    loadSaleGoods(){
      this.$request.get('/goods/sales').then(res => {
        this.salesGoods = res.data || []
      })
    },
    goPage(url){
      location.href = url
    },
    goToGoods(typeId) {
      if (typeId) {
        this.$router.push('/front/goods?typeId=' + typeId)
      } else {
        this.$router.push('/front/goods')
      }
    },
    goDetail(id) {
      this.$router.push('/front/goodsDetail/' + id)
    },
    
    /**
     * 处理分类Tab点击事件
     * @param {Object} item - 分类对象
     */
    handleTypeClick(item) {
      this.selectedType = item.id
      // 保存选中状态到本地存储
      localStorage.setItem('homeSelectedType', item.id)
      // 跳转到商品列表页并携带分类参数
      this.goToGoods(item.id)
    },
    
    /**
     * 恢复选中的分类状态
     */
    restoreSelectedType() {
      const savedType = localStorage.getItem('homeSelectedType')
      const urlType = this.$route.query.typeId
      if (urlType) {
        this.selectedType = parseInt(urlType)
      } else if (savedType) {
        this.selectedType = parseInt(savedType)
      }
    },
    
    /**
     * 根据分类名称获取对应图标
     * @param {String} name - 分类名称
     * @returns {String} 图标类名
     */
    getTypeIcon(name) {
      const iconMap = {
        '猫咪': 'el-icon-cat',
        '狗狗': 'el-icon-dog',
        '小宠': 'el-icon-rabbit',
        '水族': 'el-icon-fish',
        '爬宠': 'el-icon-lizard'
      }
      // 如果没有匹配到，使用默认图标
      return iconMap[name] || 'el-icon-goods'
    },

    /**
     * 加载宣传图
     */
    loadPromotionImages() {
      this.$request.get('/promotion/selectAll').then(res => {
        const data = res.data || []
        data.forEach(item => {
          if (item.category && item.imageUrl) {
            this.promotionImages[item.category] = item.imageUrl
          }
        })
      })
    },

    /**
     * 加载各分类商品
     */
    loadCategoryGoods() {
      // 假设分类ID: 猫咪=1, 狗狗=2, 小宠=3, 水族=4, 爬宠=5
      const categoryMap = {
        1: 'catGoods',
        2: 'dogGoods',
        3: 'smallPetGoods',
        4: 'aquariumGoods',
        5: 'reptileGoods'
      }

      // 遍历分类，加载商品
      Object.keys(categoryMap).forEach(typeId => {
        this.$request.get('/goods/selectPage/type', {
          params: {
            typeId: typeId,
            pageNum: 1,
            pageSize: 6,
            name: ''
          }
        }).then(res => {
          this[categoryMap[typeId]] = res.data?.records || []
        })
      })
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

@keyframes pulse {
  0%, 100% { transform: scale(1); }
  50% { transform: scale(1.05); }
}

.homeContainer{
  width: 70%;
  margin: 0 auto;
  min-height: 90vh;
  padding: 20px 40px 40px;
}

/* 顶部分类Tab导航样式 */
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

/* 响应式Tab样式 */
@media (max-width: 768px) {
  .tab-item {
    padding: 12px 5px;
  }
  
  .tab-icon {
    font-size: 22px;
  }
  
  .tab-text {
    font-size: 12px;
  }
}

/* 轮播区域 */
.carousel-wrapper {
  width: 100%;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 10px 30px rgba(0,0,0,0.1);
  margin-bottom: 30px;
}

.carousel-slide {
  position: relative;
  height: 100%;
  overflow: hidden;
}

.carousel-img{
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.5s ease;
}

.carousel-slide:hover .carousel-img {
  transform: scale(1.05);
}

.carousel-overlay {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 30px;
  background: linear-gradient(transparent, rgba(0,0,0,0.7));
  opacity: 0;
  transition: opacity 0.3s ease;
}

.carousel-slide:hover .carousel-overlay {
  opacity: 1;
}

.carousel-text {
  color: #fff;
  font-size: 18px;
  font-weight: 600;
}

/* 区块样式 */
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

/* 商品网格 */
.goods-grid .el-col {
  margin-top: 20px;
}

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

/* 分类模块样式 */
.category-section {
  display: flex;
  margin-top: 50px;
  background: #fff;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 4px 15px rgba(0,0,0,0.08);
  animation: fadeInUp 0.6s ease-out;
}

.category-left {
  flex: 4;
  position: relative;
  overflow: hidden;
  min-height: 350px;
}

.category-right {
  flex: 8;
  padding: 20px;
}

.promo-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.5s ease;
}

.category-section:hover .promo-image {
  transform: scale(1.05);
}

.promo-overlay {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 30px 20px;
  background: linear-gradient(transparent, rgba(0,0,0,0.7));
  color: #fff;
}

.promo-overlay h3 {
  margin: 0 0 8px 0;
  font-size: 24px;
  font-weight: 600;
}

.promo-overlay p {
  margin: 0;
  font-size: 14px;
  opacity: 0.9;
}

.promo-more-btn {
  margin-top: 15px;
  background: #f89524;
  border-color: #f89524;
  color: #fff;
  font-weight: 500;
  transition: all 0.3s ease;
}

.promo-more-btn:hover {
  background: #ff7f20;
  border-color: #ff7f20;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(248, 149, 36, 0.4);
}

.promo-more-btn:focus {
  background: #ff7f20;
  border-color: #ff7f20;
}

.category-card {
  border-radius: 8px;
  margin-bottom: 15px;
  transition: all 0.3s ease;
  cursor: pointer;
  border: none;
  box-shadow: 0 2px 8px rgba(0,0,0,0.06);
}

.category-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 20px rgba(0,0,0,0.1);
}

.category-goods-image {
  width: 100%;
  height: 140px;
  object-fit: cover;
  border-radius: 8px 8px 0 0;
}

.category-goods-name {
  padding: 10px 12px 5px;
  font-size: 13px;
  color: #303133;
  font-weight: 500;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  line-height: 1.4;
  min-height: 38px;
}

.category-goods-price {
  padding: 0 12px 10px;
  color: #ff6b35;
  font-weight: bold;
  font-size: 16px;
}

/* 响应式分类模块 */
@media (max-width: 1200px) {
  .category-section {
    flex-direction: column;
  }

  .category-left {
    min-height: 250px;
  }

  .category-right {
    padding: 15px;
  }

  .category-goods-image {
    height: 120px;
  }
}

@media (max-width: 768px) {
  .category-left {
    min-height: 200px;
  }

  .category-right .el-col {
    width: 100% !important;
    margin-bottom: 15px;
  }
}
</style>
