-- MySQL dump 10.13  Distrib 5.5.44, for debian-linux-gnu (x86_64)
--
-- Host: localhost    Database: auction_master
-- ------------------------------------------------------
-- Server version	5.5.44-0ubuntu0.14.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `authorities`
--
create database auction_master;
use auction_master;
DROP TABLE IF EXISTS `authorities`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `authorities` (
  `username` varchar(50) NOT NULL,
  `authority` varchar(50) NOT NULL,
  UNIQUE KEY `ix_auth_username` (`username`,`authority`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `authorities`
--

LOCK TABLES `authorities` WRITE;
/*!40000 ALTER TABLE `authorities` DISABLE KEYS */;
INSERT INTO `authorities` VALUES ('abhi','ROLE_ADMIN'),('admin1','ROLE_ADMIN'),('akgupta','ROLE_OBSERVER'),('anshikasteel','ROLE_BIDDER'),('asiasteel','ROLE_BIDDER'),('bidder1','ROLE_BIDDER'),('bidder2','ROLE_BIDDER'),('delhisteel','ROLE_BIDDER'),('goelsales','ROLE_BIDDER'),('observer1','ROLE_OBSERVER'),('pdsharma','ROLE_OBSERVER'),('pdsharma1','ROLE_BIDDER'),('rsgupta','ROLE_OBSERVER'),('shivkumar','ROLE_OBSERVER'),('shriganesh','ROLE_BIDDER'),('superadmin','ROLE_SUPER_ADMIN');
/*!40000 ALTER TABLE `authorities` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `client_details`
--

DROP TABLE IF EXISTS `client_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `client_details` (
  `clientId` int(11) NOT NULL AUTO_INCREMENT,
  `clientName` varchar(40) NOT NULL,
  `clientSchemaName` varchar(45) NOT NULL,
  `clientDescription` varchar(45) DEFAULT NULL,
  `active` varchar(45) DEFAULT NULL,
  `lastUpdatedOn` timestamp NULL DEFAULT NULL,
  `createdOn` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `schemaKey` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`clientId`)
) ENGINE=InnoDB AUTO_INCREMENT=102 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `client_details`
--

LOCK TABLES `client_details` WRITE;
/*!40000 ALTER TABLE `client_details` DISABLE KEYS */;
INSERT INTO `client_details` VALUES (101,'zin','auction_trans_zin','tata steel','A',NULL,'2015-08-19 12:39:10',NULL);
/*!40000 ALTER TABLE `client_details` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `roles` (
  `ROLEID` tinyint(4) NOT NULL,
  `ROLE` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roles`
--

LOCK TABLES `roles` WRITE;
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
INSERT INTO `roles` VALUES (1,'ROLE_ADMIN'),(2,'ROLE_BIDDER');
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `userroles`
--

DROP TABLE IF EXISTS `userroles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `userroles` (
  `USERID` bigint(20) NOT NULL,
  `ROLEID` tinyint(4) NOT NULL,
  `ID` bigint(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`ID`),
  KEY `FK_userroles_users` (`USERID`),
  CONSTRAINT `FK_userroles_users` FOREIGN KEY (`USERID`) REFERENCES `users` (`userId`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `userroles`
--

LOCK TABLES `userroles` WRITE;
/*!40000 ALTER TABLE `userroles` DISABLE KEYS */;
INSERT INTO `userroles` VALUES (1,1,1),(2,2,2),(3,3,3);
/*!40000 ALTER TABLE `userroles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `userId` bigint(20) NOT NULL AUTO_INCREMENT,
  `userName` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL,
  `passwordQuestion` varchar(200) DEFAULT NULL,
  `passwordAnswer` varchar(200) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  `clientId` varchar(10) DEFAULT NULL,
  `enabled` tinyint(1) NOT NULL DEFAULT '1',
  `active` tinyint(4) DEFAULT NULL,
  `LAST_LOGIN_TIME` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `RETRY_COUNT` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`userId`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'admin','e87161df44872ac7d77e97e779dabe51','Favourite Movie','Titanic','jeeva@bizonease.com','101',1,1,'2015-08-20 07:13:58',0),(2,'bidder','e87161df44872ac7d77e97e779dabe51','Favourite Movie','Titanic','vikas.cool@gmail.com','101',1,1,'2015-08-20 06:54:03',0),(3,'observer','e87161df44872ac7d77e97e779dabe51','Favourite Movie','Titanic','jeevaratnam.k@gmail.com','101',1,1,NULL,0),(4,'observer2','e87161df44872ac7d77e97e779dabe51','Favourite Movie','Titanic','kumarr.rakesh@gmail.com','101',1,1,NULL,0),(5,'superadmin','iJo6eRs4dc+uQTV0tT2ku4qQ1T4=','Favourite Movie','Titanic','101','101',1,1,NULL,0),(6,'pdsharma1','wLE3/i15JFnyb/djzORFdKW1qwM=','Favourite Movie','Titanic','kumarr.rakesh@gmail.com','101',1,0,NULL,0);
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

-- Dump completed on 2015-08-20 14:43:05
