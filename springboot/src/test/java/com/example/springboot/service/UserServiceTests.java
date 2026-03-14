package com.example.springboot.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.springboot.entity.User;
import com.example.springboot.exception.ServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 用户服务单元测试
 */
@SpringBootTest
@ActiveProfiles("test")
class UserServiceTests {

    @Autowired
    private IUserService userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setPassword("123456");
        testUser.setName("Test User");
        testUser.setEmail("test@example.com");
        testUser.setPhone("13800138000");
        testUser.setRole("USER");
        testUser.setAccount(0.0);
    }

    @Test
    void testRegisterSuccess() {
        // 清理现有数据
        userService.remove(new QueryWrapper<User>().eq("username", "testuser_register"));
        
        // 新建用户
        User user = new User();
        user.setUsername("testuser_register");
        user.setPassword("123456");
        user.setName("New User");
        user.setEmail("newuser@example.com");
        user.setPhone("13800138001");
        user.setRole("USER");
        
        User result = userService.register(user);
        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals("testuser_register", result.getUsername());
    }

    @Test
    void testLoginSuccess() {
        User user = new User();
        user.setUsername("admin");
        user.setPassword("123");
        
        User result = userService.login(user);
        assertNotNull(result);
        assertEquals("admin", result.getUsername());
        assertNotNull(result.getToken());
    }

    @Test
    void testLoginFailure() {
        User user = new User();
        user.setUsername("admin");
        user.setPassword("wrongpassword");
        
        assertThrows(ServiceException.class, () -> {
            userService.login(user);
        });
    }

    @Test
    void testRegisterPhoneDuplicate() {
        // 清理
        userService.remove(new QueryWrapper<User>().eq("username", "testuser_phone_dup1"));
        userService.remove(new QueryWrapper<User>().eq("username", "testuser_phone_dup2"));

        // 第一次注册
        User user1 = new User();
        user1.setUsername("testuser_phone_dup1");
        user1.setPassword("123456");
        user1.setPhone("13800138002");
        user1.setRole("USER");
        userService.register(user1);

        // 第二次使用同一手机号注册
        User user2 = new User();
        user2.setUsername("testuser_phone_dup2");
        user2.setPassword("123456");
        user2.setPhone("13800138002");
        user2.setRole("USER");

        assertThrows(ServiceException.class, () -> {
            userService.register(user2);
        });
    }

    @Test
    void testUpdatePassword() {
        User user = new User();
        user.setUsername("tom");
        user.setPassword("123");
        user.setNewPassword("newpassword123");
        
        assertDoesNotThrow(() -> {
            userService.updatePassword(user);
        });
    }

    @Test
    void testResetPassword() {
        User user = new User();
        user.setUsername("jerry");
        user.setEmail("tom@qq.com");
        
        assertDoesNotThrow(() -> {
            userService.resetPassword(user);
        });
    }

    @Test
    void testGetUserById() {
        User user = userService.getById(1);
        assertNotNull(user);
        assertEquals("admin", user.getUsername());
    }

    @Test
    void testUpdateUser() {
        User user = userService.getById(2);
        user.setName("Updated Tom");
        
        boolean result = userService.updateById(user);
        assertTrue(result);
        
        User updatedUser = userService.getById(2);
        assertEquals("Updated Tom", updatedUser.getName());
    }

    @Test
    void testRemoveUser() {
        User user = new User();
        user.setUsername("tempuser");
        user.setPassword("123456");
        user.setName("Temp User");
        user.setEmail("temp@example.com");
        user.setRole("USER");
        
        userService.save(user);
        Integer userId = user.getId();
        
        boolean result = userService.removeById(userId);
        assertTrue(result);
        
        User deletedUser = userService.getById(userId);
        assertNull(deletedUser);
    }

    @Test
    void testUserCount() {
        long count = userService.count();
        assertTrue(count > 0);
    }

}
