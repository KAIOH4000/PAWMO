<template>
  <div class="error-container">
    <el-result icon="warning" title="页面加载失败">
      <template slot="extra">
        <el-button type="primary" @click="handleRetry">
          {{ retryTip }}
        </el-button>
        <div v-if="isDevMode" class="debug-info">
          <p>错误类型: {{ errorType }}</p>
          <p>组件: {{ failedComponent }}</p>
          <el-button size="small" @click="showDetails = !showDetails">
            {{ showDetails ? '隐藏详情' : '显示详情' }}
          </el-button>
          <pre v-if="showDetails">{{ errorDetails }}</pre>
        </div>
      </template>
    </el-result>
  </div>
</template>

<script>
export default {
  data() {
    return {
      retryCount: 0,
      errorType: 'ChunkLoadError',
      failedComponent: this.$route.name || '未知',
      errorDetails: '',
      showDetails: false
    }
  },
  computed: {
    isDevMode() {
      return process.env.NODE_ENV === 'development'
    },
    retryTip() {
      return this.retryCount < 3 ? `重新加载 (${this.retryCount}/3)` : '联系管理员'
    }
  },
  methods: {
    handleRetry() {
      if (this.retryCount < 3) {
        this.retryCount++
        window.location.reload()
      } else {
        window.open('mailto:support@petshop.com')
      }
    },
    captureError() {
      if (window.__CHUNK_ERROR__) {
        this.errorDetails = JSON.stringify(window.__CHUNK_ERROR__, null, 2)
        this.errorType = window.__CHUNK_ERROR__.name
      }
    }
  },
  mounted() {
    this.captureError()
  }
}
</script>

<style scoped>
.error-container {
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
}
.debug-info {
  margin-top: 20px;
  padding: 15px;
  background: #f8f8f8;
  border-radius: 4px;
}
pre {
  max-height: 300px;
  overflow: auto;
}
</style>