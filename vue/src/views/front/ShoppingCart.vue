<template>
  <div class="shopping-cart-container">
    <div class="content-wrapper">
      <!-- 左侧购物车列表 -->
      <el-card class="table-card" style="flex: 1; margin-right: 20px;">
        <div slot="header" style="display: flex; justify-content: space-between; align-items: center;">
          <span style="font-weight: bold; font-size: 16px;">
            <i class="el-icon-shopping-cart-2"></i> 购物车
            <el-tag v-if="cartItems.length > 0" type="danger" size="small" style="margin-left: 10px;">
              {{ cartItems.length }} 件商品
            </el-tag>
          </span>
          <el-button
            v-if="cartItems.length > 0"
            type="danger"
            size="small"
            @click="clearCart"
            plain>
            <i class="el-icon-delete"></i> 清空购物车
          </el-button>
        </div>

        <!-- 空购物车提示 -->
        <div v-if="cartItems.length === 0" style="text-align: center; padding: 60px 0; color: #999;">
          <i class="el-icon-shopping-cart-2" style="font-size: 80px; color: #ddd;"></i>
          <div style="margin-top: 20px; font-size: 16px;">购物车空空如也</div>
          <el-button type="primary" style="margin-top: 20px;" @click="goShopping">去逛逛</el-button>
        </div>

        <!-- 购物车表格 -->
        <el-table v-else :data="cartItems" border stripe @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="55"></el-table-column>
          <el-table-column label="商品信息" width="320">
            <template slot-scope="scope">
              <div style="display: flex; align-items: center; gap: 10px;">
                <el-image
                  v-if="scope.row.cover"
                  style="width: 70px; height: 70px; border-radius: 4px;"
                  :src="scope.row.cover"
                  fit="cover"
                  :preview-src-list="[scope.row.cover]">
                </el-image>
                <div style="flex: 1; overflow: hidden;">
                  <div style="font-size: 14px; margin-bottom: 5px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap;">
                    {{ scope.row.name }}
                  </div>
                  <div style="color: #999; font-size: 12px;">
                    库存：{{ scope.row.store || 999 }}
                  </div>
                </div>
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="price" label="单价" width="100">
            <template slot-scope="scope">
              <span style="color: #ff6700; font-weight: bold;">¥{{ scope.row.price }}</span>
            </template>
          </el-table-column>
          <el-table-column label="数量" width="150">
            <template slot-scope="scope">
              <el-input-number
                v-model="scope.row.nums"
                :min="1"
                :max="scope.row.store || 999"
                size="small"
                @change="updateCartItem(scope.row)">
              </el-input-number>
            </template>
          </el-table-column>
          <el-table-column label="小计" width="120">
            <template slot-scope="scope">
              <span style="color: #ff6700; font-weight: bold; font-size: 16px;">
                ¥{{ (scope.row.price * scope.row.nums).toFixed(2) }}
              </span>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="100" fixed="right">
            <template slot-scope="scope">
              <el-button type="danger" size="small" @click="removeFromCart(scope.row.id)">
                <i class="el-icon-delete"></i> 删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-card>

      <!-- 右侧结算栏 -->
      <div style="flex: 1;">
        <el-card style="position: sticky; top: 10px;">
          <div style="font-weight: bold; font-size: 16px; margin-bottom: 20px;">
            <i class="el-icon-s-order"></i> 结算明细
          </div>

          <div v-if="selectedItems.length === 0" style="text-align: center; padding: 30px 0; color: #999;">
            <i class="el-icon-shopping-bag-1" style="font-size: 48px; color: #ddd;"></i>
            <div style="margin-top: 10px;">请选择要结算的商品</div>
          </div>

          <div v-else>
            <!-- 已选商品列表 -->
            <div style="max-height: 300px; overflow-y: auto; margin-bottom: 15px;">
              <div v-for="item in selectedItems" :key="item.id"
                   style="display: flex; justify-content: space-between; margin-bottom: 10px; font-size: 14px; padding-bottom: 10px; border-bottom: 1px dashed #eee;">
                <span style="flex: 1; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; margin-right: 10px;">
                  {{ item.name }} × {{ item.nums }}
                </span>
                <span style="color: #ff6700; font-weight: bold;">
                  ¥{{ (item.price * item.nums).toFixed(2) }}
                </span>
              </div>
            </div>

            <el-divider></el-divider>

            <!-- 金额统计 -->
            <div style="margin-bottom: 20px;">
              <div style="display: flex; justify-content: space-between; margin-bottom: 10px; font-size: 14px;">
                <span>商品总额:</span>
                <span style="color: #ff6700; font-weight: bold;">¥{{ totalPrice }}</span>
              </div>
              <div style="display: flex; justify-content: space-between; margin-bottom: 10px; font-size: 14px;">
                <span>优惠减免:</span>
                <span style="color: #67c23a; font-weight: bold;">-¥0.00</span>
              </div>
            </div>

            <el-divider></el-divider>

            <!-- 合计 -->
            <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; padding: 15px; background-color: #fff5f0; border-radius: 8px;">
              <span style="font-size: 16px; font-weight: bold;">应付合计:</span>
              <span style="color: #ff5000; font-size: 28px; font-weight: bold;">¥{{ totalPrice }}</span>
            </div>

            <!-- 结算按钮 -->
            <el-button
              type="warning"
              style="width: 100%; background-color: #ff5000; border-color: #ff5000; height: 50px; font-size: 18px;"
              @click="openCheckoutDialog"
              :disabled="selectedItems.length === 0">
              <i class="el-icon-wallet"></i> 立即结算 ({{ selectedItems.length }})
            </el-button>
          </div>
        </el-card>

        <!-- 购物车统计 -->
        <el-card style="margin-top: 20px;">
          <div style="font-size: 14px; color: #666;">
            <div style="margin-bottom: 8px;">
              <i class="el-icon-data-line"></i> 购物车统计
            </div>
            <div style="display: flex; justify-content: space-between; margin-bottom: 5px;">
              <span>已选商品:</span>
              <span style="color: #ff6700; font-weight: bold;">{{ selectedCount }} 件</span>
            </div>
            <div style="display: flex; justify-content: space-between;">
              <span>购物车总数:</span>
              <span style="color: #909399;">{{ cartItems.length }} 件</span>
            </div>
          </div>
        </el-card>
      </div>
    </div>

    <!-- 结算确认对话框 -->
    <el-dialog title="确认订单" :visible.sync="checkoutDialogVisible" width="700px" :close-on-click-modal="false">
      <el-form ref="addressForm" :model="addressForm" :rules="addressRules" label-width="100px">
        <!-- 收货地址 -->
        <div style="background-color: #f5f5f5; padding: 15px; border-radius: 8px; margin-bottom: 20px;">
          <div style="font-weight: bold; margin-bottom: 10px;">
            <i class="el-icon-location-outline"></i> 收货地址
          </div>
          <el-form-item label="收货人" prop="name">
            <el-input v-model="addressForm.name" placeholder="请输入收货人姓名"></el-input>
          </el-form-item>
          <el-form-item label="手机号" prop="phone">
            <el-input v-model="addressForm.phone" placeholder="请输入手机号"></el-input>
          </el-form-item>
          <el-form-item label="详细地址" prop="address">
            <el-input type="textarea" v-model="addressForm.address" placeholder="请输入详细地址" rows="3"></el-input>
          </el-form-item>
        </div>

        <!-- 订单信息 -->
        <div style="margin-bottom: 20px;">
          <div style="font-weight: bold; margin-bottom: 10px;">
            <i class="el-icon-goods"></i> 订单信息
          </div>
          <div v-for="item in selectedItems" :key="item.id"
               style="display: flex; align-items: center; gap: 10px; padding: 10px; border-bottom: 1px solid #eee;">
            <el-image
              v-if="item.goods && item.goods.cover"
              style="width: 60px; height: 60px; border-radius: 4px;"
              :src="item.goods.cover"
              fit="cover">
            </el-image>
            <div style="flex: 1;">
              <div style="font-size: 14px;">{{ item.name }}</div>
              <div style="color: #999; font-size: 12px;">数量：{{ item.nums }}</div>
            </div>
            <div style="color: #ff6700; font-weight: bold;">¥{{ (item.price * item.nums).toFixed(2) }}</div>
          </div>
        </div>

        <!-- 支付方式 -->
        <div style="margin-bottom: 20px;">
          <div style="font-weight: bold; margin-bottom: 10px;">
            <i class="el-icon-wallet"></i> 支付方式
          </div>
          <el-radio-group v-model="payMethod">
            <el-radio label="balance">余额支付 (可用：¥{{ user.account || 0 }})</el-radio>
            <el-radio label="wechat">微信支付</el-radio>
            <el-radio label="alipay">支付宝支付</el-radio>
            <el-radio label="bank">银行卡支付</el-radio>
          </el-radio-group>
        </div>

        <!-- 支付金额 -->
        <div style="display: flex; justify-content: space-between; align-items: center; padding: 15px; background-color: #fff5f0; border-radius: 8px;">
          <span style="font-size: 14px;">应付金额:</span>
          <span style="color: #ff5000; font-size: 28px; font-weight: bold;">¥{{ totalPrice }}</span>
        </div>
      </el-form>

      <div slot="footer">
        <el-button @click="checkoutDialogVisible = false">取消</el-button>
        <el-button type="warning" style="background-color: #ff5000; border-color: #ff5000;" @click="confirmPay">
          确认支付
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "ShoppingCart",
  data() {
    return {
      cartItems: [], // 购物车商品列表
      selectedItems: [], // 已选中的商品
      pageNum: 1,
      pageSize: 100, // 购物车一次加载所有商品
      total: 0,
      user: JSON.parse(localStorage.getItem('user') || '{}'),
      checkoutDialogVisible: false,
      payMethod: 'balance',
      addressForm: {
        name: '',
        phone: '',
        address: ''
      },
      addressRules: {
        name: [
          { required: true, message: '请输入收货人姓名', trigger: 'blur' }
        ],
        phone: [
          { required: true, message: '请输入手机号', trigger: 'blur' },
          { pattern: /^1[3-9]\d{9}$/, message: '手机号格式不正确', trigger: 'blur' }
        ],
        address: [
          { required: true, message: '请输入详细地址', trigger: 'blur' }
        ]
      }
    }
  },
  computed: {
    // 计算总价
    totalPrice() {
      return this.selectedItems.reduce((sum, item) => {
        return sum + (Number(item.price) * Number(item.nums))
      }, 0).toFixed(2)
    },
    // 已选商品数量
    selectedCount() {
      return this.selectedItems.length
    }
  },
  created() {
    this.loadCart()
    this.initAddress()
  },
  methods: {
    /**
     * 加载购物车数据
     */
    loadCart() {
      this.$request.get('/orders/selectPage', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          state: '待付款' // 只显示待付款订单（购物车）
        }
      }).then(res => {
        if (res.code === '200') {
          this.cartItems = res.data?.records || []
          this.total = res.data?.total
        }
      })
    },

    /**
     * 初始化收货地址
     */
    initAddress() {
      this.addressForm.name = this.user.name || ''
      this.addressForm.phone = this.user.phone || ''
      this.addressForm.address = this.user.address || ''
    },

    /**
     * 选择商品
     */
    handleSelectionChange(val) {
      this.selectedItems = val
    },

    /**
     * 更新商品数量
     */
    updateCartItem(item) {
      // 可以在这里调用后端接口更新数量
      // 暂时只在本地更新
    },

    /**
     * 从购物车移除商品
     */
    removeFromCart(id) {
      this.$confirm('确认删除该商品吗？', '提示', { type: 'warning' }).then(() => {
        this.$request.delete(`/orders/delete?id=${id}`).then(res => {
          if (res.code === '200') {
            this.$notify.success({ title: '成功', message: '删除成功', duration: 2000 })
            this.loadCart()
          } else {
            this.$notify.error({ title: '错误', message: res.msg, duration: 2000 })
          }
        })
      }).catch(() => {})
    },

    /**
     * 清空购物车
     */
    clearCart() {
      this.$confirm('确认清空购物车吗？', '警告', {
        type: 'warning',
        confirmButtonText: '确定',
        cancelButtonText: '取消'
      }).then(() => {
        const ids = this.cartItems.map(item => item.id)
        this.$request.post('/orders/batchDelete', { ids }).then(res => {
          if (res.code === '200') {
            this.$notify.success({ title: '成功', message: '清空成功', duration: 2000 })
            this.loadCart()
          } else {
            this.$notify.error({ title: '错误', message: res.msg, duration: 2000 })
          }
        })
      }).catch(() => {})
    },

    /**
     * 打开结算对话框
     */
    openCheckoutDialog() {
      if (this.selectedItems.length === 0) {
        this.$notify.warning({ title: '提示', message: '请选择要结算的商品', duration: 2000 })
        return
      }
      this.loadUserInfo()
      this.initAddress()
      this.checkoutDialogVisible = true
    },

    /**
     * 加载用户最新信息
     */
    loadUserInfo() {
      const oldToken = this.user.token
      this.$request.get(`/user/selectById/${this.user.id}`).then(res => {
        if (res.code === '200') {
          this.user = res.data
          this.user.token = oldToken
          localStorage.setItem('user', JSON.stringify(this.user))
        }
      })
    },

    /**
     * 确认支付
     */
    confirmPay() {
      this.$refs.addressForm.validate(valid => {
        if (!valid) return

        // 余额支付需要检查余额
        if (this.payMethod === 'balance' && this.user.account < this.totalPrice) {
          this.$notify.warning({ title: '提示', message: '余额不足，请充值', duration: 2000 })
          return
        }

        // 非余额支付时提示用户
        if (this.payMethod !== 'balance') {
          const payMethodNames = {
            'wechat': '微信支付',
            'alipay': '支付宝支付',
            'bank': '银行卡支付'
          }
          this.$confirm(`即将跳转至${payMethodNames[this.payMethod]}，请确认订单信息`, '支付提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            this.processPayment()
          }).catch(() => {})
        } else {
          this.processPayment()
        }
      })
    },

    /**
     * 处理支付
     */
    processPayment() {
      const orderIds = this.selectedItems.map(item => item.id)

      console.log('=== 购物车开始处理支付 ===')
      console.log('选择的支付方式:', this.payMethod)
      console.log('订单 IDs:', orderIds)

      // 判断是否是支付宝支付
      if (this.payMethod === 'alipay') {
        console.log('进入支付宝支付分支')

        // 对于支付宝支付，需要先创建订单，然后跳转到第一个订单的支付页面
        this.$request.post('/orders/pay', {
          orderIds: orderIds,
          payMethod: this.payMethod,
          address: this.addressForm
        }).then(res => {
          if (res.code === '200') {
            console.log('订单创建成功，准备跳转支付宝')
            console.log('后端返回的数据:', res.data)

            // 获取新创建的订单 ID
            let firstOrderId = null

            if (res.data) {
              // 如果是数组，取第一个元素的 id
              if (Array.isArray(res.data)) {
                if (res.data.length > 0) {
                  firstOrderId = res.data[0].id
                  console.log('批量订单，第一个订单 ID:', firstOrderId)
                }
              } else if (res.data.id) {
                // 如果是单个对象，直接取 id
                firstOrderId = res.data.id
                console.log('单订单，订单 ID:', firstOrderId)
              }
            }

            if (firstOrderId) {
              console.log('使用订单 ID 调用支付宝接口:', firstOrderId)

              // 调用支付宝接口生成支付页面
              this.$request.post('/orders/alipay/web', { id: firstOrderId }, { responseType: 'text' }).then(alipayRes => {
                console.log('支付宝接口响应:', alipayRes)

                if (alipayRes.code === '200' && alipayRes.data) {
                  console.log('支付宝 HTML 长度:', alipayRes.data.length)

                  // 打开新窗口显示支付宝支付页面
                  const win = window.open('', '_blank')
                  console.log('新窗口对象:', win)

                  if (win) {
                    try {
                      // 先写入空白页面
                      win.document.write('<html><head><title>支付宝支付</title></head><body></body></html>')
                      win.document.close()

                      // 然后写入表单
                      win.document.write(alipayRes.data)
                      console.log('✅ 已写入 HTML 到新窗口')

                      this.checkoutDialogVisible = false
                      this.loadCart()
                    } catch (writeError) {
                      console.error('❌ 写入窗口时出错:', writeError)
                      this.$notify.error({title: '错误', message: '写入支付页面失败：' + writeError.message, showClose: false, duration: 5000})
                    }
                  } else {
                    console.error('❌ 窗口打开失败或被浏览器拦截')
                    this.$notify.error({title: '错误', message: '无法打开支付窗口，请检查浏览器是否拦截弹窗', showClose: false, duration: 5000})
                  }
                } else {
                  console.error('❌ 支付宝接口返回错误:', alipayRes.msg)
                  this.$notify.error({title: '失败', message: alipayRes.msg || '支付宝支付失败', showClose: false, duration: 3000})
                }
              }).catch(err => {
                console.error('❌ 支付宝接口调用失败:', err)
                this.$notify.error({title: '错误', message: '支付宝支付失败：' + (err.response?.data?.msg || err.message), showClose: false, duration: 5000})
              })
            } else {
              console.error('❌ 未找到创建的订单 ID，响应数据:', res.data)
              this.$notify.error({title: '错误', message: '订单创建失败：未获取到订单 ID', showClose: false, duration: 3000})
            }
          } else {
            console.error('❌ 订单创建失败:', res.msg)
            this.$notify.error({title: '失败', message: res.msg, showClose: false, duration: 3000})
          }
        }).catch(err => {
          console.error('❌ 订单创建异常:', err)
          this.$notify.error({title: '错误', message: '订单创建失败：' + (err.response?.data?.msg || err.message), showClose: false, duration: 5000})
        })
      } else {
        // 其他支付方式（余额、微信、银行卡）
        console.log('进入其他支付方式分支:', this.payMethod)

        this.$request.post('/orders/pay', {
          orderIds: orderIds,
          payMethod: this.payMethod,
          address: this.addressForm
        }).then(res => {
          if (res.code === '200') {
            this.$notify.success({ title: '成功', message: '支付成功', duration: 2000 })
            this.checkoutDialogVisible = false
            this.loadCart()

            // 如果是非余额支付，模拟跳转第三方支付
            if (this.payMethod !== 'balance') {
              this.$message.success('正在跳转至支付平台...')
            }
          } else {
            this.$notify.error({ title: '失败', message: res.msg, duration: 2000 })
          }
        }).catch(err => {
          this.$notify.error({ title: '错误', message: '支付失败', duration: 2000 })
        })
      }
    },

    /**
     * 去购物
     */
    goShopping() {
      this.$router.push('/front/goods')
    }
  }
}
</script>

<style scoped>
.shopping-cart-container {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: calc(100vh - 120px);
}

.content-wrapper {
  display: flex;
  gap: 20px;
}

.table-card {
  flex: 1;
}

/* 响应式适配 */
@media (max-width: 768px) {
  .content-wrapper {
    flex-direction: column;
  }

  .table-card {
    margin-right: 0 !important;
  }
}
</style>
