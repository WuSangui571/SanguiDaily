-- MySQL dump 10.13  Distrib 8.0.41, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: sanguidaily_db
-- ------------------------------------------------------
-- Server version	8.0.41

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `t_post`
--

DROP TABLE IF EXISTS `t_post`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_post` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '动态标识id',
  `author_id` bigint unsigned NOT NULL COMMENT '动态作者id（对应t_user.id）',
  `type` int unsigned NOT NULL DEFAULT '0' COMMENT '动态类型（0-纯文本，1-图片，2-链接，3-视频）',
  `content_text` text COMMENT '动态正文文本',
  `link_url` varchar(2048) DEFAULT NULL COMMENT '链接地址（type=2必填）',
  `link_title` varchar(256) DEFAULT NULL COMMENT '链接标题（type=2必填）',
  `link_cover_url` varchar(1024) DEFAULT NULL COMMENT '链接封面地址',
  `link_site_name` varchar(64) DEFAULT NULL COMMENT '链接站点名字',
  `video_url` varchar(2048) DEFAULT NULL COMMENT '视频地址（type=3时必填）',
  `video_cover_url` varchar(1024) DEFAULT NULL COMMENT '视频封面图地址',
  `status` int unsigned NOT NULL DEFAULT '0' COMMENT '动态状态（0-公开，1-私密）',
  `is_pinned` int unsigned NOT NULL DEFAULT '0' COMMENT '动态是否置顶（0-否，1-是）',
  `pinned_at` datetime(3) DEFAULT NULL COMMENT '动态置顶时间',
  `like_count` int unsigned NOT NULL DEFAULT '0' COMMENT '动态点赞数',
  `created_at` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
  `updated_at` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '修改时间',
  `deleted_at` datetime(3) DEFAULT NULL COMMENT '删除时间（软删除）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='动态表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_post`
--

LOCK TABLES `t_post` WRITE;
/*!40000 ALTER TABLE `t_post` DISABLE KEYS */;
INSERT INTO t_post (id, author_id, type, content_text, link_url, link_title, link_cover_url, link_site_name, video_url, video_cover_url, status, is_pinned, pinned_at, like_count, created_at, updated_at, deleted_at) VALUES
  (1001, 1, 0, '早起一杯热茶，窗外是安静的冬日。今天开始做三桂日常的小程序示例，把朋友圈的熟悉感做得更克制一些。长文本也要能展开，方便记录更完整的想法。', '', '', '', '', '', '', 0, 1, '2026-01-09 09:30:00', 12, '2026-01-08 21:18:00', '2026-01-08 21:18:00', NULL),
  (1002, 1, 1, '冬日散步的几张照片。', '', '', '', '', '', '', 0, 0, NULL, 5, '2026-01-09 08:30:00', '2026-01-09 08:30:00', NULL),
  (1003, 1, 2, '很喜欢这篇文章的表达方式。', 'https://www.sangui.top/', '写给自己的日常记录法', '', '三桂博客', '', '', 1, 0, NULL, 2, '2026-01-07 19:20:00', '2026-01-07 19:20:00', NULL),
  (1004, 1, 3, '路过黄昏。', '', '', '', '', 'https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4', '/static/logo.png', 0, 0, NULL, 0, '2026-01-06 18:05:00', '2026-01-06 18:05:00', NULL),
  (1005, 1, 0, '只给自己看的小记录。', '', '', '', '', '', '', 1, 0, NULL, 0, '2026-01-05 09:00:00', '2026-01-05 09:00:00', NULL),
  (1006, 1, 1, '图片动态：1 张', '', '', '', '', '', '', 0, 0, NULL, 1, '2026-01-09 07:50:00', '2026-01-09 07:50:00', NULL),
  (1007, 1, 1, '图片动态：2 张', '', '', '', '', '', '', 0, 0, NULL, 0, '2026-01-09 07:40:00', '2026-01-09 07:40:00', NULL),
  (1008, 1, 1, '图片动态：3 张', '', '', '', '', '', '', 0, 0, NULL, 3, '2026-01-09 07:30:00', '2026-01-09 07:30:00', NULL),
  (1009, 1, 1, '图片动态：4 张', '', '', '', '', '', '', 0, 0, NULL, 2, '2026-01-09 07:20:00', '2026-01-09 07:20:00', NULL),
  (1010, 1, 1, '图片动态：5 张', '', '', '', '', '', '', 0, 0, NULL, 4, '2026-01-09 07:10:00', '2026-01-09 07:10:00', NULL),
  (1011, 1, 1, '图片动态：6 张', '', '', '', '', '', '', 0, 0, NULL, 1, '2026-01-09 07:00:00', '2026-01-09 07:00:00', NULL),
  (1012, 1, 1, '图片动态：7 张', '', '', '', '', '', '', 0, 0, NULL, 0, '2026-01-09 06:50:00', '2026-01-09 06:50:00', NULL),
  (1013, 1, 1, '图片动态：8 张', '', '', '', '', '', '', 0, 0, NULL, 2, '2026-01-09 06:40:00', '2026-01-09 06:40:00', NULL),
  (1014, 1, 1, '图片动态：9 张', '', '', '', '', '', '', 0, 0, NULL, 5, '2026-01-09 06:30:00', '2026-01-09 06:30:00', NULL),
  (1015, 1, 1, '图片动态：10 张（测试 +x）', '', '', '', '', '', '', 0, 0, NULL, 6, '2026-01-09 06:20:00', '2026-01-09 06:20:00', NULL);

