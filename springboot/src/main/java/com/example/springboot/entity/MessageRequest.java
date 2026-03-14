package com.example.springboot.entity;

import lombok.Data;

/**
 * AI 消息请求实体
 */
@Data
public class MessageRequest {
    /**
     * 会话 ID
     */
    private String sessionId;
    
    /**
     * 用户消息内容
     */
    private String message;
}
