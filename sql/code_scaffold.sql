/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 50737
 Source Host           : localhost:3306
 Source Schema         : code_scaffold

 Target Server Type    : MySQL
 Target Server Version : 50737
 File Encoding         : 65001

 Date: 14/11/2025 11:13:13
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户名',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '密码',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '姓名',
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '电话',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '邮箱',
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '地址',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '头像',
  `sex` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '性别',
  `age` int(11) NULL DEFAULT NULL COMMENT '年龄',
  `infos` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '个人介绍',
  `role` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '角色',
  `account` double(10, 2) NULL DEFAULT 0.00 COMMENT '账户余额',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'admin', '123', '管理员', '13677889988', 'admin@xxx.com', '江苏南京', 'http://localhost:9999/file/download/汤姆.jpg', '男', 27, '我是管理员', 'ADMIN', 0.00);
INSERT INTO `user` VALUES (2, 'tom', '123', '汤姆', '13988776699', 'jerry@qq.com', '北京', 'http://localhost:9999/file/download/汤姆.jpg', '男', 24, '我爱你，亲爱的姑娘', 'USER', 0.00);
INSERT INTO `user` VALUES (3, 'jerry', '123', '杰瑞', '15098765321', 'tom@qq.com', '上海', 'http://localhost:9999/file/download/杰瑞.jpg', '男', 25, '我爱你，亲爱的姑娘', 'USER', 0.00);

-- ----------------------------
-- Table structure for type
-- ----------------------------
DROP TABLE IF EXISTS `type`;
CREATE TABLE `type`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '分类名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '商品分类表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of type (宠物用品五大分类)
-- ----------------------------
INSERT INTO `type` VALUES (1, '猫咪');
INSERT INTO `type` VALUES (2, '狗狗');
INSERT INTO `type` VALUES (3, '小宠');
INSERT INTO `type` VALUES (4, '水族');
INSERT INTO `type` VALUES (5, '爬宠');

-- ----------------------------
-- Table structure for goods
-- ----------------------------
DROP TABLE IF EXISTS `goods`;
CREATE TABLE `goods`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '商品名称',
  `descr` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '商品描述',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '商品详情',
  `cover` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '商品封面',
  `price` double(10, 2) NULL DEFAULT NULL COMMENT '商品价格',
  `store` int(11) NULL DEFAULT NULL COMMENT '库存',
  `user_id` int(11) NULL DEFAULT NULL COMMENT '发布用户ID',
  `date` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '发布日期',
  `type_id` int(11) NULL DEFAULT NULL COMMENT '分类ID',
  `state` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '状态',
  `sales` int(11) NULL DEFAULT 0 COMMENT '销量',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 41 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '商品表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of goods (宠物用品商品数据)