/*!40000 ALTER TABLE `t_post` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_post_image`
--

DROP TABLE IF EXISTS `t_post_image`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_post_image` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '动态图片标识id',
  `post_id` bigint unsigned NOT NULL COMMENT '动态id（对应t_post.id）',
  `image_url` varchar(1024) NOT NULL COMMENT '图片地址',
  `sort_order` int unsigned NOT NULL DEFAULT '0' COMMENT '图片排序（从0开始，越小越靠前）',
  `width` int unsigned DEFAULT NULL COMMENT '图片宽',
  `height` int unsigned DEFAULT NULL COMMENT '图片高',
  `size_bytes` int unsigned DEFAULT NULL COMMENT '图片大小（字节）',
  `created_at` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
  `updated_at` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='动态图片表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_post_image`
--

LOCK TABLES `t_post_image` WRITE;
/*!40000 ALTER TABLE `t_post_image` DISABLE KEYS */;
INSERT INTO t_post_image (post_id, image_url, sort_order, width, height, size_bytes) VALUES
  (1002, 'https://sangui.top/uploads/covers/20260107/c5ef6deb900a48ac950105e69f78e974/9d8e8f46-b520-4df3-8553-ae59d8b19a40.png', 1, 0, 0, 0),
  (1002, 'https://sangui.top/uploads/covers/20260103/dd57a21ca9334e17a4d97d2fc511bebc/08e664a3-7273-410d-8551-1cce059817d0.png', 2, 0, 0, 0),
  (1002, 'https://sangui.top/uploads/covers/20260102/5161d50d1d3e4f829d19948ae15999cc/8a147c23-1d28-4275-be48-eb143d34e40d.png', 3, 0, 0, 0),
  (1002, 'https://sangui.top/uploads/covers/20260101/20acf760c058471fac90f996c4239655/9506389c-953f-4715-9de7-7fa39f796b1f.png', 4, 0, 0, 0),
  (1002, 'https://sangui.top/uploads/covers/20251230/42f37c189b8e453bae3f9d09fa299561/0081fd5a-623f-4046-afb1-582f98cdfc4f.png', 5, 0, 0, 0),
  (1002, 'https://sangui.top/uploads/covers/20251223/1beaf7a39a0d4048ba4066d303459ee0/5bff3e20-4b9b-4bd9-91eb-9b957e04373a.png', 6, 0, 0, 0),
  (1002, 'https://sangui.top/uploads/covers/20251222/46ff38b58c2145ffa1aace3d8e0bc669/47f3cfaa-cbb8-4b0d-9999-d4f7050d5b6d.png', 7, 0, 0, 0),
  (1006, 'https://sangui.top/uploads/covers/20260103/dd57a21ca9334e17a4d97d2fc511bebc/08e664a3-7273-410d-8551-1cce059817d0.png', 1, 0, 0, 0),
  (1007, 'https://sangui.top/uploads/covers/20260102/5161d50d1d3e4f829d19948ae15999cc/8a147c23-1d28-4275-be48-eb143d34e40d.png', 1, 0, 0, 0),
  (1007, 'https://sangui.top/uploads/covers/20260101/20acf760c058471fac90f996c4239655/9506389c-953f-4715-9de7-7fa39f796b1f.png', 2, 0, 0, 0),
  (1008, 'https://sangui.top/uploads/covers/20260107/c5ef6deb900a48ac950105e69f78e974/9d8e8f46-b520-4df3-8553-ae59d8b19a40.png', 1, 0, 0, 0),
  (1008, 'https://sangui.top/uploads/covers/20260103/dd57a21ca9334e17a4d97d2fc511bebc/08e664a3-7273-410d-8551-1cce059817d0.png', 2, 0, 0, 0),
  (1008, 'https://sangui.top/uploads/covers/20260102/5161d50d1d3e4f829d19948ae15999cc/8a147c23-1d28-4275-be48-eb143d34e40d.png', 3, 0, 0, 0),
  (1009, 'https://sangui.top/uploads/covers/20260103/dd57a21ca9334e17a4d97d2fc511bebc/08e664a3-7273-410d-8551-1cce059817d0.png', 1, 0, 0, 0),
  (1009, 'https://sangui.top/uploads/covers/20260102/5161d50d1d3e4f829d19948ae15999cc/8a147c23-1d28-4275-be48-eb143d34e40d.png', 2, 0, 0, 0),
  (1009, 'https://sangui.top/uploads/covers/20260101/20acf760c058471fac90f996c4239655/9506389c-953f-4715-9de7-7fa39f796b1f.png', 3, 0, 0, 0),
  (1009, 'https://sangui.top/uploads/covers/20251230/42f37c189b8e453bae3f9d09fa299561/0081fd5a-623f-4046-afb1-582f98cdfc4f.png', 4, 0, 0, 0),
  (1010, 'https://sangui.top/uploads/covers/20260102/5161d50d1d3e4f829d19948ae15999cc/8a147c23-1d28-4275-be48-eb143d34e40d.png', 1, 0, 0, 0),
  (1010, 'https://sangui.top/uploads/covers/20260101/20acf760c058471fac90f996c4239655/9506389c-953f-4715-9de7-7fa39f796b1f.png', 2, 0, 0, 0),
  (1010, 'https://sangui.top/uploads/covers/20251230/42f37c189b8e453bae3f9d09fa299561/0081fd5a-623f-4046-afb1-582f98cdfc4f.png', 3, 0, 0, 0),
  (1010, 'https://sangui.top/uploads/covers/20251223/1beaf7a39a0d4048ba4066d303459ee0/5bff3e20-4b9b-4bd9-91eb-9b957e04373a.png', 4, 0, 0, 0),
  (1010, 'https://sangui.top/uploads/covers/20251222/46ff38b58c2145ffa1aace3d8e0bc669/47f3cfaa-cbb8-4b0d-9999-d4f7050d5b6d.png', 5, 0, 0, 0),
  (1011, 'https://sangui.top/uploads/covers/20260107/c5ef6deb900a48ac950105e69f78e974/9d8e8f46-b520-4df3-8553-ae59d8b19a40.png', 1, 0, 0, 0),
  (1011, 'https://sangui.top/uploads/covers/20260103/dd57a21ca9334e17a4d97d2fc511bebc/08e664a3-7273-410d-8551-1cce059817d0.png', 2, 0, 0, 0),
  (1011, 'https://sangui.top/uploads/covers/20260102/5161d50d1d3e4f829d19948ae15999cc/8a147c23-1d28-4275-be48-eb143d34e40d.png', 3, 0, 0, 0),
  (1011, 'https://sangui.top/uploads/covers/20260101/20acf760c058471fac90f996c4239655/9506389c-953f-4715-9de7-7fa39f796b1f.png', 4, 0, 0, 0),
  (1011, 'https://sangui.top/uploads/covers/20251230/42f37c189b8e453bae3f9d09fa299561/0081fd5a-623f-4046-afb1-582f98cdfc4f.png', 5, 0, 0, 0),
  (1011, 'https://sangui.top/uploads/covers/20251223/1beaf7a39a0d4048ba4066d303459ee0/5bff3e20-4b9b-4bd9-91eb-9b957e04373a.png', 6, 0, 0, 0),
  (1012, 'https://sangui.top/uploads/covers/20260103/dd57a21ca9334e17a4d97d2fc511bebc/08e664a3-7273-410d-8551-1cce059817d0.png', 1, 0, 0, 0),
  (1012, 'https://sangui.top/uploads/covers/20260102/5161d50d1d3e4f829d19948ae15999cc/8a147c23-1d28-4275-be48-eb143d34e40d.png', 2, 0, 0, 0),
  (1012, 'https://sangui.top/uploads/covers/20260101/20acf760c058471fac90f996c4239655/9506389c-953f-4715-9de7-7fa39f796b1f.png', 3, 0, 0, 0),
  (1012, 'https://sangui.top/uploads/covers/20251230/42f37c189b8e453bae3f9d09fa299561/0081fd5a-623f-4046-afb1-582f98cdfc4f.png', 4, 0, 0, 0),
  (1012, 'https://sangui.top/uploads/covers/20251223/1beaf7a39a0d4048ba4066d303459ee0/5bff3e20-4b9b-4bd9-91eb-9b957e04373a.png', 5, 0, 0, 0),
  (1012, 'https://sangui.top/uploads/covers/20251222/46ff38b58c2145ffa1aace3d8e0bc669/47f3cfaa-cbb8-4b0d-9999-d4f7050d5b6d.png', 6, 0, 0, 0),
  (1012, 'https://sangui.top/uploads/covers/20251218/2de7c613827b49f4b3f4b3296bd33a7f/91758872-9c74-41d1-9a30-04ec6359598f.png', 7, 0, 0, 0),
  (1013, 'https://sangui.top/uploads/covers/20260102/5161d50d1d3e4f829d19948ae15999cc/8a147c23-1d28-4275-be48-eb143d34e40d.png', 1, 0, 0, 0),
  (1013, 'https://sangui.top/uploads/covers/20260101/20acf760c058471fac90f996c4239655/9506389c-953f-4715-9de7-7fa39f796b1f.png', 2, 0, 0, 0),
  (1013, 'https://sangui.top/uploads/covers/20251230/42f37c189b8e453bae3f9d09fa299561/0081fd5a-623f-4046-afb1-582f98cdfc4f.png', 3, 0, 0, 0),
  (1013, 'https://sangui.top/uploads/covers/20251223/1beaf7a39a0d4048ba4066d303459ee0/5bff3e20-4b9b-4bd9-91eb-9b957e04373a.png', 4, 0, 0, 0),
  (1013, 'https://sangui.top/uploads/covers/20251222/46ff38b58c2145ffa1aace3d8e0bc669/47f3cfaa-cbb8-4b0d-9999-d4f7050d5b6d.png', 5, 0, 0, 0),
  (1013, 'https://sangui.top/uploads/covers/20251218/2de7c613827b49f4b3f4b3296bd33a7f/91758872-9c74-41d1-9a30-04ec6359598f.png', 6, 0, 0, 0),
  (1013, 'https://sangui.top/uploads/covers/20251214/324b5bfe9e824adb8184e5b0a79b8ffc/d043ec4a-70f6-4677-83b1-40e20e787084.png', 7, 0, 0, 0),
  (1013, 'https://sangui.top/uploads/covers/20251214/270c964887344a0f8ab57c542adf338f/46676735-378c-4215-b6fc-2a9eb94198e2.png', 8, 0, 0, 0),
  (1014, 'https://sangui.top/uploads/covers/20260107/c5ef6deb900a48ac950105e69f78e974/9d8e8f46-b520-4df3-8553-ae59d8b19a40.png', 1, 0, 0, 0),
  (1014, 'https://sangui.top/uploads/covers/20260103/dd57a21ca9334e17a4d97d2fc511bebc/08e664a3-7273-410d-8551-1cce059817d0.png', 2, 0, 0, 0),
  (1014, 'https://sangui.top/uploads/covers/20260102/5161d50d1d3e4f829d19948ae15999cc/8a147c23-1d28-4275-be48-eb143d34e40d.png', 3, 0, 0, 0),
  (1014, 'https://sangui.top/uploads/covers/20260101/20acf760c058471fac90f996c4239655/9506389c-953f-4715-9de7-7fa39f796b1f.png', 4, 0, 0, 0),
  (1014, 'https://sangui.top/uploads/covers/20251230/42f37c189b8e453bae3f9d09fa299561/0081fd5a-623f-4046-afb1-582f98cdfc4f.png', 5, 0, 0, 0),
  (1014, 'https://sangui.top/uploads/covers/20251223/1beaf7a39a0d4048ba4066d303459ee0/5bff3e20-4b9b-4bd9-91eb-9b957e04373a.png', 6, 0, 0, 0),
  (1014, 'https://sangui.top/uploads/covers/20251222/46ff38b58c2145ffa1aace3d8e0bc669/47f3cfaa-cbb8-4b0d-9999-d4f7050d5b6d.png', 7, 0, 0, 0),
  (1014, 'https://sangui.top/uploads/covers/20251218/2de7c613827b49f4b3f4b3296bd33a7f/91758872-9c74-41d1-9a30-04ec6359598f.png', 8, 0, 0, 0),
  (1014, 'https://sangui.top/uploads/covers/20251214/324b5bfe9e824adb8184e5b0a79b8ffc/d043ec4a-70f6-4677-83b1-40e20e787084.png', 9, 0, 0, 0),
  (1015, 'https://sangui.top/uploads/covers/20260103/dd57a21ca9334e17a4d97d2fc511bebc/08e664a3-7273-410d-8551-1cce059817d0.png', 1, 0, 0, 0),
  (1015, 'https://sangui.top/uploads/covers/20260102/5161d50d1d3e4f829d19948ae15999cc/8a147c23-1d28-4275-be48-eb143d34e40d.png', 2, 0, 0, 0),
  (1015, 'https://sangui.top/uploads/covers/20260101/20acf760c058471fac90f996c4239655/9506389c-953f-4715-9de7-7fa39f796b1f.png', 3, 0, 0, 0),
  (1015, 'https://sangui.top/uploads/covers/20251230/42f37c189b8e453bae3f9d09fa299561/0081fd5a-623f-4046-afb1-582f98cdfc4f.png', 4, 0, 0, 0),
  (1015, 'https://sangui.top/uploads/covers/20251223/1beaf7a39a0d4048ba4066d303459ee0/5bff3e20-4b9b-4bd9-91eb-9b957e04373a.png', 5, 0, 0, 0),
  (1015, 'https://sangui.top/uploads/covers/20251222/46ff38b58c2145ffa1aace3d8e0bc669/47f3cfaa-cbb8-4b0d-9999-d4f7050d5b6d.png', 6, 0, 0, 0),
  (1015, 'https://sangui.top/uploads/covers/20251218/2de7c613827b49f4b3f4b3296bd33a7f/91758872-9c74-41d1-9a30-04ec6359598f.png', 7, 0, 0, 0),
  (1015, 'https://sangui.top/uploads/covers/20251214/324b5bfe9e824adb8184e5b0a79b8ffc/d043ec4a-70f6-4677-83b1-40e20e787084.png', 8, 0, 0, 0),
  (1015, 'https://sangui.top/uploads/covers/20251214/270c964887344a0f8ab57c542adf338f/46676735-378c-4215-b6fc-2a9eb94198e2.png', 9, 0, 0, 0),
  (1015, 'https://sangui.top/uploads/covers/20260107/c5ef6deb900a48ac950105e69f78e974/9d8e8f46-b520-4df3-8553-ae59d8b19a40.png', 10, 0, 0, 0);

