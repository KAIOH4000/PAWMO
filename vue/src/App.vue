<template>
  <div id="app">
    <!-- 页面路由视图 -->
    <router-view/>

    <!-- AI聊天组件的独立容器 -->
    <div class="ai-chat-container">
      <AiChat />
    </div>
  </div>
</template>

<script>
import AiChat from './components/AiChat.vue'

export default {
  name: 'App',
  components: {
    AiChat
  }
}
</script>

<style>
/* 全局样式可以保留 */

/* 为AI聊天组件容器添加样式 */
.ai-chat-container {
  position: fixed; /* 固定在视口位置 */
  right: 20px;     /* 距离右边20px */
  bottom: 20px;    /* 距离底部20px */
  width: auto;     /* 宽度由内容决定 */
  height: auto;    /* 高度由内容决定 */
  z-index: 1000;   /* 确保它在其他内容之上 */
  pointer-events: none; /* 关键：让容器本身不响应鼠标事件 */
}

/* 让容器内的组件可以响应鼠标事件 */
.ai-chat-container > * {
  pointer-events: auto;
}
</style>