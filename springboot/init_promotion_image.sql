-- 设置字符集
SET NAMES utf8mb4;

-- 创建推广图片表
CREATE TABLE IF NOT EXISTS `promotion_image` (
  `id` int NOT NULL AUTO_INCREMENT,
  `category` varchar(50) NOT NULL COMMENT '分类标识',
  `image_url` varchar(500) DEFAULT NULL COMMENT '推广图片URL',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='推广图片表';

-- 插入5个分类的默认推广图片数据
INSERT INTO `promotion_image` (`category`, `image_url`) VALUES
('cats', 'https://placehold.co/800x400/FF6B6B/white?text=Cats+Zone'),
('dogs', 'https://placehold.co/800x400/4ECDC4/white?text=Dogs+Zone'),
('small-pets', 'https://placehold.co/800x400/95E1D3/white?text=Small+Pets+Zone'),
('aquatics', 'https://placehold.co/800x400/45B7D1/white?text=Aquatics+Zone'),
('reptiles', 'https://placehold.co/800x400/F9CA24/white?text=Reptiles+Zone');

-- 查询插入的数据
SELECT * FROM promotion_image;