-- ----------------------------
-- 猫咪用品 (type_id = 1)
INSERT INTO `goods` VALUES (1, '皇家猫粮幼猫粮', '专为幼猫设计的高营养猫粮', '皇家幼猫粮含有丰富的蛋白质和维生素，专为2-12个月幼猫设计，帮助猫咪健康成长。', 'https://picsum.photos/400/400?random=1', 128.00, 100, 1, '2025-03-12', 1, '上架', 25);
INSERT INTO `goods` VALUES (2, '猫砂盆全封闭超大号', '防臭防溅猫厕所', '超大空间设计，全封闭结构有效防臭，可拆卸清洗，适合各种体型的猫咪。', 'https://picsum.photos/400/400?random=2', 89.00, 50, 1, '2025-03-12', 1, '上架', 18);
INSERT INTO `goods` VALUES (3, '猫抓板磨爪器', '瓦楞纸猫抓板耐磨耐抓', '高密度瓦楞纸材质，耐磨耐抓，保护家具不被抓坏，猫咪磨爪好帮手。', 'https://picsum.photos/400/400?random=3', 25.00, 200, 1, '2025-03-12', 1, '上架', 45);
INSERT INTO `goods` VALUES (4, '逗猫棒羽毛玩具', '互动玩具增进感情', '天然羽毛制作，柔软安全，可伸缩设计，与猫咪互动玩耍，增进感情。', 'https://picsum.photos/400/400?random=4', 15.00, 300, 1, '2025-03-12', 1, '上架', 60);
INSERT INTO `goods` VALUES (21, '猫咪自动饮水机', '循环过滤活水饮水器', '静音水泵循环过滤，保持水质新鲜，吸引猫咪多喝水，预防泌尿系统疾病。', 'https://picsum.photos/400/400?random=21', 168.00, 80, 1, '2025-03-13', 1, '上架', 12);
INSERT INTO `goods` VALUES (22, '猫爬架猫树一体', '多层跳台猫窝', '多层设计满足猫咪攀爬天性，剑麻柱磨爪，舒适猫窝休息，猫咪乐园。', 'https://picsum.photos/400/400?random=22', 258.00, 30, 1, '2025-03-13', 1, '上架', 8);
INSERT INTO `goods` VALUES (23, '猫咪营养膏化毛膏', '去毛球补充营养', '富含多种维生素和矿物质，帮助猫咪排出毛球，补充营养，增强免疫力。', 'https://picsum.photos/400/400?random=23', 45.00, 150, 1, '2025-03-13', 1, '上架', 35);
INSERT INTO `goods` VALUES (24, '猫包外出便携包', '透气太空舱背包', '透气网眼设计，太空舱造型，轻便舒适，带猫咪外出就医旅行必备。', 'https://picsum.photos/400/400?random=24', 88.00, 60, 1, '2025-03-13', 1, '上架', 15);
INSERT INTO `goods` VALUES (25, '猫咪冻干零食', '鸡肉鸭肉冻干', '精选优质肉类，-40℃真空冻干，保留营养，口感酥脆，猫咪爱吃。', 'https://picsum.photos/400/400?random=25', 35.00, 200, 1, '2025-03-13', 1, '上架', 42);
INSERT INTO `goods` VALUES (26, '猫砂除臭珠', '猫砂盆除臭剂', '天然植物精华，有效吸附异味，撒在猫砂中持久清新，人猫都舒适。', 'https://picsum.photos/400/400?random=26', 22.00, 180, 1, '2025-03-13', 1, '上架', 28);
INSERT INTO `goods` VALUES (27, '猫咪指甲剪', '专用宠物指甲钳', '圆头安全设计，防止剪伤血线，带锉刀打磨，轻松给猫咪剪指甲。', 'https://picsum.photos/400/400?random=27', 18.00, 250, 1, '2025-03-13', 1, '上架', 55);
INSERT INTO `goods` VALUES (28, '猫咪项圈铃铛', '可爱装饰颈圈', '柔软材质不勒脖子，可爱铃铛设计，可调节大小，适合猫咪日常佩戴。', 'https://picsum.photos/400/400?random=28', 12.00, 300, 1, '2025-03-13', 1, '上架', 68);
INSERT INTO `goods` VALUES (29, '猫咪专用沐浴露', '温和无刺激香波', 'pH值温和配方，不刺激猫咪皮肤，天然香味，洗后毛发柔顺蓬松。', 'https://picsum.photos/400/400?random=29', 38.00, 120, 1, '2025-03-13', 1, '上架', 22);
INSERT INTO `goods` VALUES (30, '猫咪电动玩具球', '自动滚动逗猫球', '智能感应自动滚动，LED灯光吸引，让猫咪自嗨解闷，释放精力。', 'https://picsum.photos/400/400?random=30', 55.00, 100, 1, '2025-03-13', 1, '上架', 18);

