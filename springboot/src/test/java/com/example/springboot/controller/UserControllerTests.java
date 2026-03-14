package com.example.springboot.controller;

import com.alibaba.fastjson.JSON;
import com.example.springboot.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 用户控制器集成测试
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testAddUser() throws Exception {
        User user = new User();
        user.setUsername("newuser123");
        user.setPassword("123456");
        user.setName("New User");
        user.setEmail("newuser@example.com");
        user.setPhone("13800138000");
        user.setRole("USER");

        mockMvc.perform(post("/user/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSON.toJSONString(user)))
                .andExpect(status().isOk());
    }

    @Test
    void testLoginSuccess() throws Exception {
        User user = new User();
        user.setUsername("admin");
        user.setPassword("123");

        mockMvc.perform(post("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSON.toJSONString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").exists());
    }

    @Test
    void testLoginFailure() throws Exception {
        User user = new User();
        user.setUsername("admin");
        user.setPassword("wrongpassword");

        mockMvc.perform(post("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSON.toJSONString(user)))
                .andExpect(status().isOk());
    }

    @Test
    void testGetUserById() throws Exception {
        mockMvc.perform(get("/user/select/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateUser() throws Exception {
        User user = new User();
        user.setId(2);
        user.setName("Updated Tom");
        user.setEmail("tom@example.com");

        mockMvc.perform(put("/user/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSON.toJSONString(user)))
                .andExpect(status().isOk());
    }

    @Test
    void testListUsers() throws Exception {
        mockMvc.perform(get("/user/list/1/5")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteUser() throws Exception {
        mockMvc.perform(delete("/user/delete/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

}
