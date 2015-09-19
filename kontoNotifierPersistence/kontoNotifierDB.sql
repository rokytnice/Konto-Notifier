-- phpMyAdmin SQL Dump
-- version 4.2.6deb1
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Sep 18, 2015 at 06:29 AM
-- Server version: 5.5.44-0ubuntu0.14.10.1
-- PHP Version: 5.5.12-2ubuntu4.6

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `kontoNotifierDB`
--

-- --------------------------------------------------------

--
-- Table structure for table `FILTER`
--

CREATE TABLE IF NOT EXISTS `FILTER` (
  `FILTER_ID` int(11) NOT NULL,
  `ENABLED` bit(1) DEFAULT NULL,
  `LAST_EXEC` datetime DEFAULT NULL,
  `MAX` int(11) DEFAULT NULL,
  `MIN` int(11) DEFAULT NULL,
  `NOTIFY_EMAIL` bit(1) DEFAULT NULL,
  `NOTIFY_PUSH` bit(1) DEFAULT NULL,
  `OPERATOR_AND` bit(1) DEFAULT NULL,
  `OPERATOR_OR` bit(1) DEFAULT NULL,
  `SEARCH` varchar(255) DEFAULT NULL,
  `SEARCH_AUFTRAGG` bit(1) DEFAULT NULL,
  `SEARCH_BUCHUNGTXT` bit(1) DEFAULT NULL,
  `FK_KONTO_ID` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `FILTER`
--

INSERT INTO `FILTER` (`FILTER_ID`, `ENABLED`, `LAST_EXEC`, `MAX`, `MIN`, `NOTIFY_EMAIL`, `NOTIFY_PUSH`, `OPERATOR_AND`, `OPERATOR_OR`, `SEARCH`, `SEARCH_AUFTRAGG`, `SEARCH_BUCHUNGTXT`, `FK_KONTO_ID`) VALUES
(22, b'0', NULL, NULL, 1000, b'0', b'0', b'0', b'0', NULL, b'0', b'0', 17),
(23, b'0', NULL, NULL, NULL, b'0', b'0', b'0', b'0', 'Auszahlung', b'0', b'0', 17),
(26, b'0', NULL, 999999, 1, b'0', b'0', b'0', b'0', NULL, b'0', b'0', 25),
(27, b'0', NULL, NULL, NULL, b'0', b'0', b'0', b'0', 'Lastschrift', b'0', b'0', 17),
(28, b'0', NULL, NULL, 100, b'0', b'0', b'0', b'0', NULL, b'0', b'0', 17);

-- --------------------------------------------------------

--
-- Table structure for table `hibernate_sequence`
--

CREATE TABLE IF NOT EXISTS `hibernate_sequence` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `hibernate_sequence`
--

INSERT INTO `hibernate_sequence` (`next_val`) VALUES
(29);

-- --------------------------------------------------------

--
-- Table structure for table `KONTO`
--

CREATE TABLE IF NOT EXISTS `KONTO` (
  `KONTO_ID` bigint(20) NOT NULL,
  `ACCOUNT` varchar(255) DEFAULT NULL,
  `BIC` varchar(255) DEFAULT NULL,
  `BLZ` int(11) DEFAULT NULL,
  `IBAN` varchar(255) DEFAULT NULL,
  `KONTO_NR` int(11) DEFAULT NULL,
  `PASSWORD` varchar(255) DEFAULT NULL,
  `FK_USER_ID` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `KONTO`
--

INSERT INTO `KONTO` (`KONTO_ID`, `ACCOUNT`, `BIC`, `BLZ`, `IBAN`, `KONTO_NR`, `PASSWORD`, `FK_USER_ID`) VALUES
(17, '30746930', NULL, 20041133, NULL, 4900585, '741896', 1),
(25, '85088014', NULL, 20041133, NULL, 8508806, '824245', 24);

-- --------------------------------------------------------

--
-- Table structure for table `USER`
--

CREATE TABLE IF NOT EXISTS `USER` (
  `USER_ID` bigint(20) NOT NULL,
  `ACTIVE` bit(1) DEFAULT NULL,
  `EMAIL` varchar(255) DEFAULT NULL,
  `PASSWORD` varchar(255) DEFAULT NULL,
  `USERNAME` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `USER`
--

INSERT INTO `USER` (`USER_ID`, `ACTIVE`, `EMAIL`, `PASSWORD`, `USERNAME`) VALUES
(1, b'1', 'andre.rochlitz@gmail.com', '4dff4ea340f0a823f15d3f4f01ab62eae0e5da579ccb851f8db9dfe84c58b2b37b89903a740e1ee172da793a6e79d560e5f7f9bd058a12a280433ed6fa46510a', NULL),
(24, b'1', 'proc213@gmail.com ', 'ba3be38d8b2c0f09f7f745b6810350e2e33fb36d7aab88c1cce918cf0032a73d20434da55d37597d8d075aa52623f053d407fed5ee55e32b38b43fea3f3083f6', NULL);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `FILTER`
--
ALTER TABLE `FILTER`
 ADD PRIMARY KEY (`FILTER_ID`), ADD KEY `FK_e6nbxyl8vpksm9etk7d766bks` (`FK_KONTO_ID`);

--
-- Indexes for table `KONTO`
--
ALTER TABLE `KONTO`
 ADD PRIMARY KEY (`KONTO_ID`), ADD KEY `FK_in0eyo29falb6ypmbmmejmvgb` (`FK_USER_ID`);

--
-- Indexes for table `USER`
--
ALTER TABLE `USER`
 ADD PRIMARY KEY (`USER_ID`), ADD UNIQUE KEY `UK_ejfk3g58oxsgbb4ju3u4fhivk` (`EMAIL`);

--
-- Constraints for dumped tables
--

--
-- Constraints for table `FILTER`
--
ALTER TABLE `FILTER`
ADD CONSTRAINT `FK_e6nbxyl8vpksm9etk7d766bks` FOREIGN KEY (`FK_KONTO_ID`) REFERENCES `KONTO` (`KONTO_ID`) ON DELETE CASCADE ON UPDATE NO ACTION;

--
-- Constraints for table `KONTO`
--
ALTER TABLE `KONTO`
ADD CONSTRAINT `FK_in0eyo29falb6ypmbmmejmvgb` FOREIGN KEY (`FK_USER_ID`) REFERENCES `USER` (`USER_ID`) ON DELETE CASCADE ON UPDATE NO ACTION;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
