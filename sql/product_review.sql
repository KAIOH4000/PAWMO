-- ========================================================
-- 宠物电商平台 - 商品评价表
-- 表名: product_review
-- 描述: 存储用户对商品的评价信息
-- 作者: System
-- 创建时间: 2025-03-14
-- ========================================================

-- 创建商品评价表
CREATE TABLE IF NOT EXISTS `product_review` (
    -- 主键ID
    `id` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '评价ID，主键自增',
    
    -- 关联字段
    `product_id` INT UNSIGNED NOT NULL COMMENT '商品ID，关联goods表',
    `user_id` INT UNSIGNED NOT NULL COMMENT '用户ID，关联user表',
    `order_id` INT UNSIGNED NOT NULL COMMENT '订单ID，关联orders表，用于验证购买记录',
    
    -- 评价内容
    `score` TINYINT UNSIGNED NOT NULL COMMENT '评分，1-5星',
    `content` VARCHAR(500) NOT NULL COMMENT '评价内容，最多500字',
    
    -- 管理字段
    `is_admin_reply` TINYINT UNSIGNED DEFAULT 0 COMMENT '是否管理员回复：0-否，1-是',
    `admin_reply_content` VARCHAR(500) DEFAULT NULL COMMENT '管理员回复内容',
    `admin_reply_time` DATETIME DEFAULT NULL COMMENT '管理员回复时间',
    
    -- 时间字段
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '评价创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '评价更新时间',
    
    -- 逻辑删除字段
    `is_deleted` TINYINT UNSIGNED DEFAULT 0 COMMENT '是否删除：0-正常，1-已删除',
    
    -- 主键约束
    PRIMARY KEY (`id`),
    
    -- 索引设计
    KEY `idx_product_id` (`product_id`) COMMENT '商品ID索引，用于查询商品评价列表',
    KEY `idx_user_id` (`user_id`) COMMENT '用户ID索引，用于查询用户历史评价',
    KEY `idx_order_id` (`order_id`) COMMENT '订单ID索引，用于验证用户是否已评价',
    KEY `idx_create_time` (`create_time`) COMMENT '创建时间索引，用于按时间排序',
    KEY `idx_product_score` (`product_id`, `score`) COMMENT '商品ID+评分联合索引，用于统计商品评分分布'
    
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='商品评价表';

-- ========================================================
-- 插入测试数据（可选）
-- ========================================================

-- 插入模拟评价数据
INSERT INTO `product_review` (`product_id`, `user_id`, `order_id`, `score`, `content`, `create_time`) VALUES
(1, 1, 1, 5, '猫砂盆质量很好，空间很大，我家布偶猫用着很合适。封闭式设计确实能有效隔绝异味，推荐购买！', '2025-03-10 14:30:00'),
(1, 2, 2, 4, '做工不错，材质厚实，就是价格稍微有点贵，不过物有所值。', '2025-03-08 09:15:00'),
(1, 3, 3, 5, '第三次回购了，猫咪很喜欢，清理也很方便，好评！', '2025-03-05 18:45:00'),
(2, 1, 4, 5, '狗粮很香，狗狗很爱吃，包装也很精美。', '2025-03-12 10:20:00'),
(2, 4, 5, 3, '狗狗不太喜欢这个口味，可能因狗而异吧。', '2025-03-11 16:30:00');

-- ========================================================
-- 常用查询语句示例
-- ========================================================

-- 1. 查询某商品的所有评价（按时间倒序）
-- SELECT * FROM product_review WHERE product_id = 1 AND is_deleted = 0 ORDER BY create_time DESC;

-- 2. 查询某商品的平均评分
-- SELECT AVG(score) as avg_score, COUNT(*) as review_count FROM product_review WHERE product_id = 1 AND is_deleted = 0;

-- 3. 查询某用户是否已购买并评价过某商品
-- SELECT * FROM product_review WHERE product_id = 1 AND user_id = 1 AND is_deleted = 0 LIMIT 1;

-- 4. 查询某订单的评价
-- SELECT * FROM product_review WHERE order_id = 1 AND is_deleted = 0;
