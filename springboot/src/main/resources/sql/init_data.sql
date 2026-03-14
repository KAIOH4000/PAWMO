-- ==========================================
-- 宠物用品电商平台数据库初始化脚本
-- ==========================================

-- 1. 创建宣传图表
CREATE TABLE IF NOT EXISTS `promotion_image` (
  `id` int NOT NULL AUTO_INCREMENT,
  `category` varchar(50) DEFAULT NULL COMMENT '分类标识：cat/dog/small_pet/aquarium/reptile',
  `image_url` varchar(500) DEFAULT NULL COMMENT '图片URL',
  `category_name` varchar(50) DEFAULT NULL COMMENT '分类名称：猫咪/狗狗/小宠/水族/爬宠',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 2. 插入默认宣传图数据
INSERT INTO `promotion_image` (`category`, `image_url`, `category_name`) VALUES
('cat', 'https://placehold.co/400x300/FFA07A/FFF?text=Cat', '猫咪'),
('dog', 'https://placehold.co/400x300/87CEEB/FFF?text=Dog', '狗狗'),
('small_pet', 'https://placehold.co/400x300/98FB98/FFF?text=Small+Pet', '小宠'),
('aquarium', 'https://placehold.co/400x300/00CED1/FFF?text=Aquarium', '水族'),
('reptile', 'https://placehold.co/400x300/9370DB/FFF?text=Reptile', '爬宠');

-- 3. 插入商品分类数据（如果type表为空）
INSERT INTO `type` (`name`) VALUES
('猫咪'),
('狗狗'),
('小宠'),
('水族'),
('爬宠');

-- 4. 插入商品数据
-- 猫咪用品 (type_id = 1)
INSERT INTO goods (name, descr, content, cover, price, store, type_id, state, sales, `date`) VALUES
('豪华猫爬架', '三层实木结构', '三层实木猫爬架，带舒适猫窝和抓柱，高度1.5米，适合多猫家庭使用，材质环保安全', 'https://placehold.co/400x300/FFA07A/FFF?text=Cat+Tower', 299.00, 50, 1, '在售', 0, NOW()),
('自动喂食器', '智能定时投喂', '智能定时猫粮喂食器，可设置6餐，2.5L大容量，带语音录制功能，让您的猫咪按时吃饭', 'https://placehold.co/400x300/FFA07A/FFF?text=Feeder', 189.00, 30, 1, '在售', 0, NOW()),
('猫薄荷玩具鱼', '有机猫薄荷填充', '有机猫薄荷填充玩具鱼，激发猫咪玩耍兴趣，不含化学添加剂，安全健康', 'https://placehold.co/400x300/FFA07A/FFF?text=Catnip', 25.00, 100, 1, '在售', 0, NOW()),
('全封闭猫砂盆', '防臭防溅设计', '大号全封闭猫厕所，带活性炭过滤门帘，有效减少异味扩散，保护猫咪隐私', 'https://placehold.co/400x300/FFA07A/FFF?text=Litter+Box', 159.00, 40, 1, '在售', 0, NOW()),
('猫咪指甲剪', '安全不伤爪', '专业宠物指甲剪，带LED灯和指甲锉，精准修剪不伤血线，轻松护理猫咪爪子', 'https://placehold.co/400x300/FFA07A/FFF?text=Nail+Clipper', 15.00, 200, 1, '在售', 0, NOW()),
('猫用梳毛刷', '去浮毛不伤肤', '双面不锈钢针梳，一面去浮毛，一面按摩皮肤，防静电设计，让猫咪毛发更顺滑', 'https://placehold.co/400x300/FFA07A/FFF?text=Brush', 35.00, 150, 1, '在售', 0, NOW());

-- 狗狗用品 (type_id = 2)
INSERT INTO goods (name, descr, content, cover, price, store, type_id, state, sales, `date`) VALUES
('防爆冲牵引绳', '可伸缩缓冲设计', '5米可伸缩牵引绳，带缓冲系统，最大承重50kg，适合中大型犬，让遛狗更轻松安全', 'https://placehold.co/400x300/87CEEB/FFF?text=Leash', 89.00, 60, 2, '在售', 0, NOW()),
('耐咬磨牙棒', '清洁牙齿玩具', '天然橡胶磨牙玩具，清洁牙齿同时缓解焦虑，适合换牙期幼犬，安全无毒', 'https://placehold.co/400x300/87CEEB/FFF?text=Chew+Toy', 35.00, 200, 2, '在售', 0, NOW()),
('智能饮水机', '循环过滤系统', '3L循环活水饮水机，三重过滤系统，静音水泵，适合所有体型犬只，促进多喝水', 'https://placehold.co/400x300/87CEEB/FFF?text=Water+Fountain', 159.00, 40, 2, '在售', 0, NOW()),
('狗狗航空箱', '安全运输工具', 'PP材质航空箱，通风透气，可拆卸托盘，适合长途旅行，出行必备', 'https://placehold.co/400x300/87CEEB/FFF?text=Carrier', 229.00, 20, 2, '在售', 0, NOW()),
('犬用沐浴露', '深层清洁配方', '500ml宠物专用沐浴露，温和不刺激，去污除臭效果显著，让狗狗毛发亮泽', 'https://placehold.co/400x300/87CEEB/FFF?text=Shampoo', 45.00, 150, 2, '在售', 0, NOW()),
('自动投食器', '定时定量喂食', '6餐编程自动喂食器，4L大容量，带语音提示功能，让爱犬按时用餐', 'https://placehold.co/400x300/87CEEB/FFF?text=Auto+Feeder', 219.00, 30, 2, '在售', 0, NOW());

-- 小宠用品 (type_id = 3)
INSERT INTO goods (name, descr, content, cover, price, store, type_id, state, sales, `date`) VALUES
('仓鼠跑轮', '静音轴承设计', '28cm超大静音跑轮，无缝隙设计，防止夹脚伤害，让仓鼠健康运动', 'https://placehold.co/400x300/98FB98/FFF?text=Wheel', 49.00, 100, 3, '在售', 0, NOW()),
('小宠笼具', '多层别墅设计', '60cm三层笼子，带食盆水壶和跑轮，适合仓鼠等小宠，空间宽敞舒适', 'https://placehold.co/400x300/98FB98/FFF?text=Cage', 159.00, 30, 3, '在售', 0, NOW()),
('仓鼠木屑', '除臭吸水', '10L天然松木木屑，高吸水性，有效控制异味，保持笼内清洁干燥', 'https://placehold.co/400x300/98FB98/FFF?text=Wood+Shavings', 25.00, 200, 3, '在售', 0, NOW()),
('小宠食盆', '陶瓷防翻设计', '陶瓷材质小宠食盆，重量适中防翻倒，易清洁，美观实用', 'https://placehold.co/400x300/98FB98/FFF?text=Food+Bowl', 15.00, 200, 3, '在售', 0, NOW()),
('仓鼠磨牙石', '天然矿物质', '含多种矿物质的磨牙石，帮助小宠磨牙防止过长，补充营养', 'https://placehold.co/400x300/98FB98/FFF?text=Chew+Stone', 12.00, 300, 3, '在售', 0, NOW()),
('小宠隧道', '多通道玩具', '可拼接塑料隧道，丰富小宠活动空间，防止无聊，增加乐趣', 'https://placehold.co/400x300/98FB98/FFF?text=Tunnel', 35.00, 150, 3, '在售', 0, NOW());

-- 水族用品 (type_id = 4)
INSERT INTO goods (name, descr, content, cover, price, store, type_id, state, sales, `date`) VALUES
('水族箱套装', '超白玻璃缸体', '60cm超白玻璃鱼缸套装，含过滤器和LED灯，新手入门首选，美观大方', 'https://placehold.co/400x300/00CED1/FFF?text=Aquarium', 499.00, 20, 4, '在售', 0, NOW()),
('鱼缸过滤器', '三层生化过滤', '外置桶式过滤器，静音设计，适合60-90cm鱼缸，水质清澈透明', 'https://placehold.co/400x300/00CED1/FFF?text=Filter', 259.00, 30, 4, '在售', 0, NOW()),
('水族加热棒', '恒温精准控制', '300W不锈钢加热棒，自动恒温，防爆设计，保持水温稳定', 'https://placehold.co/400x300/00CED1/FFF?text=Heater', 89.00, 50, 4, '在售', 0, NOW()),
('鱼粮', '全营养配方', '200ml热带鱼专用粮，浮性颗粒，促进发色，营养均衡', 'https://placehold.co/400x300/00CED1/FFF?text=Fish+Food', 35.00, 200, 4, '在售', 0, NOW()),
('水草泥', '营养底床', '5L水草专用泥，提供养分，稳定水质，促进水草生长', 'https://placehold.co/400x300/00CED1/FFF?text=Soil', 59.00, 100, 4, '在售', 0, NOW()),
('LED水族灯', '全光谱照明', '72W全光谱LED灯，模拟自然光照，促进水草生长，观赏效果佳', 'https://placehold.co/400x300/00CED1/FFF?text=LED+Light', 199.00, 30, 4, '在售', 0, NOW());

-- 爬宠用品 (type_id = 5)
INSERT INTO goods (name, descr, content, cover, price, store, type_id, state, sales, `date`) VALUES
('爬宠饲养箱', '玻璃通风设计', '45x45x60cm玻璃爬箱，前开门设计，通风透气，观察方便', 'https://placehold.co/400x300/9370DB/FFF?text=Terrarium', 299.00, 15, 5, '在售', 0, NOW()),
('爬宠加热垫', '恒温保暖', '20W加热垫，自动恒温，适合蛇类等爬行动物，保持适宜温度', 'https://placehold.co/400x300/9370DB/FFF?text=Heat+Mat', 59.00, 50, 5, '在售', 0, NOW()),
('UVB灯', '促进钙质吸收', '10.0 UVB节能灯泡，模拟自然阳光，预防代谢病，健康必备', 'https://placehold.co/400x300/9370DB/FFF?text=UVB+Lamp', 89.00, 40, 5, '在售', 0, NOW()),
('爬宠垫材', '椰土保湿', '5L压缩椰土砖，保湿性好，适合需要湿度的爬宠，自然环境', 'https://placehold.co/400x300/9370DB/FFF?text=Coco+Fiber', 35.00, 100, 5, '在售', 0, NOW()),
('温湿度计', '双显监测', '数字显示温湿度计，带记忆功能，精准监测环境参数', 'https://placehold.co/400x300/9370DB/FFF?text=Thermometer', 25.00, 150, 5, '在售', 0, NOW()),
('爬宠躲避穴', '仿真岩石', '中号躲避洞穴，提供安全感，帮助蜕皮，自然仿真', 'https://placehold.co/400x300/9370DB/FFF?text=Hide+Box', 45.00, 80, 5, '在售', 0, NOW());