/*!40000 ALTER TABLE `t_post_image` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_post_like`
--

DROP TABLE IF EXISTS `t_post_like`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_post_like` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '动态点赞记录标识id',
  `post_id` bigint unsigned NOT NULL COMMENT '动态id（对应t_post.id）',
  `user_id` bigint unsigned NOT NULL COMMENT '用户id（对应t_user.id）',
  `created_at` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间（点赞时间）',
  `updated_at` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `t_post_like_uk` (`post_id`,`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='动态点赞记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_post_like`
--

LOCK TABLES `t_post_like` WRITE;
/*!40000 ALTER TABLE `t_post_like` DISABLE KEYS */;
INSERT INTO t_post_like (post_id, user_id, created_at) VALUES
  (1001, 1, '2026-01-08 21:20:00'),
  (1002, 1, '2026-01-09 08:35:00');

/*!40000 ALTER TABLE `t_post_like` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_user`
--

DROP TABLE IF EXISTS `t_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_user` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '用户标识id',
  `openid` varchar(64) NOT NULL COMMENT '微信用户标识id',
  `role` enum('OWNER','VIEWER','SUSPENDED') NOT NULL DEFAULT 'VIEWER' COMMENT '角色',
  `nickname` varchar(64) DEFAULT NULL COMMENT '昵称',
  `avatar_url` varchar(512) DEFAULT NULL COMMENT '头像地址',
  `created_at` datetime(3) DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
  `updated_at` datetime(3) DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '修改时间',
  `last_login_at` datetime(3) DEFAULT NULL COMMENT '最后登录于',
  PRIMARY KEY (`id`),
  UNIQUE KEY `t_user_uk` (`openid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_user`
--

LOCK TABLES `t_user` WRITE;
/*!40000 ALTER TABLE `t_user` DISABLE KEYS */;
INSERT INTO t_user (id, openid, role, nickname, avatar_url, created_at, updated_at, last_login_at) VALUES
  (1, 'mock-openid-001', 'OWNER', 'Čýšđ', '', '2026-01-09 08:00:00', '2026-01-09 08:00:00', '2026-01-09 08:00:00');

/*!40000 ALTER TABLE `t_user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-01-10 20:49:11
