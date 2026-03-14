-- 创建宣传图表
CREATE TABLE IF NOT EXISTS `promotion_image` (
  `id` int NOT NULL AUTO_INCREMENT,
  `category` varchar(50) DEFAULT NULL COMMENT '分类标识：cat/dog/small_pet/aquarium/reptile',
  `image_url` varchar(500) DEFAULT NULL COMMENT '图片URL',
  `category_name` varchar(50) DEFAULT NULL COMMENT '分类名称：猫咪/狗狗/小宠/水族/爬宠',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 插入默认宣传图数据（使用占位图片，请在后台管理中上传实际图片）
INSERT INTO `promotion_image` (`category`, `image_url`, `category_name`) VALUES
('cat', 'https://placehold.co/400x300/FFA07A/FFF?text=Cat', '猫咪'),
('dog', 'https://placehold.co/400x300/87CEEB/FFF?text=Dog', '狗狗'),
('small_pet', 'https://placehold.co/400x300/98FB98/FFF?text=Small+Pet', '小宠'),
('aquarium', 'https://placehold.co/400x300/00CED1/FFF?text=Aquarium', '水族'),
('reptile', 'https://placehold.co/400x300/9370DB/FFF?text=Reptile', '爬宠');

