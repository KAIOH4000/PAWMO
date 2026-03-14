<template>
  <div style="width: 60%;min-height: 90vh;margin: 10px auto">
    <div style="display: flex">
      <div style="flex: 4">
        <el-image v-if="goods.cover" style="width: 400px; height: 300px" :src="goods.cover" fit="cover" :preview-src-list="[goods.cover]"></el-image>
      </div>
      <div style="flex: 6">
        <div style="padding: 0 15px">
          <b style="font-size: 20px;color: #303133;">{{goods.name}}</b>
        </div>
        <div style="padding: 0 15px;margin-top: 10px;">
          <p style="font-size: 11px;color: #606266;line-height: 17px;">{{goods.descr}}</p>
          <div style="border-bottom: 1px dashed #eaeaea;margin-top: 10px"></div>
        </div>
        <div style="display: flex;padding: 0 15px;margin-top: 20px;align-items: center">
          <div>
            <span style="font-size: 11px;color: #606266">价格：</span>
            <span style="font-size: 17px;color: #ff6700;font-weight: bold">￥{{goods.price}}</span>
          </div>
          <div style="margin-left: 20px">
            <span style="font-size: 11px;color: #606266">库存：{{goods.store}}</span>
          </div>
          <div style="margin-left: 20px">
            <span style="font-size: 11px;color: #606266">累计热销：{{goods.sales}}</span>
          </div>
        </div>
        <div style="display: flex;justify-content: space-between;align-items: center;padding: 0 15px;margin-top: 20px">
          <div>
            <span style="font-size: 11px;color: #606266">上架时间：{{goods.date}}</span>
          </div>
          <div>
            <el-button :type="isCollect ? 'danger' : 'info'" size="mini" plain @click="collect" style="font-size: 12px;">{{isCollect ? '已收藏' : '☆ 收藏'}}</el-button>
          </div>
        </div>
        <div style="display: flex;padding: 0 15px;margin-top: 15px;gap: 20px">
          <div>
            <el-input-number v-model="num" @change="handleChange" :min="1" :max="10" label="描述文字"></el-input-number>
          </div>
          <div>
            <el-button type="primary" style="width: 180px" @click="addToCart">加入购物车</el-button>
          </div>
          <div>
            <el-button type="warning" style="width: 180px" @click="buyNow">立即购买</el-button>
          </div>
        </div>
      </div>
    </div>

    <div style="margin-top: 30px">
      <el-card>
        <el-tabs v-model="activeName" type="card" @tab-click="handleClick">
          <el-tab-pane label="详细介绍" name="goods">
            <div class="w-e-text" v-html="goods.content"></div>
          </el-tab-pane>
          <el-tab-pane label="用户评价" name="review">
            <div class="review-container">
              <!-- 评价列表 -->
              <div class="review-list-section" style="flex: 1;">
                <h3 class="review-title">用户评价 ({{ reviewList.length }})</h3>
                <div v-if="reviewList.length === 0" style="text-align: center; padding: 40px; color: #999;">
                  <i class="el-icon-chat-line-square" style="font-size: 48px; margin-bottom: 10px;"></i>
                  <div>暂无评价，购买后可前往售后服务页发表评价</div>
                </div>
                <div class="review-list">
                  <div v-for="(review, index) in reviewList" :key="index" class="review-item">
                    <div class="review-header">
                      <div class="reviewer-info">
                        <el-avatar :size="40" :src="review.userAvatar || review.avatar" class="reviewer-avatar"></el-avatar>
                        <div class="reviewer-meta">
                          <span class="reviewer-name">{{ review.username }}</span>
                          <span class="review-time">{{ formatTime(review.createTime || review.time) }}</span>
                        </div>
                      </div>
                      <div class="review-rating">
                        <i v-for="n in 5" :key="n" 
                           class="el-icon-star-on" 
                           :class="{ 'star-active': n <= (review.score || review.rating) }"></i>
                      </div>
                    </div>
                    <div class="review-content-text">{{ review.content }}</div>
                  </div>
                </div>
              </div>
            </div>
          </el-tab-pane>
          <el-tab-pane label="购买须知" name="notice">
            <div style="padding: 25px;">
              <h3 style="color: #333;margin: 15px 0;">购买说明</h3>
              <div>
                <div style="margin: 10px 0;color: #666;">1、正品保证</div>
                <div style="margin: 10px 0;color: #666;">2、7天无理由退货</div>
                <div style="margin: 10px 0;color: #666;">3、全国包邮</div>
                <div style="margin: 10px 0;color: #666;">4、售后无忧</div>
              </div>
            </div>
          </el-tab-pane>
        </el-tabs>
      </el-card>
    </div>
  </div>
</template>