-- 狗狗用品 (type_id = 2)
INSERT INTO `goods` VALUES (5, '伯纳天纯狗粮', '天然无谷狗粮', '选用优质肉类原料，无谷物配方，低敏易消化，适合各年龄段狗狗。', 'https://picsum.photos/400/400?random=5', 168.00, 80, 1, '2025-03-12', 2, '上架', 32);
INSERT INTO `goods` VALUES (6, '狗狗牵引绳套装', '胸背带遛狗绳', '舒适透气材质，不勒脖子，反光条设计夜间安全，适合中小型犬。', 'https://picsum.photos/400/400?random=6', 45.00, 150, 1, '2025-03-12', 2, '上架', 28);
INSERT INTO `goods` VALUES (7, '狗窝四季通用', '可拆洗宠物窝', '柔软舒适，四季通用，可拆洗设计方便清洁，给狗狗一个温暖的家。', 'https://picsum.photos/400/400?random=7', 78.00, 60, 1, '2025-03-12', 2, '上架', 15);
INSERT INTO `goods` VALUES (8, '狗狗磨牙棒', '洁齿骨咬胶玩具', '天然牛皮制作，耐咬耐磨，帮助清洁牙齿，缓解狗狗焦虑情绪。', 'https://picsum.photos/400/400?random=8', 35.00, 120, 1, '2025-03-12', 2, '上架', 40);
INSERT INTO `goods` VALUES (31, '狗狗智能喂食器', '定时定量自动投喂', '手机APP远程控制，定时定量投喂，视频语音互动，出差旅行无忧。', 'https://picsum.photos/400/400?random=31', 299.00, 40, 1, '2025-03-13', 2, '上架', 6);
INSERT INTO `goods` VALUES (32, '狗狗飞盘玩具', '耐咬软胶飞盘', '软胶材质不伤牙齿，飞行稳定，适合户外互动，锻炼狗狗身体。', 'https://picsum.photos/400/400?random=32', 28.00, 200, 1, '2025-03-13', 2, '上架', 38);
INSERT INTO `goods` VALUES (33, '狗狗雨衣防水', '四脚全包雨披', '透明防水材质，四脚全包设计，雨天遛狗不湿身，带反光条安全。', 'https://picsum.photos/400/400?random=33', 48.00, 100, 1, '2025-03-13', 2, '上架', 22);
INSERT INTO `goods` VALUES (34, '狗狗拾便袋', '可降解便便袋', '环保可降解材质，加厚防漏，方便清理，文明养犬必备。', 'https://picsum.photos/400/400?random=34', 15.00, 300, 1, '2025-03-13', 2, '上架', 65);
INSERT INTO `goods` VALUES (35, '狗狗训练零食', '鸡肉干牛肉粒', '高肉含量，适口性好，小块设计方便训练奖励，增进人狗感情。', 'https://picsum.photos/400/400?random=35', 32.00, 180, 1, '2025-03-13', 2, '上架', 45);
INSERT INTO `goods` VALUES (36, '狗狗洗澡手套', '硅胶按摩刷', '柔软硅胶材质，按摩清洁两不误，去浮毛效果好，狗狗洗澡不抗拒。', 'https://picsum.photos/400/400?random=36', 22.00, 250, 1, '2025-03-13', 2, '上架', 52);
INSERT INTO `goods` VALUES (37, '狗狗航空箱', '外出托运箱', '符合航空标准，坚固耐用，透气通风，带狗狗出行托运必备。', 'https://picsum.photos/400/400?random=37', 128.00, 50, 1, '2025-03-13', 2, '上架', 12);
INSERT INTO `goods` VALUES (38, '狗狗钙片营养', '补钙健骨维生素', '富含钙质和维生素D，帮助狗狗骨骼发育，预防关节问题。', 'https://picsum.photos/400/400?random=38', 42.00, 150, 1, '2025-03-13', 2, '上架', 28);
INSERT INTO `goods` VALUES (39, '狗狗玩具球', '耐咬橡胶弹力球', '天然橡胶材质，耐咬耐玩，可浮水设计，适合各种犬类。', 'https://picsum.photos/400/400?random=39', 18.00, 280, 1, '2025-03-13', 2, '上架', 58);
INSERT INTO `goods` VALUES (40, '狗狗除臭喷雾', '宠物去味消毒剂', '生物酶分解技术，快速去除异味，安全无毒，可喷狗窝环境。', 'https://picsum.photos/400/400?random=40', 35.00, 120, 1, '2025-03-13', 2, '上架', 20);
INSERT INTO `goods` VALUES (41, '猫咪智能猫砂盆', '全自动清理猫厕所', '感应自动清理，APP远程监控，大容量设计，解放双手告别铲屎。', 'https://picsum.photos/400/400?random=41', 899.00, 20, 1, '2025-03-13', 1, '上架', 5);
INSERT INTO `goods` VALUES (42, '猫咪鱼油美毛', '深海鱼油软胶囊', '富含Omega-3，美毛护肤，减少掉毛，增强猫咪免疫力。', 'https://picsum.photos/400/400?random=42', 68.00, 100, 1, '2025-03-13', 1, '上架', 32);
INSERT INTO `goods` VALUES (43, '猫咪隧道玩具', '可折叠猫隧道', '可折叠收纳，铃铛球设计，满足猫咪钻洞天性，自嗨解闷神器。', 'https://picsum.photos/400/400?random=43', 45.00, 150, 1, '2025-03-13', 1, '上架', 48);
INSERT INTO `goods` VALUES (44, '猫咪益生菌', '调理肠胃保健品', '活性益生菌配方，调理肠胃，改善软便，增强消化吸收。', 'https://picsum.photos/400/400?random=44', 55.00, 80, 1, '2025-03-13', 1, '上架', 25);
INSERT INTO `goods` VALUES (45, '猫咪激光笔', '自动逗猫激光灯', '自动随机移动，USB充电，解放双手，让猫咪疯狂追逐。', 'https://picsum.photos/400/400?random=45', 38.00, 200, 1, '2025-03-13', 1, '上架', 62);
INSERT INTO `goods` VALUES (46, '猫咪洁齿饼干', '去口臭磨牙零食', '酥脆口感，有效清洁牙齿，去除口臭，猫咪爱吃。', 'https://picsum.photos/400/400?random=46', 28.00, 250, 1, '2025-03-13', 1, '上架', 55);
INSERT INTO `goods` VALUES (47, '猫咪保暖窝', '冬季加厚猫床', '加厚保暖材质，半封闭设计，给猫咪温暖安全感，冬季必备。', 'https://picsum.photos/400/400?random=47', 78.00, 60, 1, '2025-03-13', 1, '上架', 18);
INSERT INTO `goods` VALUES (48, '猫咪驱虫药', '体内外驱虫滴剂', '广谱驱虫，安全有效，一月一次，保护猫咪远离寄生虫。', 'https://picsum.photos/400/400?random=48', 88.00, 120, 1, '2025-03-13', 1, '上架', 42);
INSERT INTO `goods` VALUES (49, '猫咪猫草种子', '猫薄荷小麦草', '天然猫草，帮助猫咪排出毛球，清新口气，种植简单。', 'https://picsum.photos/400/400?random=49', 15.00, 300, 1, '2025-03-13', 1, '上架', 75);
INSERT INTO `goods` VALUES (50, '猫咪监控摄像头', '宠物智能摄像机', '360度旋转，夜视功能，双向语音，随时查看猫咪动态。', 'https://picsum.photos/400/400?random=50', 168.00, 40, 1, '2025-03-13', 1, '上架', 12);
INSERT INTO `goods` VALUES (51, '狗狗智能项圈', 'GPS定位防丢', '实时GPS定位，电子围栏，防水设计，手机APP随时查看狗狗位置。', 'https://picsum.photos/400/400?random=51', 199.00, 50, 1, '2025-03-13', 2, '上架', 15);
INSERT INTO `goods` VALUES (52, '狗狗关节保养', '软骨素营养片', '富含葡萄糖胺和软骨素，保护关节，适合老年犬和运动犬。', 'https://picsum.photos/400/400?random=52', 75.00, 100, 1, '2025-03-13', 2, '上架', 28);
INSERT INTO `goods` VALUES (53, '狗狗发声玩具', '耐咬会叫的玩具', '内置发声器，吸引狗狗注意，耐咬材质，解闷神器。', 'https://picsum.photos/400/400?random=53', 32.00, 200, 1, '2025-03-13', 2, '上架', 58);
INSERT INTO `goods` VALUES (54, '狗狗羊奶粉', '幼犬成犬营养品', '新西兰进口奶源，易消化吸收，补充营养，增强体质。', 'https://picsum.photos/400/400?random=54', 68.00, 80, 1, '2025-03-13', 2, '上架', 35);
INSERT INTO `goods` VALUES (55, '狗狗嘴套防咬', '防乱吃防叫口罩', '透气网布材质，可调节松紧，防止乱吃乱咬，保护他人安全。', 'https://picsum.photos/400/400?random=55', 25.00, 150, 1, '2025-03-13', 2, '上架', 42);
INSERT INTO `goods` VALUES (56, '狗狗电热毯', '宠物专用加热垫', '恒温加热，防水防抓，冬季保暖，给狗狗温暖睡眠环境。', 'https://picsum.photos/400/400?random=56', 88.00, 60, 1, '2025-03-13', 2, '上架', 22);
INSERT INTO `goods` VALUES (57, '狗狗驱虫项圈', '防跳蚤虱子颈圈', '天然植物精油，持续防护8个月，防水设计，安全无毒。', 'https://picsum.photos/400/400?random=57', 45.00, 180, 1, '2025-03-13', 2, '上架', 48);
INSERT INTO `goods` VALUES (58, '狗狗慢食碗', '防噎缓食盆', '迷宫设计，减缓进食速度，防止噎食，促进消化。', 'https://picsum.photos/400/400?random=58', 35.00, 120, 1, '2025-03-13', 2, '上架', 38);
INSERT INTO `goods` VALUES (59, '狗狗护毛素', '美毛柔顺护理液', '天然植物配方，滋润毛发，去除打结，让狗狗毛发柔顺亮泽。', 'https://picsum.photos/400/400?random=59', 48.00, 100, 1, '2025-03-13', 2, '上架', 25);
INSERT INTO `goods` VALUES (60, '狗狗自动饮水器', '循环过滤活水机', '循环过滤，保持水质新鲜，大容量设计，满足狗狗日常饮水需求。', 'https://picsum.photos/400/400?random=60', 128.00, 70, 1, '2025-03-13', 2, '上架', 18);

