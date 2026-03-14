package com.example.springboot.service.impl;

import com.example.springboot.entity.Orders;
import com.example.springboot.entity.ProductReview;
import com.example.springboot.exception.ServiceException;
import com.example.springboot.mapper.OrdersMapper;
import com.example.springboot.mapper.ReviewMapper;
import com.example.springboot.service.IReviewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * 商品评价 Service 实现类
 * 
 * @author System
 * @since 2025-03-14
 */
@Slf4j
@Service
public class ReviewServiceImpl implements IReviewService {

    @Autowired
    private ReviewMapper reviewMapper;

    @Autowired
    private OrdersMapper ordersMapper;

    /**
     * 根据商品ID查询所有评价（包含用户信息）
     * 按时间倒序排列
     *
     * @param productId 商品ID
     * @return 评价列表
     */
    @Override
    public List<ProductReview> getReviewsByProductId(Integer productId) {
        log.info("查询商品评价列表, productId: {}", productId);
        
        if (productId == null) {
            throw new ServiceException("400", "商品ID不能为空");
        }
        
        List<ProductReview> reviews = reviewMapper.selectReviewsWithUserByProductId(productId);
        log.info("查询到 {} 条评价记录", reviews.size());
        
        return reviews;
    }

    /**
     * 添加商品评价
     * 业务逻辑：
     * 1. 验证用户是否登录
     * 2. 验证评分是否在1-5之间
     * 3. 验证用户是否购买过该商品（通过订单验证）
     * 4. 验证用户是否已评价过该订单的该商品（防止重复评价）
     * 5. 保存评价记录
     *
     * @param review 评价对象
     * @param userId 当前登录用户ID
     * @return 评价ID
     * @throws ServiceException 业务校验失败时抛出
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer addReview(ProductReview review, Integer userId) {
        log.info("添加商品评价, userId: {}, productId: {}, orderId: {}", userId, review.getProductId(), review.getOrderId());
        
        // 1. 验证用户是否登录
        if (userId == null) {
            log.warn("用户未登录，无法提交评价");
            throw new ServiceException("401", "请先登录后再评价");
        }
        
        // 2. 验证评价对象
        validateReview(review);
        
        // 3. 验证用户是否购买过该商品
        validateUserPurchase(userId, review.getProductId(), review.getOrderId());
        
        // 4. 验证是否已评价过（防止重复评价）
        validateNotDuplicateReview(userId, review.getOrderId(), review.getProductId());
        
        // 5. 设置评价用户信息
        review.setUserId(userId);
        review.setIsAdminReply(0);
        review.setIsDeleted(0);
        
        // 6. 保存评价
        int result = reviewMapper.insert(review);
        if (result <= 0) {
            log.error("评价保存失败, userId: {}, productId: {}", userId, review.getProductId());
            throw new ServiceException("500", "评价保存失败");
        }
        
        log.info("评价提交成功, reviewId: {}", review.getId());
        return review.getId();
    }

    /**
     * 验证评价对象的有效性
     *
     * @param review 评价对象
     */
    private void validateReview(ProductReview review) {
        // 验证商品ID
        if (review.getProductId() == null) {
            throw new ServiceException("400", "商品ID不能为空");
        }
        
        // 验证订单ID
        if (review.getOrderId() == null) {
            throw new ServiceException("400", "订单ID不能为空");
        }
        
        // 验证评分范围
        if (review.getScore() == null || review.getScore() < 1 || review.getScore() > 5) {
            throw new ServiceException("400", "评分必须在1-5星之间");
        }
        
        // 验证评价内容
        if (review.getContent() == null || review.getContent().trim().isEmpty()) {
            throw new ServiceException("400", "评价内容不能为空");
        }
        
        // 验证评价内容长度
        if (review.getContent().length() > 500) {
            throw new ServiceException("400", "评价内容不能超过500字");
        }
    }

