-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Dec 16, 2023 at 02:38 PM
-- Server version: 10.4.28-MariaDB
-- PHP Version: 8.0.28

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `sportifly`
--

-- --------------------------------------------------------

--
-- Table structure for table `songs`
--

CREATE TABLE `songs` (
  `id` int(11) NOT NULL,
  `title` varchar(255) NOT NULL,
  `artist` varchar(255) DEFAULT NULL,
  `file_path` varchar(255) NOT NULL,
  `thumbnail` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `songs`
--

INSERT INTO `songs` (`id`, `title`, `artist`, `file_path`, `thumbnail`) VALUES
(1, 'Rewrite The Stars', 'James Arthur, Anne-Marie', 'Music/1.wav', 'Music/Thumbnail/1.jpg'),
(2, 'Locked Out Of Heaven', 'Bruno Mars', 'Music/2.wav', 'Music/Thumbnail/2.jpg'),
(3, '18', 'One Direction', 'Music/3.wav', 'Music/Thumbnail/3.jpg'),
(4, 'Blank Space', 'Taylor Swift', 'Music/4.wav', 'Music/Thumbnail/4.jpg'),
(5, 'Top 10 Male Singers In One Song', 'Ed Sheeran, Shawn Mendes, Coldplay, Justin Bieber, Harry Stiles, Sam Smith...', 'Music/5.wav', 'Music/Thumbnail/5.jpg'),
(6, 'lover ', 'Taylor Swift', 'Music/6.wav', 'Music/Thumbnail/6.jpg'),
(7, 'Thank U, Next', 'Ariana Grande', 'Music/7.wav', 'Music/Thumbnail/7.jpg'),
(8, 'Hollywood\'s Bleeding', 'Post Malone', 'Music/8.wav', 'Music/Thumbnail/8.jpg'),
(9, 'Free Spirit ', 'Khalid', 'Music/9.wav', 'Music/Thumbnail/9.jpg'),
(10, 'Happiness Begins', 'Jonas Brothers', 'Music/10.wav', 'Music/Thumbnail/10.jpg'),
(11, 'Cuz I Love You', 'Lizzo', 'Music/11.wav', 'Music/Thumbnail/11.jpg'),
(12, 'Divinely Uninspired To a Hellish Extent', 'Lewis Capaldi', 'Music/12.wav', 'Music/Thumbnail/12.jpg'),
(13, 'Beerbongs & Bentleys', 'Post Malone', 'Music/13.wav', 'Music/Thumbnail/13.jpg'),
(14, 'Sweetener', 'Ariana Grande', 'Music/14.wav', 'Music/Thumbnail/14.jpg'),
(15, 'Frozen 2 Soundtrack', 'Various Artists', 'Music/15.wav', 'Music/Thumbnail/15.jpg'),
(16, 'Astroworld', 'Travis Scott', 'Music/16.wav', 'Music/Thumbnail/16.jpg'),
(17, 'Abbey Road ', 'The Beatles', 'Music/17.wav', 'Music/Thumbnail/17.jpg'),
(18, 'The Eminem Show', 'Eminem', 'Music/18.wav', 'Music/Thumbnail/18.jpg'),
(19, 'Scorpion', 'Drake', 'Music/19.wav', 'Music/Thumbnail/19.jpg'),
(20, 'Thriller', 'Michael Jackson', 'Music/20.wav', 'Music/Thumbnail/20.jpg'),
(21, 'Rumours', 'Fleetwood Mac', 'Music/21.wav', 'Music/Thumbnail/21.jpg'),
(22, 'Back to Black', 'Amy Winehouse', 'Music/22.wav', 'Music/Thumbnail/22.jpg'),
(23, 'Purple Rain', 'Prince and The Revolution', 'Music/23.wav', 'Music/Thumbnail/23.jpg'),
(24, 'Born This Way', 'Lady Gaga', 'Music/24.wav', 'Music/Thumbnail/24.jpg'),
(25, 'Jagged Little Pill', 'Alanis Morissette', 'Music/25.wav', 'Music/Thumbnail/25.jpg');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `songs`
--
ALTER TABLE `songs`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `songs`
--
ALTER TABLE `songs`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=26;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