-- 小宠用品 (type_id = 3)
INSERT INTO `goods` VALUES (9, '仓鼠笼子豪华别墅', '双层笼舍带跑轮', '双层设计空间大，配备跑轮、食盆、水壶，给仓鼠一个舒适的家。', 'https://picsum.photos/400/400?random=9', 68.00, 40, 1, '2025-03-12', 3, '上架', 12);
INSERT INTO `goods` VALUES (10, '仓鼠粮食主粮', '五谷粮营养搭配', '精选五谷杂粮科学配比，营养均衡，适口性好，仓鼠爱吃。', 'https://picsum.photos/400/400?random=10', 22.00, 100, 1, '2025-03-12', 3, '上架', 35);
INSERT INTO `goods` VALUES (11, '兔子牧草提摩西草', '烘干提草兔粮', '优质提摩西草烘干制作，富含粗纤维，帮助兔子磨牙消化。', 'https://picsum.photos/400/400?random=11', 28.00, 80, 1, '2025-03-12', 3, '上架', 22);
INSERT INTO `goods` VALUES (12, '龙猫浴沙火山灰', '龙猫专用洗澡沙', '天然火山灰制作，细腻无粉尘，帮助龙猫清洁毛发，保持干爽。', 'https://picsum.photos/400/400?random=12', 32.00, 60, 1, '2025-03-12', 3, '上架', 18);

