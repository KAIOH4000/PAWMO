package com.example.springboot.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.springboot.common.AuthAccess;
import com.example.springboot.common.Result;
import com.example.springboot.entity.User;
import com.example.springboot.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * 功能：提供接口返回数据
 */
@RestController
public class WebController {

    @Autowired
    private IUserService userService;

    @AuthAccess
    @GetMapping("/")
    public Result hello() {
        return Result.success("success");
    }

    /**
     * 登录
     */
    @PostMapping("/login")
    public Result login(@RequestBody User user) {
        if (StrUtil.isBlank(user.getUsername()) || StrUtil.isBlank(user.getPassword())) {
            return Result.error("数据输入不合法");
        }
        user = userService.login(user);
        System.out.println("登录返回用户信息: " + user);
        System.out.println("Token: " + user.getToken());
        return Result.success(user);
    }

    /**
     * 注册
     */
    @PostMapping("/register")
    public Result register(@RequestBody User user) {
        if (StrUtil.isBlank(user.getUsername()) || StrUtil.isBlank(user.getPassword()) || StrUtil.isBlank(user.getPhone())) {
            return Result.error("400", "数据输入不合法");
        }
        if (user.getUsername().length() > 20 || user.getPassword().length() > 20) {
            return Result.error("400", "数据输入不合法");
        }
        // 手机号格式校验
        if (!user.getPhone().matches("^1[3-9]\\d{9}$")) {
            return Result.error("400", "手机号格式不正确");
        }
        if (StrUtil.isBlank(user.getRole())) {
            user.setRole("USER");
        }
        // 防御性校验：再次校验手机号唯一性（控制层保证安全）
        if (userService.getOne(new QueryWrapper<User>().eq("phone", user.getPhone()), false) != null) {
            return Result.error("400", "手机号已存在");
        }
        // 防御性校验：账号唯一性
        if (userService.getOne(new QueryWrapper<User>().eq("username", user.getUsername()), false) != null) {
            return Result.error("400", "用户名已存在");
        }
        user = userService.register(user);
        return Result.success(user);
    }

    /**
     *  重置密码
     */
    @PutMapping("/password")
    public Result password(@RequestBody User user) {
        if (StrUtil.isBlank(user.getUsername()) || StrUtil.isBlank(user.getPhone())) {
            return Result.error("数据输入不合法");
        }
        userService.resetPassword(user);
        return Result.success();
    }

}
