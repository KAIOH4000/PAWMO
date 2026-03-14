package com.example.springboot.service;

import com.example.springboot.entity.ProductReview;

import java.util.List;

/**
 * 商品评价 Service 接口
 * 
 * @author System
 * @since 2025-03-14
 */
public interface IReviewService {

    /**
     * 根据商品ID查询所有评价（包含用户信息）
     * 按时间倒序排列
     *
     * @param productId 商品ID
     * @return 评价列表
     */
    List<ProductReview> getReviewsByProductId(Integer productId);

    /**
     * 添加商品评价
     * 业务逻辑：
     * 1. 验证用户是否登录
     * 2. 验证用户是否购买过该商品
     * 3. 验证用户是否已评价过该订单的该商品（防止重复评价）
     * 4. 保存评价记录
     *
     * @param review 评价对象
     * @param userId 当前登录用户ID
     * @return 评价ID
     * @throws RuntimeException 业务校验失败时抛出
     */
    Integer addReview(ProductReview review, Integer userId);

    /**
     * 查询某商品的评价数量
     *
     * @param productId 商品ID
     * @return 评价数量
     */
    Integer getReviewCount(Integer productId);

    /**
     * 查询某商品的平均评分
     *
     * @param productId 商品ID
     * @return 平均评分，无评价返回null
     */
    Double getAverageScore(Integer productId);

    /**
     * 删除评价（逻辑删除）
     *
     * @param reviewId 评价ID
     * @param userId   当前登录用户ID（用于权限校验）
     * @return 是否删除成功
     */
    Boolean deleteReview(Integer reviewId, Integer userId);
}
