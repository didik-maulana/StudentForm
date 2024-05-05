-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: localhost:8889
-- Generation Time: May 05, 2024 at 03:05 PM
-- Server version: 5.7.39
-- PHP Version: 8.2.0

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `students_db`
--

-- --------------------------------------------------------

--
-- Table structure for table `students`
--

CREATE TABLE `students` (
  `nim` varchar(10) NOT NULL,
  `name` varchar(100) DEFAULT NULL,
  `score_tugas` double DEFAULT NULL,
  `score_quiz` double DEFAULT NULL,
  `score_uts` double DEFAULT NULL,
  `score_uas` double DEFAULT NULL,
  `average` double DEFAULT NULL,
  `grade` varchar(2) DEFAULT NULL,
  `description` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `students`
--

INSERT INTO `students` (`nim`, `name`, `score_tugas`, `score_quiz`, `score_uts`, `score_uas`, `average`, `grade`, `description`) VALUES
('1234567895', 'Hafid Mustaqim', 100, 60, 70, 90, 80, 'B+', 'Dinyatakan Lulus'),
('2702347755', 'Alia Anggraini', 90, 85, 97, 95, 91.75, 'A', 'Dinyatakan Lulus'),
('2702352692', 'Didik Maulana Ardiansyah', 95, 98, 95, 95, 95.75, 'A', 'Dinyatakan Lulus'),
('2702364723', 'William Chandra', 92, 90, 95, 92, 92.25, 'A', 'Dinyatakan Lulus'),
('2702365972', 'Alexander William', 90, 92, 97, 92, 92.75, 'A', 'Dinyatakan Lulus'),
('2702384223', 'Muhammad Utsman', 80, 60, 70, 50, 65, 'C+', 'Dinyatakan Lulus'),
('2702384280', 'James Leopold', 60, 50, 45, 50, 51.25, 'D', 'Dinyatakan Tidak Lulus'),
('2702384290', 'Muhammad Fikri Darno', 95, 92, 90, 92, 92.25, 'A', 'Dinyatakan Lulus');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `students`
--
ALTER TABLE `students`
  ADD PRIMARY KEY (`nim`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
