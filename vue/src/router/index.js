import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

// 解决重复点击路由报错问题
const originalPush = Router.prototype.push
Router.prototype.push = function push(location) {
  return originalPush.call(this, location).catch(err => err)
}

const routes = [
  {
    path: '/',
    name: 'Manager',
    component: () => import(/* webpackChunkName: "manager" */ '../views/Manager.vue')
      .catch(() => import('../views/Error.vue')),
    redirect: '/home',
    children: [
      { path: '403', name: 'Auth', meta: { name: '无权限' }, component: () => import('../views/manager/Auth.vue') },
      { path: 'home', name: 'ManagerHome', meta: { name: '系统首页' }, component: () => import('../views/manager/Home.vue') },
      { path: 'admin', name: 'Admin', meta: { name: '管理员信息', requireAdmin: true }, component: () => import('../views/manager/Admin.vue') },
      { path: 'user', name: 'User', meta: { name: '用户信息', requireAdmin: true }, component: () => import('../views/manager/User.vue') },
      { path: 'person', name: 'person', meta: { name: '个人信息' }, component: () => import('../views/manager/Person.vue') },
      { path: 'type', name: 'type', meta: { name: '商品分类信息' }, component: () => import('../views/manager/Type.vue') },
      { path: 'goods', name: 'goods', meta: { name: '商品信息' }, component: () => import('../views/manager/Goods.vue') },
      { path: 'carousel', name: 'Carousel', meta: { name: '轮播图信息' }, component: () => import('../views/manager/Carousel.vue') },
      { path: 'promotionImage', name: 'PromotionImage', meta: { name: '宣传图管理' }, component: () => import('../views/manager/PromotionImage.vue') },
      { path: 'collect', name: 'Collect', meta: { name: '收藏信息' }, component: () => import('../views/manager/Collect.vue') },
      { path: 'orders', name: 'Orders', meta: { name: '订单信息' }, component: () => import('../views/manager/Orders.vue') },
      { path: 'afterSales', name: 'AfterSales', meta: { name: '售后管理' }, component: () => import('../views/manager/AfterSales.vue') },
    ]
  },
  {
    path: '/front',
    name: 'Front',
    component: () => import('../views/front/Front.vue'),
    redirect: '/front/home',
    children: [
      { path: 'home', name: 'FrontHome', meta: { name: '首页信息' }, component: () => import(/* webpackChunkName: "front-home" */ '../views/front/Home.vue').catch(() => import('../views/Error.vue')) },
      { path: 'goods', name: 'FrontGoods', meta: { name: '全部商品' }, component: () => import(/* webpackChunkName: "front-goods" */ '../views/front/Goods.vue').catch(() => import('../views/Error.vue')) },
      { path: 'goodsDetail/:id', name: 'GoodsDetail', meta: { name: '商品详情' }, component: () => import(/* webpackChunkName: "front-goods-detail" */ '../views/front/GoodsDetail.vue').catch(() => import('../views/Error.vue')) },
      { path: 'collect', name: 'FrontCollect', meta: { name: '我的收藏' }, component: () => import(/* webpackChunkName: "front-collect" */ '../views/front/Collect.vue').catch(() => import('../views/Error.vue')) },
      { path: 'shopping-cart', name: 'ShoppingCart', meta: { name: '购物车' }, component: () => import(/* webpackChunkName: "shopping-cart" */ '../views/front/ShoppingCart.vue').catch(() => import('../views/Error.vue')) },
      { path: 'orders', name: 'FrontOrders', meta: { name: '我的订单' }, component: () => import(/* webpackChunkName: "front-orders" */ '../views/front/Orders.vue').catch(() => import('../views/Error.vue')) },
      { path: 'afterSales', name: 'FrontAfterSales', meta: { name: '售后服务' }, component: () => import(/* webpackChunkName: "front-after-sales" */ '../views/front/AfterSales.vue').catch(() => import('../views/Error.vue')) },
      { path: 'person', name: 'FrontPerson', meta: { name: '个人信息' }, component: () => import(/* webpackChunkName: "front-person" */ '../views/front/Person.vue').catch(() => import('../views/Error.vue')) },
      { path: 'password', name: 'Password', meta: { name: '修改密码' }, component: () => import(/* webpackChunkName: "front-password" */ '../views/front/Password.vue').catch(() => import('../views/Error.vue')) },
    ]
  },
  { path: '/login', name: 'Login', meta: { name: '登录' }, component: () => import('../views/Login.vue') },
  { path: '/register', name: 'Register', meta: { name: '注册' }, component: () => import('../views/Register.vue') },
  { path: '*', name: 'page-404', meta: { name: '无法访问' }, component: () => import('../views/404.vue')},
]

const router = new Router({
  mode: 'hash',
  routes
})

// 全局前置守卫
router.beforeEach((to, from, next) => {
  const user = JSON.parse(localStorage.getItem('user') || '{}')
  if (to.path === '/'){
    if (user.role){
      if (user.role === 'ADMIN'){
        next('/home')
      } else {
        next('/front/home')
      }
    } else {
      next('/login')
    }
  } else {
    if (to.matched.length === 0){
      next('/404')
      return
    } else {
      next()
    }
  }
})

export default router
