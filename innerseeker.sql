CREATE DATABASE  IF NOT EXISTS `innerseeker` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `innerseeker`;
-- MySQL dump 10.13  Distrib 8.0.44, for Win64 (x86_64)
--
-- Host: localhost    Database: innerseeker
-- ------------------------------------------------------
-- Server version	8.0.44

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `draw_history`
--

DROP TABLE IF EXISTS `draw_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `draw_history` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int DEFAULT NULL,
  `card_pk` int NOT NULL,
  `question` varchar(500) NOT NULL,
  `reversed` tinyint(1) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_dh_user_created` (`user_id`,`created_at`),
  KEY `idx_dh_created` (`created_at`),
  KEY `fk_dh_card_idx` (`card_pk`) /*!80000 INVISIBLE */,
  CONSTRAINT `fk_dh_card` FOREIGN KEY (`card_pk`) REFERENCES `rws_tarot` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_dh_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `draw_history`
--

LOCK TABLES `draw_history` WRITE;
/*!40000 ALTER TABLE `draw_history` DISABLE KEYS */;
/*!40000 ALTER TABLE `draw_history` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rws_tarot`
--

DROP TABLE IF EXISTS `rws_tarot`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rws_tarot` (
  `id` int NOT NULL AUTO_INCREMENT,
  `card_id` varchar(10) DEFAULT NULL,
  `card_name_zh` varchar(45) NOT NULL,
  `card_name_en` varchar(45) NOT NULL,
  `suit` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=79 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rws_tarot`
--

LOCK TABLES `rws_tarot` WRITE;
/*!40000 ALTER TABLE `rws_tarot` DISABLE KEYS */;
INSERT INTO `rws_tarot` VALUES (1,'0','愚者','The Fool','major'),(2,'I','魔術師','The Magician','major'),(3,'II','女祭司','The High Priestess','major'),(4,'III','皇后','The Empress','major'),(5,'IV','皇帝','The Emperor','major'),(6,'V','教皇','The Hierophant','major'),(7,'VI','戀人','The Lovers','major'),(8,'VII','戰車','The Chariot','major'),(9,'VIII','力量','Strength','major'),(10,'IX','隱者','The Hermit','major'),(11,'X','命運之輪','Wheel of Fortune','major'),(12,'XI','正義','Justice','major'),(13,'XII','倒吊人','The Hanged Man','major'),(14,'XIII','死神','Death','major'),(15,'XIV','節制','Temperance','major'),(16,'XV','惡魔','The Devil','major'),(17,'XVI','高塔','The Tower','major'),(18,'XVII','星星','The Star','major'),(19,'XVIII','月亮','The Moon','major'),(20,'XIX','太陽','The Sun','major'),(21,'XX','審判','Judgement','major'),(22,'XXI','世界','The World','major'),(23,'1','權杖一','Ace of Wands','wands'),(24,'2','權杖二','Two of Wands','wands'),(25,'3','權杖三','Three of Wands','wands'),(26,'4','權杖四','Four of Wands','wands'),(27,'5','權杖五','Five of Wands','wands'),(28,'6','權杖六','Six of Wands','wands'),(29,'7','權杖七','Seven of Wands','wands'),(30,'8','權杖八','Eight of Wands','wands'),(31,'9','權杖九','Nine of Wands','wands'),(32,'10','權杖十','Ten of Wands','wands'),(33,NULL,'權杖侍者','Page of Wands','wands'),(34,NULL,'權杖騎士','Knight of Wands','wands'),(35,NULL,'權杖皇后','Queen of Wands','wands'),(36,NULL,'權杖國王','King of Wands','wands'),(37,'1','聖杯一','Ace of Cups','cups'),(38,'2','聖杯二','Two of Cups','cups'),(39,'3','聖杯三','Three of Cups','cups'),(40,'4','聖杯四','Four of Cups','cups'),(41,'5','聖杯五','Five of Cups','cups'),(42,'6','聖杯六','Six of Cups','cups'),(43,'7','聖杯七','Seven of Cups','cups'),(44,'8','聖杯八','Eight of Cups','cups'),(45,'9','聖杯九','Nine of Cups','cups'),(46,'10','聖杯十','Ten of Cups','cups'),(47,NULL,'聖杯侍者','Page of Cups','cups'),(48,NULL,'聖杯騎士','Knight of Cups','cups'),(49,NULL,'聖杯皇后','Queen of Cups','cups'),(50,NULL,'聖杯國王','King of Cups','cups'),(51,'1','寶劍一','Ace of Swords','swords'),(52,'2','寶劍二','Two of Swords','swords'),(53,'3','寶劍三','Three of Swords','swords'),(54,'4','寶劍四','Four of Swords','swords'),(55,'5','寶劍五','Five of Swords','swords'),(56,'6','寶劍六','Six of Swords','swords'),(57,'7','寶劍七','Seven of Swords','swords'),(58,'8','寶劍八','Eight of Swords','swords'),(59,'9','寶劍九','Nine of Swords','swords'),(60,'10','寶劍十','Ten of Swords','swords'),(61,NULL,'寶劍侍者','Page of Swords','swords'),(62,NULL,'寶劍騎士','Knight of Swords','swords'),(63,NULL,'寶劍皇后','Queen of Swords','swords'),(64,NULL,'寶劍國王','King of Swords','swords'),(65,'1','錢幣一','Ace of Pentacles','pentacles'),(66,'2','錢幣二','Two of Pentacles','pentacles'),(67,'3','錢幣三','Three of Pentacles','pentacles'),(68,'4','錢幣四','Four of Pentacles','pentacles'),(69,'5','錢幣五','Five of Pentacles','pentacles'),(70,'6','錢幣六','Six of Pentacles','pentacles'),(71,'7','錢幣七','Seven of Pentacles','pentacles'),(72,'8','錢幣八','Eight of Pentacles','pentacles'),(73,'9','錢幣九','Nine of Pentacles','pentacles'),(74,'10','錢幣十','Ten of Pentacles','pentacles'),(75,NULL,'錢幣侍者','Page of Pentacles','pentacles'),(76,NULL,'錢幣騎士','Knight of Pentacles','pentacles'),(77,NULL,'錢幣皇后','Queen of Pentacles','pentacles'),(78,NULL,'錢幣國王','King of Pentacles','pentacles');
/*!40000 ALTER TABLE `rws_tarot` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `hint` varchar(100) DEFAULT NULL,
  `hint_answer` varchar(100) DEFAULT NULL,
  `role` enum('user','admin') NOT NULL DEFAULT 'user',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'admin','admin','hint','hint','admin','2026-02-04 07:47:47');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-02-04 19:19:47
