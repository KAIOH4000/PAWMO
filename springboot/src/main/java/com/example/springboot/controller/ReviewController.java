package com.example.springboot.controller;

import com.example.springboot.common.Result;
import com.example.springboot.entity.ProductReview;
import com.example.springboot.entity.User;
import com.example.springboot.exception.ServiceException;
import com.example.springboot.mapper.ReviewMapper;
import com.example.springboot.service.IReviewService;
import com.example.springboot.utils.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商品评价 Controller
 * 提供商品评价的查询和提交接口
 * 
 * @author System
 * @since 2025-03-14
 */
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/product")
public class ReviewController {

    @Autowired
    private IReviewService reviewService;

    @Autowired
    private ReviewMapper reviewMapper;

    /**
     * 查询商品评价列表
     * GET /product/reviews/{productId}
     * 
     * @param productId 商品ID
     * @return 评价列表及统计信息
     */
    @GetMapping("/reviews/{productId}")
    public Result getReviews(@PathVariable Integer productId) {
        log.info("查询商品评价列表, productId: {}", productId);
        
        try {
            // 1. 查询评价列表
            List<ProductReview> reviews = reviewService.getReviewsByProductId(productId);
            
            // 2. 查询评价统计信息
            Integer reviewCount = reviewService.getReviewCount(productId);
            Double averageScore = reviewService.getAverageScore(productId);
            
            // 3. 组装返回数据
            Map<String, Object> result = new HashMap<>();
            result.put("list", reviews);
            result.put("count", reviewCount);
            result.put("averageScore", averageScore != null ? String.format("%.1f", averageScore) : "0.0");
            
            log.info("查询成功, productId: {}, 评价数量: {}", productId, reviewCount);
            return Result.success(result);
            
        } catch (ServiceException e) {
            log.warn("查询评价失败, productId: {}, msg: {}", productId, e.getMessage());
            return Result.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("查询评价异常, productId: {}", productId, e);
            return Result.error("查询评价失败，请稍后重试");
        }
    }

    /**
     * 添加商品评价
     * POST /product/addReview
     * 
     * 请求参数示例：
     * {
     *   "productId": 1,
     *   "orderId": 123,
     *   "score": 5,
     *   "content": "商品质量很好，非常满意！"
     * }
     * 
     * @param review 评价对象
     * @return 评价结果
     */
    @PostMapping("/addReview")
    public Result addReview(@RequestBody ProductReview review) {
        log.info("提交商品评价, productId: {}, score: {}", 
                review.getProductId(), review.getScore());
        
        try {
            // 1. 获取当前登录用户
            User currentUser = TokenUtils.getCurrentUser();
            if (currentUser == null) {
                log.warn("用户未登录，无法提交评价");
                return Result.error("401", "请先登录后再评价");
            }
            
            // 2. 提交评价
            Integer reviewId = reviewService.addReview(review, currentUser.getId());
            
            log.info("评价提交成功, reviewId: {}, userId: {}", reviewId, currentUser.getId());
            return Result.success("评价提交成功");
            
        } catch (ServiceException e) {
            log.warn("评价提交失败, msg: {}", e.getMessage());
            return Result.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("评价提交异常", e);
            return Result.error("500", "评价提交失败：" + e.getMessage());
        }
    }

    /**
     * 删除评价（用户只能删除自己的评价）
     * DELETE /product/review/{reviewId}
     * 
     * @param reviewId 评价ID
     * @return 删除结果
     */
    @DeleteMapping("/review/{reviewId}")
    public Result deleteReview(@PathVariable Integer reviewId) {
        log.info("删除评价, reviewId: {}", reviewId);
        
        try {
            // 1. 获取当前登录用户
            User currentUser = TokenUtils.getCurrentUser();
            if (currentUser == null) {
                log.warn("用户未登录，无法删除评价");
                return Result.error("401", "请先登录");
            }
            
            // 2. 执行删除
            Boolean success = reviewService.deleteReview(reviewId, currentUser.getId());
            
            if (success) {
                log.info("评价删除成功, reviewId: {}", reviewId);
                return Result.success("评价删除成功");
            } else {
                log.warn("评价删除失败, reviewId: {}", reviewId);
                return Result.error("评价删除失败");
            }
            
        } catch (ServiceException e) {
            log.warn("删除评价失败, reviewId: {}, msg: {}", reviewId, e.getMessage());
            return Result.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("删除评价异常, reviewId: {}", reviewId, e);
            return Result.error("删除评价失败，请稍后重试");
        }
    }

    /**
     * 查询商品评价统计信息
     * GET /product/reviewStats/{productId}
     * 
     * @param productId 商品ID
     * @return 评价统计（数量、平均分）
     */
    @GetMapping("/reviewStats/{productId}")
    public Result getReviewStats(@PathVariable Integer productId) {
        log.info("查询评价统计，productId: {}", productId);
        
        try {
            Integer count = reviewService.getReviewCount(productId);
            Double averageScore = reviewService.getAverageScore(productId);
            
            Map<String, Object> stats = new HashMap<>();
            stats.put("count", count);
            stats.put("averageScore", averageScore != null ? averageScore : 0.0);
            
            return Result.success(stats);
            
        } catch (Exception e) {
            log.error("查询评价统计异常，productId: {}", productId, e);
            return Result.error("查询失败");
        }
    }

    /**
     * 查询用户是否已评价过某订单
     * GET /review/selectByUserAndOrder
     * 
     * @param userId 用户 ID
     * @param orderId 订单 ID
     * @return 评价记录，不存在返回空对象
     */
    @GetMapping("/selectByUserAndOrder")
    public Result selectByUserAndOrder(@RequestParam Integer userId,
                                       @RequestParam Integer orderId) {
        log.info("查询用户评价状态，userId: {}, orderId: {}", userId, orderId);
        
        try {
            // 查询用户对该订单所有商品的评价
            List<ProductReview> reviews = reviewMapper.selectReviewsByUserIdAndOrderId(userId, orderId);
            boolean isReviewed = reviews != null && !reviews.isEmpty();
            
            Map<String, Object> result = new HashMap<>();
            result.put("list", reviews);
            result.put("isReviewed", isReviewed);
            
            return Result.success(result);
            
        } catch (Exception e) {
            log.error("查询用户评价状态异常", e);
            return Result.error("查询失败");
        }
    }
}