-- 水族用品 (type_id = 4)
INSERT INTO `goods` VALUES (13, '超白玻璃鱼缸', '客厅小型水族箱', '高透光超白玻璃，清晰观赏，适合金鱼、热带鱼饲养。', 'https://picsum.photos/400/400?random=13', 158.00, 30, 1, '2025-03-12', 4, '上架', 8);
INSERT INTO `goods` VALUES (14, '鱼缸过滤器三合一', '净水增氧循环泵', '过滤、增氧、循环三合一，静音设计，保持水质清洁。', 'https://picsum.photos/400/400?random=14', 55.00, 50, 1, '2025-03-12', 4, '上架', 20);
INSERT INTO `goods` VALUES (15, '热带鱼饲料鱼食', '小型鱼颗粒饲料', '营养均衡配方，适合孔雀鱼、灯科鱼等小型热带鱼，不浑水。', 'https://picsum.photos/400/400?random=15', 18.00, 150, 1, '2025-03-12', 4, '上架', 45);
INSERT INTO `goods` VALUES (16, '鱼缸LED灯水草灯', '全光谱照明灯', '全光谱LED灯，促进水草光合作用，增强鱼儿色彩，节能环保。', 'https://picsum.photos/400/400?random=16', 42.00, 40, 1, '2025-03-12', 4, '上架', 15);