    /**
     * 验证用户是否购买过该商品
     *
     * @param userId    用户ID
     * @param productId 商品ID
     * @param orderId   订单ID
     * @throws ServiceException 未购买时抛出异常
     */
    private void validateUserPurchase(Integer userId, Integer productId, Integer orderId) {
        // 查询订单是否存在且属于当前用户
        Orders order = ordersMapper.selectById(orderId);
        
        if (order == null) {
            log.warn("订单不存在, orderId: {}", orderId);
            throw new ServiceException("403", "订单不存在，无法评价");
        }
        
        // 验证订单是否属于当前用户
        if (!Objects.equals(order.getUserId(), userId)) {
            log.warn("订单不属于当前用户, orderId: {}, orderUserId: {}, currentUserId: {}", 
                    orderId, order.getUserId(), userId);
            throw new ServiceException("403", "您无权评价该订单");
        }
        
        // 验证订单中的商品是否匹配
        if (!Objects.equals(order.getGoodsId(), productId)) {
            log.warn("订单商品不匹配, orderId: {}, orderGoodsId: {}, reviewProductId: {}", 
                    orderId, order.getGoodsId(), productId);
            throw new ServiceException("403", "订单商品信息不匹配");
        }
        
        // 验证订单状态是否已完成（已支付、未发货、已发货、已到货、已完成都可以评价）
        if (!"已支付".equals(order.getState()) && 
            !"未发货".equals(order.getState()) && 
            !"已发货".equals(order.getState()) && 
            !"已到货".equals(order.getState()) && 
            !"已完成".equals(order.getState())) {
            log.warn("订单未完成支付，orderId: {}, state: {}", orderId, order.getState());
            throw new ServiceException("403", "订单状态为 [" + order.getState() + "],无法评价，请先完成支付");
        }
        
        log.info("用户购买验证通过, userId: {}, productId: {}", userId, productId);
    }

    /**
     * 验证用户是否已评价过该订单的该商品（防止重复评价）
     *
     * @param userId    用户ID
     * @param orderId   订单ID
     * @param productId 商品ID
     * @throws ServiceException 已评价时抛出异常
     */
    private void validateNotDuplicateReview(Integer userId, Integer orderId, Integer productId) {
        ProductReview existingReview = reviewMapper.selectByUserAndOrder(userId, orderId, productId);
        
        if (existingReview != null) {
            log.warn("用户已评价过该订单商品, userId: {}, orderId: {}, productId: {}", 
                    userId, orderId, productId);
            throw new ServiceException("403", "您已评价过该商品，不能重复评价");
        }
    }

    /**
     * 查询某商品的评价数量
     *
     * @param productId 商品ID
     * @return 评价数量
     */
    @Override
    public Integer getReviewCount(Integer productId) {
        if (productId == null) {
            return 0;
        }
        Integer count = reviewMapper.countByProductId(productId);
        return count != null ? count : 0;
    }

    /**
     * 查询某商品的平均评分
     *
     * @param productId 商品ID
     * @return 平均评分，无评价返回null
     */
    @Override
    public Double getAverageScore(Integer productId) {
        if (productId == null) {
            return null;
        }
        return reviewMapper.selectAvgScoreByProductId(productId);
    }

    /**
     * 删除评价（逻辑删除）
     *
     * @param reviewId 评价ID
     * @param userId   当前登录用户ID（用于权限校验）
     * @return 是否删除成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteReview(Integer reviewId, Integer userId) {
        log.info("删除评价, reviewId: {}, userId: {}", reviewId, userId);
        
        if (reviewId == null) {
            throw new ServiceException("400", "评价ID不能为空");
        }
        
        if (userId == null) {
            throw new ServiceException("401", "请先登录");
        }
        
        // 查询评价是否存在
        ProductReview review = reviewMapper.selectById(reviewId);
        if (review == null || review.getIsDeleted() == 1) {
            throw new ServiceException("404", "评价不存在");
        }
        
        // 验证权限：只能删除自己的评价（管理员除外，此处简化处理）
        if (!Objects.equals(review.getUserId(), userId)) {
            log.warn("无权删除他人评价, reviewId: {}, reviewUserId: {}, currentUserId: {}", 
                    reviewId, review.getUserId(), userId);
            throw new ServiceException("403", "您无权删除该评价");
        }
        
        // 逻辑删除
        review.setIsDeleted(1);
        int result = reviewMapper.updateById(review);
        
        if (result > 0) {
            log.info("评价删除成功, reviewId: {}", reviewId);
            return true;
        } else {
            log.error("评价删除失败, reviewId: {}", reviewId);
            return false;
        }
    }
}
