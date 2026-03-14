<template>
  <div class="ai-chat" :class="{ active: isOpen }">
    <div class="chat-header" @click="toggleChat">
      <span>AI客服</span>
      <i class="el-icon-close" v-if="isOpen"></i>
      <i class="el-icon-chat-line-round" v-else></i>
    </div>
    <div class="chat-body" v-if="isOpen">
      <div class="message-list">
        <div class="message" v-for="(msg, index) in messages" :key="index">
          <div class="message-content" :class="{ 'user': msg.isUser }">
            {{ msg.content }}
          </div>
        </div>
      </div>
      <div class="chat-input">
        <el-input v-model="inputMessage" @keyup.enter="sendMessage" placeholder="请输入您的问题..."></el-input>
        <el-button type="primary" @click="sendMessage">发送</el-button>
      </div>
      <div class="common-questions">
        <el-button type="text" v-for="(question, index) in commonQuestions" :key="index" @click="sendCommonQuestion(question)">{{ question }}</el-button>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  data() {
    return {
      isOpen: false,
      inputMessage: '',
      messages: [
        {
          content: '您好！我是宠物用品店的AI客服，有什么可以帮助您的吗？',
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
      axios.post('/ai/chat', {
        question: this.inputMessage
      }).then(res => {
        if (res.data.code === '200') {
          // 添加AI回复
          this.messages.push({
            content: res.data.data,
            isUser: false
          })
        } else {
          this.messages.push({
            content: '抱歉，我暂时无法回答您的问题，请稍后再试。',
            isUser: false
          })
        }
      }).catch(() => {
        this.messages.push({
          content: '网络错误，请稍后再试。',
          isUser: false
        })
      })

      // 清空输入框
      this.inputMessage = ''

      // 滚动到底部
      this.$nextTick(() => {
        const messageList = this.$el.querySelector('.message-list')
        messageList.scrollTop = messageList.scrollHeight
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
.ai-chat {
  position: fixed;
  bottom: 30px;
  right: 30px;
  width: 350px;
  height: 60px;
  background-color: #fff;
  border-radius: 30px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
  overflow: hidden;
  z-index: 1000;
}

.ai-chat.active {
  height: 500px;
  border-radius: 10px;
}

.chat-header {
  height: 60px;
  background-color: #409eff;
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
  cursor: pointer;
}

.chat-header span {
  font-size: 16px;
  font-weight: bold;
}

.chat-body {
  height: calc(100% - 60px);
  display: flex;
  flex-direction: column;
}

.message-list {
  flex: 1;
  padding: 20px;
  overflow-y: auto;
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
}

.message-content.user {
  background-color: #ecf5ff;
  color: #409eff;
  align-self: flex-end;
  border-bottom-right-radius: 4px;
}

.message-content:not(.user) {
  background-color: #f5f7fa;
  color: #606266;
  align-self: flex-start;
  border-bottom-left-radius: 4px;
}

.chat-input {
  padding: 15px;
  border-top: 1px solid #ebeef5;
  display: flex;
}

.chat-input input {
  flex: 1;
  margin-right: 10px;
}

.common-questions {
  padding: 10px 15px;
  border-top: 1px solid #ebeef5;
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}
</style>