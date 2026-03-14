package com.example.springboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.springboot.entity.ProductReview;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 商品评价 Mapper 接口
 * 
 * @author System
 * @since 2025-03-14
 */
public interface ReviewMapper extends BaseMapper<ProductReview> {

    /**
     * 根据商品ID查询所有评价（包含用户信息）
     * 按时间倒序排列
     *
     * @param productId 商品ID
     * @return 评价列表
     */
    @Select("SELECT r.*, u.username, u.avatar as userAvatar " +
            "FROM product_review r " +
            "LEFT JOIN user u ON r.user_id = u.id " +
            "WHERE r.product_id = #{productId} " +
            "AND r.is_deleted = 0 " +
            "ORDER BY r.create_time DESC")
    List<ProductReview> selectReviewsWithUserByProductId(@Param("productId") Integer productId);

    /**
     * 查询用户是否已评价过某订单的商品
     *
     * @param userId    用户ID
     * @param orderId   订单ID
     * @param productId 商品ID
     * @return 评价记录，不存在返回null
     */
    @Select("SELECT * FROM product_review " +
            "WHERE user_id = #{userId} " +
            "AND order_id = #{orderId} " +
            "AND product_id = #{productId} " +
            "AND is_deleted = 0 " +
            "LIMIT 1")
    ProductReview selectByUserAndOrder(@Param("userId") Integer userId,
                                        @Param("orderId") Integer orderId,
                                        @Param("productId") Integer productId);

    /**
     * 查询某商品的评价数量
     *
     * @param productId 商品ID
     * @return 评价数量
     */
    @Select("SELECT COUNT(*) FROM product_review " +
            "WHERE product_id = #{productId} AND is_deleted = 0")
    Integer countByProductId(@Param("productId") Integer productId);

    /**
     * 查询某商品的平均评分
     *
     * @param productId 商品ID
     * @return 平均评分
     */
    @Select("SELECT AVG(score) FROM product_review " +
            "WHERE product_id = #{productId} AND is_deleted = 0")
    Double selectAvgScoreByProductId(@Param("productId") Integer productId);

    /**
     * 查询用户对某订单的所有评价
     *
     * @param userId  用户 ID
     * @param orderId 订单 ID
     * @return 评价记录列表
     */
    @Select("SELECT * FROM product_review " +
            "WHERE user_id = #{userId} " +
            "AND order_id = #{orderId} " +
            "AND is_deleted = 0")
    List<ProductReview> selectReviewsByUserIdAndOrderId(@Param("userId") Integer userId,
                                                         @Param("orderId") Integer orderId);
}