-- 爬宠用品 (type_id = 5)
INSERT INTO `goods` VALUES (17, '爬宠饲养箱玻璃缸', '陆龟蜥蜴饲养箱', '玻璃材质透明可视，通风透气设计，适合陆龟、蜥蜴等爬宠饲养。', 'https://picsum.photos/400/400?random=17', 128.00, 25, 1, '2025-03-12', 5, '上架', 6);
INSERT INTO `goods` VALUES (18, '爬宠加热垫温控', '宠物保温加热片', '恒温加热，温度可调，给爬宠提供适宜温度，冬季保暖必备。', 'https://picsum.photos/400/400?random=18', 38.00, 60, 1, '2025-03-12', 5, '上架', 18);
INSERT INTO `goods` VALUES (19, '面包虫活虫饲料', '爬宠活体饲料', '高蛋白活体饲料，适合蜥蜴、守宫、仓鼠等宠物，营养丰富。', 'https://picsum.photos/400/400?random=19', 15.00, 100, 1, '2025-03-12', 5, '上架', 30);
INSERT INTO `goods` VALUES (20, '爬虫钙粉维生素', '爬宠营养补充剂', '富含钙质和维生素D3，帮助爬宠骨骼发育，预防代谢疾病。', 'https://picsum.photos/400/400?random=20', 25.00, 80, 1, '2025-03-12', 5, '上架', 22);

-- ----------------------------
-- Table structure for carousel
-- ----------------------------
DROP TABLE IF EXISTS `carousel`;
CREATE TABLE `carousel`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '轮播图名称',
  `cover` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '轮播图封面',
  `goods_id` int(11) NULL DEFAULT NULL COMMENT '关联商品ID',
  `type_id` int(11) NULL DEFAULT NULL COMMENT '关联分类ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '轮播图表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of carousel (宠物主题轮播图)
-- ----------------------------
INSERT INTO `carousel` VALUES (1, '猫咪用品专场', 'https://picsum.photos/1200/400?random=101', 1, 1);
INSERT INTO `carousel` VALUES (2, '狗狗用品精选', 'https://picsum.photos/1200/400?random=102', 5, 2);
INSERT INTO `carousel` VALUES (3, '小宠水族世界', 'https://picsum.photos/1200/400?random=103', 13, 4);

SET FOREIGN_KEY_CHECKS = 1;
