import Vue from 'vue'  // 导入Vue2
import App from './App.vue'  // 导入根组件App（应用入口组件）
import router from './router'  // 导入路由实例（配置好的页面路由规则）
import ElementUI from 'element-ui';  // 导入Element UI组件库（Vue2兼容的UI框架）
import 'element-ui/lib/theme-chalk/index.css';  // 导入Element UI的默认样式文件
import '@/assets/css/global.css'  // 导入全局样式文件（自定义的全局CSS样式）
import request from "@/utils/request";  // 导入自定义的axios实例（封装好的请求工具）

// 关闭Vue的生产提示
Vue.config.productionTip = false

// 安装路由插件
Vue.use(router)
// 安装Element UI插件，并设置组件尺寸
Vue.use(ElementUI, {size: 'small'})

// 将自定义axios实例挂载到Vue原型，全局可通过this.$request调用
Vue.prototype.$request = request
// 将后端基础地址挂载到Vue原型，全局可通过this.$baseUrl获取
Vue.prototype.$baseUrl = 'http://localhost:9999'

// 创建Vue2应用实例并挂载
new Vue({
  router,
  render: h => h(App),
}).$mount('#app')
