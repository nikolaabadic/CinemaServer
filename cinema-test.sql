/*
SQLyog Community v13.1.6 (64 bit)
MySQL - 8.0.22 : Database - cinema-test
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`cinema-test` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `cinema-test`;

/*Table structure for table `admin` */

DROP TABLE IF EXISTS `admin`;

CREATE TABLE `admin` (
  `adminID` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  `surname` varchar(50) DEFAULT NULL,
  `username` varchar(100) DEFAULT NULL,
  `password` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`adminID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `admin` */

insert  into `admin`(`adminID`,`name`,`surname`,`username`,`password`) values 
(2,'admin','admin','admin','admin');

/*Table structure for table `film` */

DROP TABLE IF EXISTS `film`;

CREATE TABLE `film` (
  `FilmID` bigint NOT NULL AUTO_INCREMENT,
  `Name` varchar(200) DEFAULT NULL,
  `Year` int DEFAULT NULL,
  `Duration` int DEFAULT NULL,
  `Language` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`FilmID`)
) ENGINE=InnoDB AUTO_INCREMENT=306 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `film` */

insert  into `film`(`FilmID`,`Name`,`Year`,`Duration`,`Language`) values 
(40,'test',2021,100,'eng');

/*Table structure for table `hall` */

DROP TABLE IF EXISTS `hall`;

CREATE TABLE `hall` (
  `HallID` bigint NOT NULL AUTO_INCREMENT,
  `Name` varchar(50) DEFAULT NULL,
  `Capacity` int DEFAULT NULL,
  PRIMARY KEY (`HallID`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `hall` */

insert  into `hall`(`HallID`,`Name`,`Capacity`) values 
(5,'hall 1',100);

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
) ENGINE=InnoDB AUTO_INCREMENT=469 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `term` */

insert  into `term`(`TermID`,`Date`,`Type`,`HallID`,`FilmID`) values 
(62,'2021-06-20 12:02:29','2D',5,40);

/*Table structure for table `user` */

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `UserID` bigint NOT NULL AUTO_INCREMENT,
  `Name` varchar(50) DEFAULT NULL,
  `Surname` varchar(50) DEFAULT NULL,
  `Username` varchar(50) DEFAULT NULL,
  `Password` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`UserID`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `user` */

insert  into `user`(`UserID`,`Name`,`Surname`,`Username`,`Password`) values 
(5,'user ','user','user','user');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
