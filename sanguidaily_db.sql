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