<script>
export default {
  name: "GoodsDetail",
  data(){
    return{
      id: this.$route.query.id || this.$route.params.id,
      goods: { cover: '', name: '', descr: '', price: '', store: '', sales: '', date: '', content: '' },
      num: 1,
      activeName: 'goods',
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
      isCollect: false,
      // 评价相关数据
      reviewList: []
    }
  },

  created() {
    this.loadGoods()
  },
  methods:{
    loadGoods(){
      this.$request.get('/goods/selectById?id=' + this.id).then(res => {
        if (res.data) {
          this.goods = res.data
          this.isCollect = this.goods.isCollect || false
          // 商品加载完成后加载评价
          this.loadReviews()
        }
      })
    },
    handleChange(value) {
      this.num = value;
    },
    handleClick(tab, event) {
      // 标签页切换
    },
    collect(){
      const data = {goodsId: this.goods.id}
      this.$request.post('/collect/add',data).then(res => {
        if (res.code == '200'){
          this.$notify.success({title: '成功', message: '收藏成功', showClose: false, duration: 2000});
          this.isCollect = true
        } else {
          this.$notify.error({title: '成功', message: res.msg, showClose: false, duration: 2000});
          this.isCollect = false
        }
        this.loadGoods()
      })
    },
    addToCart(){
      if (!this.user.id) {
        this.$notify.error({title: '提示', message: '请先登录', showClose: false, duration: 2000});
        this.$router.push('/login')
        return
      }
      const data = {
        name: this.goods.name,
        goodsId: this.goods.id,
        price: this.goods.price * this.num,
        nums: this.num,
        userName: this.user.name || this.user.username,
        userPhone: this.user.phone || '',
        userAddress: this.user.address || '',
        userId: this.user.id
      }
      this.$request.post('/orders/add',data).then(res => {
        if (res.code == '200'){
          this.$notify.success({title: '成功', message: '已加入购物车', showClose: false, duration: 2000});
          // 跳转到购物车页面
          this.$router.push('/front/shopping-cart')
        } else {
          this.$notify.error({title: '错误', message: res.msg, showClose: false, duration: 2000});
        }
      })
    },
    buyNow(){
      if (!this.user.id) {
        this.$notify.error({title: '提示', message: '请先登录', showClose: false, duration: 2000});
        this.$router.push('/login')
        return
      }
      const data = {
        name: this.goods.name,
        goodsId: this.goods.id,
        price: this.goods.price * this.num,
        nums: this.num,
        userName: this.user.name || this.user.username,
        userPhone: this.user.phone || '',
        userAddress: this.user.address || '',
        userId: this.user.id
      }
      this.$request.post('/orders/add',data).then(res => {
        if (res.code == '200'){
          this.$notify.success({title: '成功', message: '下单成功，请尽快支付', showClose: false, duration: 2000});
          // 直接跳转到购物车页面进行结算
          this.$router.push('/front/shopping-cart')
        } else {
          this.$notify.error({title: '错误', message: res.msg, showClose: false, duration: 2000});
        }
      })
    },
    
    /**
     * 加载评价列表
     */
    loadReviews() {
      const productId = this.goods.id || this.id
      if (!productId) return
          
      this.$request.get('/product/reviews/' + productId).then(res => {
        if (res.code === '200') {
          this.reviewList = res.data?.list || []
        }
      }).catch((err) => {
        console.error('加载评价失败:', err)
      })
    },
    
    /**
     * 格式化时间
     */
    formatTime(time) {
      if (!time) return ''
      // 如果是数组格式 [2026, 3, 14, 18, 56, 17]
      if (Array.isArray(time)) {
        const [year, month, day, hour, minute, second] = time
        return `${year}.${String(month).padStart(2, '0')}.${String(day).padStart(2, '0')} ${String(hour).padStart(2, '0')}:${String(minute).padStart(2, '0')}`
      }
      // 如果是字符串，直接替换逗号为点
      if (typeof time === 'string' && time.includes(',')) {
        return time.replace(/,/g, '.')
      }
      return time
    }
  }
}
</script>

<style scoped>
/* ==================== 用户评价区域样式 ==================== */

/* 评价容器 */
.review-container {
  padding: 20px;
  min-height: 400px;
}

/* 评价列表区域 */
.review-list-section {
  width: 100%;
}

/* 评价区域标题 */
.review-title {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 20px;
  padding-bottom: 10px;
  border-bottom: 2px solid #f89524;
}

/* ==================== 评价列表项样式 ==================== */
.review-list {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.review-item {
  background: #fff;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.06);
  transition: all 0.3s ease;
}

.review-item:hover {
  box-shadow: 0 4px 20px rgba(0,0,0,0.1);
  transform: translateY(-2px);
}

/* 评价头部：用户信息+评分 */
.review-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.reviewer-info {
  display: flex;
  align-items: center;
  gap: 12px;
}

.reviewer-avatar {
  border: 2px solid #f89524;
}

.reviewer-meta {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.reviewer-name {
  font-size: 14px;
  font-weight: 600;
  color: #303133;
}

.review-time {
  font-size: 12px;
  color: #909399;
}

/* 评分星星样式 */
.review-rating i {
  font-size: 16px;
  color: #dcdfe6;
  margin-right: 2px;
}

.review-rating i.star-active {
  color: #f89524;
}

/* 评价内容文字 */
.review-content-text {
  font-size: 14px;
  color: #606266;
  line-height: 1.6;
  padding-left: 52px;
}

/* ==================== 响应式适配 ==================== */
@media (max-width: 768px) {
  .review-container {
    flex-direction: column;
  }
  
  .review-content-text {
    padding-left: 0;
    margin-top: 10px;
  }
  
  .review-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;
  }
}
</style>