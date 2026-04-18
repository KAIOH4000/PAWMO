<template>
  <div class="main-content">
    <!-- 店铺信息 -->
    <div class="store-info-card">
      <div class="store-header">
        <img :src="store.avatar || 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'" alt="Store Avatar" class="store-avatar">
        <div class="store-details">
          <h2 class="store-name">{{ store.shopName || '店铺名称加载中...' }}</h2>
          <p class="store-welcome">{{ store.shopInfo || '欢迎光临本店！' }}</p>
        </div>
      </div>
    </div>

    <!-- 商品展示 -->
    <div class="card" style="margin-top: 20px;">
      <div class="title">
        <span>店铺商品</span>
      </div>
      <div class="goods-list">
        <div v-for="item in goodsList" :key="item.id" class="goods-item" @click="goToGoodsDetail(item.id)">
          <img :src="item.cover" alt="" class="goods-img">
          <div class="goods-info">
            <div class="goods-name">{{ item.name }}</div>
            <div class="goods-price">￥ {{ item.price }}</div>
          </div>
        </div>
      </div>
      <!-- 分页 -->
      <div class="pagination">
        <el-pagination
            background
            @current-change="handleCurrentChange"
            :current-page="pageNum"
            :page-sizes="[8, 12, 16]"
            :page-size="pageSize"
            layout="total, prev, pager, next"
            :total="total">
        </el-pagination>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "Store",
  data() {
    return {
      storeId: this.$route.query.id,
      store: {},
      goodsList: [],
      pageNum: 1,
      pageSize: 8,
      total: 0,
    };
  },
  created() {
    this.loadStoreInfo();
    this.loadStoreGoods();
  },
  methods: {
    loadStoreInfo() {
      if (!this.storeId) return;
      this.$request.get('/user/selectById?id=' + this.storeId).then(res => {
        if (res.code === '200') {
          this.store = res.data;
        } else {
          this.$message.error(res.msg);
        }
      });
    },
    loadStoreGoods() {
      if (!this.storeId) return;
      this.$request.get('/goods/selectPage', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          shopId: this.storeId,
        }
      }).then(res => {
        if (res.code === '200') {
          this.goodsList = res.data.records;
          this.total = res.data.total;
        } else {
          this.$message.error(res.msg);
        }
      });
    },
    handleCurrentChange(pageNum) {
      this.pageNum = pageNum;
      this.loadStoreGoods();
    },
    goToGoodsDetail(id) {
      this.$router.push({ path: '/front/goodsdetail', query: { id: id } });
    }
  }
};
</script>

<style scoped>
.main-content {
  width: 70%;
  margin: 20px auto;
}
.store-info-card {
  background-color: #ffffff;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 2px 12px 0 rgba(0,0,0,0.1);
}
.store-header {
  display: flex;
  align-items: center;
}
.store-avatar {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  margin-right: 20px;
  border: 2px solid #eee;
}
.store-details {
  flex: 1;
}
.store-name {
  font-size: 24px;
  font-weight: bold;
  margin: 0;
}
.store-welcome {
  color: #666;
  margin-top: 5px;
}
.card {
  background-color: #ffffff;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 2px 12px 0 rgba(0,0,0,0.1);
}
.title {
  font-size: 20px;
  font-weight: bold;
  margin-bottom: 20px;
  border-left: 4px solid #409EFF;
  padding-left: 10px;
}
.goods-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(220px, 1fr));
  gap: 20px;
}
.goods-item {
  cursor: pointer;
  border: 1px solid #eee;
  border-radius: 5px;
  overflow: hidden;
  transition: all 0.3s;
}
.goods-item:hover {
  transform: translateY(-5px);
  box-shadow: 0 5px 15px rgba(0,0,0,0.1);
}
.goods-img {
  width: 100%;
  height: 220px;
  object-fit: cover;
}
.goods-info {
  padding: 10px;
}
.goods-name {
  font-size: 16px;
  color: #333;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
.goods-price {
  font-size: 18px;
  color: #e4393c;
  margin-top: 5px;
  font-weight: bold;
}
.pagination {
  margin-top: 30px;
  display: flex;
  justify-content: center;
}
</style>