/*
SQLyog Community v13.1.6 (64 bit)
MySQL - 8.0.22 : Database - cinema
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`cinema` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `cinema`;

/*Table structure for table `admin` */

DROP TABLE IF EXISTS `admin`;

CREATE TABLE `admin` (
  `adminID` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  `surname` varchar(50) DEFAULT NULL,
  `username` varchar(100) DEFAULT NULL,
  `password` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`adminID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `admin` */

insert  into `admin`(`adminID`,`name`,`surname`,`username`,`password`) values 
(1,'Nikola','Abadic','nikolaabadic','1234');

/*Table structure for table `film` */

DROP TABLE IF EXISTS `film`;

CREATE TABLE `film` (
  `FilmID` bigint NOT NULL AUTO_INCREMENT,
  `Name` varchar(200) DEFAULT NULL,
  `Year` int DEFAULT NULL,
  `Duration` int DEFAULT NULL,
  `Language` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`FilmID`)
) ENGINE=InnoDB AUTO_INCREMENT=42 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `film` */

insert  into `film`(`FilmID`,`Name`,`Year`,`Duration`,`Language`) values 
(6,'Harry Potter and the chamber of secrets',1999,127,'English'),
(7,'Lord of the rings',2001,187,'English'),
(12,'Fight club',2006,143,'English'),
(14,'Wolf of Wall Street',2014,182,'English'),
(17,'The Help',2016,119,'English'),
(19,'300',1999,134,'English'),
(26,'Mrtav ladan',2008,109,'Serbian');

/*Table structure for table `hall` */

DROP TABLE IF EXISTS `hall`;

CREATE TABLE `hall` (
  `HallID` bigint NOT NULL AUTO_INCREMENT,
  `Name` varchar(50) DEFAULT NULL,
  `Capacity` int DEFAULT NULL,
  PRIMARY KEY (`HallID`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `hall` */

insert  into `hall`(`HallID`,`Name`,`Capacity`) values 
(1,'Hall 1',100),
(2,'Hall 2',50),
(3,'Hall 3 ',80),
(4,'Hall 4',100);

/*Table structure for table `reservation` */

DROP TABLE IF EXISTS `reservation`;

CREATE TABLE `reservation` (
  `UserID` bigint NOT NULL,
  `TermID` bigint NOT NULL,
  `Date` datetime DEFAULT NULL,
  `Number` int DEFAULT NULL,
  PRIMARY KEY (`UserID`,`TermID`),
  KEY `reservation_term_fk` (`TermID`),
  CONSTRAINT `reservation_term_fk` FOREIGN KEY (`TermID`) REFERENCES `term` (`TermID`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `reservation_user_fk` FOREIGN KEY (`UserID`) REFERENCES `user` (`UserID`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `reservation` */

insert  into `reservation`(`UserID`,`TermID`,`Date`,`Number`) values 
(2,14,'2021-10-10 10:00:00',1),
(2,15,'2021-06-06 13:00:00',10),
(2,44,'2021-04-15 12:00:00',10);

/*Table structure for table `term` */

DROP TABLE IF EXISTS `term`;

CREATE TABLE `term` (
  `TermID` bigint NOT NULL AUTO_INCREMENT,
  `Date` datetime DEFAULT NULL,
  `Type` varchar(20) DEFAULT NULL,
  `HallID` bigint DEFAULT NULL,
  `FilmID` bigint DEFAULT NULL,
  PRIMARY KEY (`TermID`),
  KEY `hall_fk` (`HallID`),
  KEY `film_fk` (`FilmID`),
  CONSTRAINT `film_fk` FOREIGN KEY (`FilmID`) REFERENCES `film` (`FilmID`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `hall_fk` FOREIGN KEY (`HallID`) REFERENCES `hall` (`HallID`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=60 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `term` */

insert  into `term`(`TermID`,`Date`,`Type`,`HallID`,`FilmID`) values 
(2,'2021-02-18 00:00:00','3D',1,6),
(5,'2021-02-22 00:00:00','2D',4,7),
(14,'2021-03-06 00:00:00','2D',1,12),
(15,'2021-03-06 12:00:00','3D',3,7),
(17,'2021-03-07 07:00:00','2D',1,6),
(18,'2021-03-08 12:00:00','2D',3,7),
(19,'2021-10-10 10:00:00','2D',1,6),
(20,'2021-03-08 21:00:00','2D',1,14),
(22,'2021-03-09 10:00:00','2D',3,14),
(26,'2021-02-09 19:00:00','3D',4,7),
(27,'2021-10-10 21:00:00','3D',1,17),
(30,'2021-04-09 18:00:00','2D',4,19),
(35,'2021-04-15 07:35:00','3D',4,17),
(36,'2021-07-16 18:00:00','2D',1,26),
(37,'2020-12-12 19:00:00','2D',3,26),
(39,'2021-04-20 12:00:00','3D',4,12),
(42,'2020-12-12 11:00:00','3D',1,7),
(44,'2021-04-17 23:50:00','3D',4,6),
(49,'2020-10-10 13:00:00','2D',1,6),
(50,'2021-05-08 19:00:00','2D',1,6),
(53,'2021-05-10 19:00:00','2D',3,19),
(57,'2021-06-06 18:00:00','3D',2,6),
(58,'2021-05-14 15:45:00','3D',3,12),
(59,'2021-05-11 10:45:00','2D',1,6);

/*Table structure for table `user` */

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `UserID` bigint NOT NULL AUTO_INCREMENT,
  `Name` varchar(50) DEFAULT NULL,
  `Surname` varchar(50) DEFAULT NULL,
  `Username` varchar(50) DEFAULT NULL,
  `Password` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`UserID`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `user` */

insert  into `user`(`UserID`,`Name`,`Surname`,`Username`,`Password`) values 
(2,'Mark','Ronson','markronson','1234'),
(3,'John','Parker','johnparker','1234'),
(4,'Emily','Jones','emilyjones','1111');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
