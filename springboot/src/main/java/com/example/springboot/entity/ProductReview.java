package com.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 商品评价实体类
 * 对应数据库表: product_review
 * 
 * @author System
 * @since 2025-03-14
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("product_review")
public class ProductReview {

    /**
     * 评价ID，主键自增
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 商品ID，关联goods表
     */
    private Integer productId;

    /**
     * 用户ID，关联user表
     */
    private Integer userId;

    /**
     * 订单ID，关联orders表，用于验证购买记录
     */
    private Integer orderId;

    /**
     * 评分，1-5星
     */
    private Integer score;

    /**
     * 评价内容，最多500字
     */
    private String content;

    /**
     * 是否管理员回复：0-否，1-是
     */
    private Integer isAdminReply;

    /**
     * 管理员回复内容
     */
    private String adminReplyContent;

    /**
     * 管理员回复时间
     */
    private LocalDateTime adminReplyTime;

    /**
     * 评价创建时间
     */
    private LocalDateTime createTime;

    /**
     * 评价更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 是否删除：0-正常，1-已删除
     */
    private Integer isDeleted;

    // ==================== 关联查询字段（非数据库字段） ====================

    /**
     * 用户名称（关联查询）
     */
    @TableField(exist = false)
    private String username;

    /**
     * 用户头像（关联查询）
     */
    @TableField(exist = false)
    private String userAvatar;

    /**
     * 商品名称（关联查询）
     */
    @TableField(exist = false)
    private String productName;
}
