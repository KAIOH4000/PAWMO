<template>
  <div>
    <!-- 圆形悬浮按钮 -->
    <div class="chat-toggle-btn" @click="toggleChat">
      <i class="el-icon-chat-line-round" v-if="!isOpen"></i>
      <i class="el-icon-close"></i>
    </div>

    <!-- 聊天窗口 -->
    <div class="ai-chat" :class="{ active: isOpen }">
      <div class="chat-header" @click="toggleChat">
        <span>AI 客服</span>
        <i class="el-icon-close"></i>
      </div>
      <div class="chat-body" v-show="isOpen">
        <div class="message-list">
          <div class="message" v-for="(msg, index) in messages" :key="index">
            <div class="message-content" :class="{ 'user': msg.isUser }">
              {{ msg.content }}
            </div>
          </div>
        </div>
        <div class="chat-input">
          <input
              v-model="inputMessage"
              @keyup.enter="sendMessage"
              placeholder="请输入您的问题..."
          />
          <el-button
              type="danger"
              @click="sendMessage"
              :style="{ background: 'linear-gradient(135deg, #ff9a44 0%, #fc6076 100%)', borderColor: 'transparent' }"
          >
            发送
          </el-button>
        </div>
        <div class="common-questions">
          <el-button
              type="text"
              v-for="(question, index) in commonQuestions"
              :key="index"
              @click="sendCommonQuestion(question)"
          >
            {{ question }}
          </el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import request from '@/utils/request'

export default {
  name: 'AiChat',
  data() {
    return {
      isOpen: false,
      inputMessage: '',
      messages: [
        {
          content: '您好！我是宠物用品店的 AI 客服，有什么可以帮助您的吗？',
          isUser: false
        }
      ],
      commonQuestions: [
        '狗粮推荐',
        '如何下单',
        '如何申请售后',
        '营业时间'
      ]
    }
  },
  methods: {
    toggleChat() {
      this.isOpen = !this.isOpen
    },
    sendMessage() {
      if (!this.inputMessage.trim()) return

      // 添加用户消息
      this.messages.push({
        content: this.inputMessage,
        isUser: true
      })

      // 发送请求
      request.post('/ai/chat', {
        question: this.inputMessage
      }).then(res => {
        if (res && (res.code === '200' || res.code === 200)) {
          // 添加 AI 回复
          this.messages.push({
            content: res.data,
            isUser: false
          })
        } else {
          this.messages.push({
            content: '抱歉，我暂时无法回答您的问题，请稍后再试。',
            isUser: false
          })
        }
      }).catch((error) => {
        this.messages.push({
          content: '网络错误，请稍后再试。',
          isUser: false
        })
      })

      // 清空输入框
      this.inputMessage = ''

      // 滚动到底部
      this.$nextTick(() => {
        const messageList = this.$refs.messageList
        if (messageList) {
          messageList.scrollTop = messageList.scrollHeight
        }
      })
    },
    sendCommonQuestion(question) {
      this.inputMessage = question
      this.sendMessage()
    }
  }
}
</script>

<style scoped>
/* 圆形悬浮按钮样式 */
.chat-toggle-btn {
  position: fixed;
  bottom: 30px;
  right: 30px;
  width: 60px;
  height: 60px;
  border-radius: 50%;
  background: linear-gradient(135deg, #ff9a44 0%, #fc6076 100%);
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  box-shadow: 0 4px 12px rgba(255, 154, 68, 0.3);
  transition: all 0.3s cubic-bezier(0.68, -0.55, 0.265, 1.55);
  z-index: 1000;
  font-size: 28px;
  pointer-events: auto;
}

.chat-toggle-btn:hover {
  transform: scale(1.1) rotate(10deg);
  box-shadow: 0 6px 20px rgba(255, 154, 68, 0.5);
  background: linear-gradient(135deg, #ff8a2e 0%, #fb4f6c 100%);
}

.chat-toggle-btn:active {
  transform: scale(0.95);
  box-shadow: 0 2px 8px rgba(255, 154, 68, 0.4);
}

/* 添加脉冲动画效果 */
.chat-toggle-btn::before {
  content: '';
  position: absolute;
  width: 100%;
  height: 100%;
  border-radius: 50%;
  background: inherit;
  opacity: 0.6;
  animation: pulse 2s infinite;
  z-index: -1;
}

@keyframes pulse {
  0% {
    transform: scale(1);
    opacity: 0.6;
  }
  70% {
    transform: scale(1.3);
    opacity: 0;
  }
  100% {
    transform: scale(1);
    opacity: 0;
  }
}

/* 聊天窗口主体 */
.ai-chat {
  position: fixed;
  bottom: 100px;
  right: 30px;
  width: 380px;
  height: 500px;
  background-color: #fff;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
  transition: all 0.3s ease;
  overflow: hidden;
  z-index: 999;
  opacity: 0;
  transform: translateY(20px);
  pointer-events: none;
}

.ai-chat.active {
  opacity: 1;
  transform: translateY(0);
  pointer-events: auto;
}

.chat-header {
  height: 50px;
  background: linear-gradient(135deg, #ff9a44 0%, #fc6076 100%);
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
  cursor: pointer;
  box-shadow: 0 2px 8px rgba(255, 154, 68, 0.2);
}

.chat-header span {
  font-size: 15px;
  font-weight: bold;
}

.chat-body {
  height: calc(100% - 50px);
  display: flex;
  flex-direction: column;
}

.message-list {
  flex: 1;
  padding: 20px;
  overflow-y: auto;
  background-color: #f9f9f9;
  ref: messageList
}

.message {
  margin-bottom: 15px;
  display: flex;
}

.message-content {
  max-width: 80%;
  padding: 10px 15px;
  border-radius: 18px;
  line-height: 1.4;
  font-size: 14px;
}

.message-content.user {
  background: linear-gradient(135deg, #ff9a44 0%, #fc6076 100%);
  color: #fff;
  align-self: flex-end;
  border-bottom-right-radius: 4px;
  box-shadow: 0 2px 6px rgba(255, 154, 68, 0.2);
}

.message-content:not(.user) {
  background-color: #fff;
  color: #606266;
  align-self: flex-start;
  border-bottom-left-radius: 4px;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.05);
  border: 1px solid #eaeaea;
}

.chat-input {
  padding: 15px;
  border-top: 1px solid #ebeef5;
  display: flex;
  gap: 10px;
  background-color: #fff;
}

.chat-input input {
  flex: 1;
  border-radius: 20px;
  border: 1px solid #dcdfe6;
  padding: 8px 15px;
  outline: none;
  transition: border-color 0.3s;
}

.chat-input input:focus {
  border-color: #ff9a44;
}

.chat-input button {
  border-radius: 20px;
  padding: 8px 20px;
  font-size: 14px;
  transition: all 0.3s;
}

.common-questions {
  padding: 10px 15px;
  border-top: 1px solid #ebeef5;
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  background-color: #fafafa;
}

.common-questions .el-button--text {
  color: #ff9a44;
  background-color: #fff7f0;
  border: 1px solid #ffe4d1;
  border-radius: 15px;
  padding: 5px 12px;
  font-size: 13px;
  transition: all 0.3s ease;
}

.common-questions .el-button--text:hover {
  background: linear-gradient(135deg, #ff9a44 0%, #fc6076 100%);
  color: #fff;
  border-color: transparent;
  box-shadow: 0 2px 8px rgba(255, 154, 68, 0.3);
  transform: translateY(-2px);
}
</style>