package com.example.springboot.controller;

import cn.hutool.core.util.StrUtil;
import com.example.springboot.common.Result;
import com.example.springboot.service.AiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/ai")
public class AiController {

    @Autowired
    private AiService aiService;
    
    private static final Logger logger = LoggerFactory.getLogger(AiController.class);

    @PostMapping("/chat")
    public Result chat(@RequestBody Map<String, String> request) {
        String question = request.get("question");
        if (StrUtil.isBlank(question)) {
            return Result.error("400", "问题不能为空");
        }
        
        logger.info("=== AI Controller 收到问题：{} ===", question);
        
        try {
            String answer = aiService.getAnswer(question);
            logger.info("=== AI Controller 获取到答案：{} ===", answer != null ? answer.substring(0, Math.min(50, answer.length())) + "..." : "null");
            Result result = Result.success(answer);
            logger.info("=== AI Controller 准备返回 Result: code={}, msg={} ===", result.getCode(), result.getMsg());
            return result;
        } catch (Exception e) {
            logger.error("=== AI Controller 处理问题时发生异常 ===", e);
            return Result.error("500", "处理失败：" + e.getMessage());
        }
    }
}