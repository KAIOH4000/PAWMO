package com.example.springboot.controller;

import cn.hutool.core.util.StrUtil;
import com.example.springboot.common.Result;
import com.example.springboot.service.AiService;
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

    @PostMapping("/chat")
    public Result chat(@RequestBody Map<String, String> request) {
        String question = request.get("question");
        if (StrUtil.isBlank(question)) {
            return Result.error("400", "问题不能为空");
        }
        String answer = aiService.getAnswer(question);
        return Result.success(answer);
    }
}