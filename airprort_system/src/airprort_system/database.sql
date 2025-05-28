-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: May 28, 2025 at 02:36 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.0.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `flight_reservation_db`
--

-- --------------------------------------------------------

--
-- Table structure for table `administrators`
--

DROP TABLE IF EXISTS `administrators`;
CREATE TABLE `administrators` (
  `admin_id` varchar(36) NOT NULL,
  `user_id` varchar(36) DEFAULT NULL,
  `department` varchar(100) DEFAULT NULL,
  `permissions` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL CHECK (json_valid(`permissions`))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `administrators`
--

INSERT INTO `administrators` (`admin_id`, `user_id`, `department`, `permissions`) VALUES
('ADM001', 'admin_user_001', 'IT Department', '[\"manage_flights\", \"manage_airports\", \"manage_users\", \"generate_reports\"]');

-- --------------------------------------------------------

--
-- Table structure for table `agents`
--

DROP TABLE IF EXISTS `agents`;
CREATE TABLE `agents` (
  `agent_id` varchar(36) NOT NULL,
  `user_id` varchar(36) DEFAULT NULL,
  `airline` varchar(100) DEFAULT NULL,
  `commission_rate` decimal(5,2) DEFAULT NULL,
  `access_level` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `agents`
--

INSERT INTO `agents` (`agent_id`, `user_id`, `airline`, `commission_rate`, `access_level`) VALUES
('AGTE078D85C', '5f2375bc-a08d-425a-9899-49f544e03a13', 'dfhj', 4.00, 1);

-- --------------------------------------------------------

--
-- Table structure for table `airport`
--

DROP TABLE IF EXISTS `airport`;
CREATE TABLE `airport` (
  `airport_code` varchar(10) NOT NULL,
  `name` varchar(20) DEFAULT NULL,
  `city` varchar(10) DEFAULT NULL,
  `country` varchar(10) DEFAULT NULL,
  `terminals` text DEFAULT NULL,
  `facilities` text DEFAULT NULL,
  `time_zone` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `airport`
--

INSERT INTO `airport` (`airport_code`, `name`, `city`, `country`, `terminals`, `facilities`, `time_zone`) VALUES
('ABH', 'ABHA airport', 'Abha', 'KSA', '[\"dd\",\"yy\",\"uu\"]', '[\"yy\",\"oo\",\"mm\"]', 'GMT +3'),
('ALX', 'Alexandria airport', 'alexandria', 'egypt', '[\"aa\",\"ss\",\"dd\",\"ff\"]', '[\"ee\",\"rr\",\"tt\"]', '+2 GMT');

-- --------------------------------------------------------

--
-- Table structure for table `blacklist`
--

DROP TABLE IF EXISTS `blacklist`;
CREATE TABLE `blacklist` (
  `blacklist_id` varchar(30) NOT NULL,
  `user_id` varchar(30) DEFAULT NULL,
  `reason` text DEFAULT NULL,
  `date_added` date DEFAULT NULL,
  `added_by` varchar(30) DEFAULT NULL,
  `is_active` tinyint(1) DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `blacklist`
--

INSERT INTO `blacklist` (`blacklist_id`, `user_id`, `reason`, `date_added`, `added_by`, `is_active`) VALUES
('BL02842467', 'customer_002', 'badass', '2025-05-27', 'ADM001', 0),
('BL36B22AD2', 'customer_001', 'badass', '2025-05-23', 'ADM001', 0),
('BL4578DA8C', 'customer_001', 'nothing', '2025-05-23', 'ADM001', 0),
('BL7B407F7D', 'customer_001', 'non', '2025-05-23', 'ADM001', 0),
('BLBA061E05', 'customer_002', 'badass', '2025-05-27', 'ADM001', 0);

-- --------------------------------------------------------

--
-- Table structure for table `booking`
--

DROP TABLE IF EXISTS `booking`;
CREATE TABLE `booking` (
  `booking_id` varchar(20) NOT NULL,
  `customer_id` varchar(36) DEFAULT NULL,
  `flight_id` varchar(20) DEFAULT NULL,
  `booking_date` datetime DEFAULT NULL,
  `seat_class` varchar(20) DEFAULT NULL,
  `total_price` decimal(10,4) DEFAULT NULL,
  `payment_status` varchar(20) DEFAULT NULL,
  `special_requests` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `booking`
--

INSERT INTO `booking` (`booking_id`, `customer_id`, `flight_id`, `booking_date`, `seat_class`, `total_price`, `payment_status`, `special_requests`) VALUES
('BOOK1e3c25cb', '99e9f935-9160-462b-91c2-495aa4aa504e', 'FL82B88853', '2025-05-25 20:54:03', 'Business', 200.0000, 'Refund Approved', ''),
('BOOK2d744e62', 'customer_002', 'FL82B88853', '2025-05-25 05:15:47', 'Economy', 100.0000, 'Completed', 'dc'),
('BOOK42d4ed84', 'customer_002', 'FL82B88853', '2025-05-25 17:03:33', 'Business', 200.0000, 'Completed', 'excvuol'),
('BOOK6327eef7', 'customer_002', 'FL82B88853', '2025-05-25 17:01:42', 'Business', 200.0000, 'Completed', 'rdy8iul'),
('BOOK87a567de', '99e9f935-9160-462b-91c2-495aa4aa504e', 'FL86BBCA7C', '2025-05-27 17:20:11', 'First Class', 1000.0000, 'Completed', '56w46'),
('BOOK9C172F5A', 'customer_002', 'FL82B88853', '2025-05-28 02:28:20', 'First Class', 300.0000, 'Completed', ''),
('BOOKa72fca89', 'customer_002', 'FL82B88853', '2025-05-25 02:17:42', 'Economy', 100.0000, 'Cancelled', 'fef, Cancellation Reason: not going, Cancellation Reason: x'),
('BOOKc12081b9', '99e9f935-9160-462b-91c2-495aa4aa504e', 'FL82B88853', '2025-05-25 23:14:39', 'First Class', 300.0000, 'Cancelled', 'e6y [CANCELLED: non]'),
('BOOKe8901152', 'customer_002', 'FL82B88853', '2025-05-25 03:36:48', 'Economy', 100.0000, 'Cancelled', 'comfy [CANCELLED: no reason]');

-- --------------------------------------------------------

--
-- Table structure for table `crew_member`
--

DROP TABLE IF EXISTS `crew_member`;
CREATE TABLE `crew_member` (
  `crew_member_id` varchar(10) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `role` varchar(30) DEFAULT NULL,
  `experience_years` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `customers`
--

DROP TABLE IF EXISTS `customers`;
CREATE TABLE `customers` (
  `customer_id` varchar(36) NOT NULL,
  `user_id` varchar(36) DEFAULT NULL,
  `passport_number` varchar(50) DEFAULT NULL,
  `frequent_flyer_number` varchar(50) DEFAULT NULL,
  `preferences` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL CHECK (json_valid(`preferences`))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `customers`
--

INSERT INTO `customers` (`customer_id`, `user_id`, `passport_number`, `frequent_flyer_number`, `preferences`) VALUES
('3503ec0c-a39e-4ccf-8cfa-a0dcf8732aba', '3503ec0c-a39e-4ccf-8cfa-a0dcf8732aba', NULL, NULL, NULL),
('94848713-77b6-4a13-97c2-1baf4c86db28', '94848713-77b6-4a13-97c2-1baf4c86db28', NULL, NULL, NULL),
('99e9f935-9160-462b-91c2-495aa4aa504e', '99e9f935-9160-462b-91c2-495aa4aa504e', '1', NULL, NULL),
('9dbac576-4c32-4e5a-b36c-12148c8507e6', '9dbac576-4c32-4e5a-b36c-12148c8507e6', NULL, NULL, NULL),
('cb85c049-82a0-465a-a8a1-0e54774373a4', 'cb85c049-82a0-465a-a8a1-0e54774373a4', NULL, NULL, NULL),
('CUST22C47DFC', NULL, '1234567', NULL, NULL),
('customer_002', 'customer_002', NULL, NULL, NULL),
('customer_003', '07eef829-3105-44e6-98ec-cd7de2b127aa', NULL, NULL, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `deepfakesuspiciousactivity`
--

DROP TABLE IF EXISTS `deepfakesuspiciousactivity`;
CREATE TABLE `deepfakesuspiciousactivity` (
  `log_id` varchar(30) NOT NULL,
  `user_id` varchar(30) DEFAULT NULL,
  `activity_detail` text NOT NULL,
  `activity_time` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `deepfakeuser`
--

DROP TABLE IF EXISTS `deepfakeuser`;
CREATE TABLE `deepfakeuser` (
  `original_user_id` varchar(30) NOT NULL,
  `detection_score` decimal(4,4) DEFAULT NULL,
  `detection_date` date DEFAULT NULL,
  `is_verified` tinyint(1) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `discounts`
--

DROP TABLE IF EXISTS `discounts`;
CREATE TABLE `discounts` (
  `discount_id` varchar(30) NOT NULL,
  `booking_id` varchar(20) DEFAULT NULL,
  `discount_type` varchar(20) DEFAULT NULL,
  `discount_value` decimal(10,2) DEFAULT NULL,
  `reason` text DEFAULT NULL,
  `applied_by` varchar(36) DEFAULT NULL,
  `applied_at` datetime DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `discounts`
--

INSERT INTO `discounts` (`discount_id`, `booking_id`, `discount_type`, `discount_value`, `reason`, `applied_by`, `applied_at`) VALUES
('DISC6D574B3D', 'BOOK1e3c25cb', 'Percentage', 20.00, 'non', 'AGTE078D85C', '2025-05-27 20:53:30'),
('DISC8875816C', 'BOOK1e3c25cb', 'Percentage', 67.00, 'uu', 'AGTE078D85C', '2025-05-27 22:02:15');

-- --------------------------------------------------------

--
-- Table structure for table `feedback`
--

DROP TABLE IF EXISTS `feedback`;
CREATE TABLE `feedback` (
  `feedback_id` varchar(30) NOT NULL,
  `customer_id` varchar(36) DEFAULT NULL,
  `flight_id` varchar(20) DEFAULT NULL,
  `rating` int(11) DEFAULT NULL CHECK (`rating` between 1 and 5),
  `comment` text DEFAULT NULL,
  `created_at` datetime DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `feedback`
--

INSERT INTO `feedback` (`feedback_id`, `customer_id`, `flight_id`, `rating`, `comment`, `created_at`) VALUES
('FEED06c201b4', 'customer_002', 'FL82B88853', 5, 'good', '2025-05-25 03:56:28'),
('FEED208c5f6b', 'customer_002', 'FL82B88853', 3, 'retxcyui', '2025-05-25 17:07:33');

-- --------------------------------------------------------

--
-- Table structure for table `file_manager`
--

DROP TABLE IF EXISTS `file_manager`;
CREATE TABLE `file_manager` (
  `file_path` varchar(200) NOT NULL,
  `file_type` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `file_manager`
--

INSERT INTO `file_manager` (`file_path`, `file_type`) VALUES
('C:\\Users\\YOUSSEF\\flight_reports\\booking_report_20250528_011915.txt', 'TXT'),
('exports/feedbacks_1748293924687.txt', 'feedback'),
('exports/feedbacks_1748354939488.txt', 'feedback'),
('exports/system_logs_1748285607844.txt', 'log'),
('exports/system_logs_1748354583950.txt', 'log'),
('exports/tickets_report_1748294129303.txt', 'report-tickets');

-- --------------------------------------------------------

--
-- Table structure for table `flight`
--

DROP TABLE IF EXISTS `flight`;
CREATE TABLE `flight` (
  `flight_id` varchar(20) NOT NULL,
  `flight_num` varchar(10) DEFAULT NULL,
  `departure_airport_id` varchar(10) DEFAULT NULL,
  `arrival_airport_id` varchar(10) DEFAULT NULL,
  `departure_time` datetime DEFAULT NULL,
  `arrival_time` datetime DEFAULT NULL,
  `aircraft_type` varchar(50) DEFAULT NULL,
  `total_seats` int(11) DEFAULT NULL,
  `available_seats` int(11) DEFAULT NULL,
  `status` varchar(20) DEFAULT NULL,
  `fare_economy` decimal(10,4) DEFAULT NULL,
  `fare_business` decimal(10,4) DEFAULT NULL,
  `fare_first_class` decimal(10,2) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `flight`
--

INSERT INTO `flight` (`flight_id`, `flight_num`, `departure_airport_id`, `arrival_airport_id`, `departure_time`, `arrival_time`, `aircraft_type`, `total_seats`, `available_seats`, `status`, `fare_economy`, `fare_business`, `fare_first_class`) VALUES
('FL82B88853', '1', 'ABH', 'ALX', '2025-06-01 12:30:00', '2025-06-01 15:30:00', 'th', 97, 99, 'On Time', 100.0000, 200.0000, 300.00),
('FL86BBCA7C', '2', 'ALX', 'ABH', '2025-06-02 12:30:00', '2025-06-02 15:30:00', 'boeing_98009', 49, 50, 'On Time', 500.0000, 800.0000, 1000.00);

-- --------------------------------------------------------

--
-- Table structure for table `flight_flightcrew`
--

DROP TABLE IF EXISTS `flight_flightcrew`;
CREATE TABLE `flight_flightcrew` (
  `flight_id` varchar(10) NOT NULL,
  `crew_member_id` varchar(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `notifications`
--

DROP TABLE IF EXISTS `notifications`;
CREATE TABLE `notifications` (
  `notification_id` varchar(36) NOT NULL,
  `user_id` varchar(36) NOT NULL,
  `message` text NOT NULL,
  `notification_time` datetime NOT NULL,
  `is_read` tinyint(1) DEFAULT 0,
  `sender_role` varchar(50) DEFAULT 'System'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `notifications`
--

INSERT INTO `notifications` (`notification_id`, `user_id`, `message`, `notification_time`, `is_read`, `sender_role`) VALUES
('NOTIF01CC11F9', 'customer_002', 'Your account has been restricted. Reason: badass', '2025-05-27 17:06:40', 0, 'ADMINISTRATOR'),
('NOTIF0b2892d5-e', 'customer_002', 'New booking confirmed! Booking ID: BOOK9C172F5A', '2025-05-28 02:28:26', 0, 'System'),
('NOTIF10D91E06', 'admin_user_001', 'User yyy blacklisted. Reason: badass', '2025-05-27 00:17:06', 0, 'System'),
('NOTIF13EB7C27', 'admin_user_001', 'Report \'feedback\' exported to exports/feedbacks_1748293924687.txt', '2025-05-27 00:12:08', 0, 'System'),
('NOTIF14bce4f6', '99e9f935-9160-462b-91c2-495aa4aa504e', 'New booking confirmed! Booking ID: BOOK87a567de', '2025-05-27 17:20:13', 0, 'System'),
('NOTIF158D2E26', '99e9f935-9160-462b-91c2-495aa4aa504e', 'Your booking BOOK1e3c25cb has been updated by an agent. New price: $7888.00', '2025-05-27 22:14:53', 0, 'Agent'),
('NOTIF1C5F176F', 'admin_user_001', 'User kk deleted.', '2025-05-27 17:02:07', 0, 'System'),
('NOTIF2219139E', 'customer_002', 'Unblock request from user: yy@gmail.com', '2025-05-27 17:07:09', 0, 'CUSTOMER'),
('NOTIF386375D4', 'admin_user_001', 'System logs exported to exports/system_logs_1748354583950.txt', '2025-05-27 17:03:07', 0, 'System'),
('NOTIF3b220b1b', 'customer_001', 'Payment method PAYM002 set as default (simulated).', '2025-05-24 13:10:54', 1, 'System'),
('NOTIF3BF313A2', 'admin_user_001', 'User yyy blacklisted. Reason: badass', '2025-05-27 17:06:40', 0, 'System'),
('NOTIF3d795859', 'customer_002', 'Booking BOOKa72fca89 cancelled. Reason: x', '2025-05-25 02:58:06', 1, 'System'),
('NOTIF41dcc726-c', 'customer_002', 'Payment completed for Booking ID: BOOK9C172F5A', '2025-05-28 02:28:27', 0, 'System'),
('NOTIF52b2c04b', '99e9f935-9160-462b-91c2-495aa4aa504e', 'Payment completed for Booking ID: BOOKc12081b9', '2025-05-25 23:15:23', 0, 'System'),
('NOTIF5e1652d3', 'customer_002', 'New feedback submitted for Flight: 1', '2025-05-25 17:07:34', 0, 'System'),
('NOTIF67E8CA32', 'customer_002', 'Your account has been restricted. Reason: badass', '2025-05-27 00:17:06', 0, 'ADMINISTRATOR'),
('NOTIF69b69ee0', 'customer_002', 'New feedback submitted for Flight: 1', '2025-05-25 03:56:30', 1, 'System'),
('NOTIF7f5ceaae', '99e9f935-9160-462b-91c2-495aa4aa504e', 'New booking confirmed! Booking ID: BOOKc12081b9', '2025-05-25 23:14:53', 0, 'System'),
('NOTIF80A9E680', 'admin_user_001', 'User yyy unblocked.', '2025-05-27 00:19:24', 0, 'System'),
('NOTIF883f10ba', 'customer_002', 'New booking confirmed! Booking ID: BOOK6327eef7', '2025-05-25 17:01:45', 0, 'System'),
('NOTIF8CBBEB6E', 'customer_002', 'Unblock request from user: yy@gmail.com', '2025-05-27 00:17:48', 0, 'CUSTOMER'),
('NOTIF8D60316B', 'admin_user_001', 'Report \'feedback\' exported to exports/feedbacks_1748354939488.txt', '2025-05-27 17:09:02', 0, 'System'),
('NOTIF9041bc70-e', 'customer_002', 'Block request for customer: customer_002', '2025-05-28 02:22:19', 0, 'BlockRequest'),
('NOTIF92356387', 'admin_user_001', 'User yyy unblocked.', '2025-05-27 17:07:48', 0, 'System'),
('NOTIF9707ca01', 'customer_002', 'New booking confirmed! Booking ID: BOOK2d744e62', '2025-05-25 05:15:51', 0, 'System'),
('NOTIFa4aa928f', '99e9f935-9160-462b-91c2-495aa4aa504e', 'Payment completed for Booking ID: BOOK87a567de', '2025-05-27 17:21:47', 0, 'System'),
('NOTIFA4FBA06F', '99e9f935-9160-462b-91c2-495aa4aa504e', 'A discount has been applied to your booking BOOK1e3c25cb. New total price: $240.00. Reason: non', '2025-05-27 20:53:32', 0, 'Agent'),
('NOTIFb9d07164', 'customer_002', 'Refund request submitted for Booking ID: BOOKe8901152. Reason: non', '2025-05-25 17:08:52', 0, 'System'),
('NOTIFC36EBE02', '99e9f935-9160-462b-91c2-495aa4aa504e', 'Your refund request for booking BOOK1e3c25cb has been approved. The amount will be processed shortly.', '2025-05-27 22:18:07', 0, 'System'),
('NOTIFc4a9d3ee', 'customer_002', 'New booking confirmed! Booking ID: BOOKa72fca89', '2025-05-25 02:17:51', 1, 'System'),
('NOTIFcd6fe891', 'customer_002', 'New booking confirmed! Booking ID: BOOK42d4ed84', '2025-05-25 17:03:35', 0, 'System'),
('NOTIFCFC0F57F', '99e9f935-9160-462b-91c2-495aa4aa504e', 'A discount has been applied to your booking BOOK1e3c25cb. New total price: $79.20. Reason: uu', '2025-05-27 22:02:17', 0, 'Agent'),
('NOTIFd57907cb', '99e9f935-9160-462b-91c2-495aa4aa504e', 'New booking confirmed! Booking ID: BOOK1e3c25cb', '2025-05-25 20:54:07', 0, 'System'),
('NOTIFD91C2E55', '07eef829-3105-44e6-98ec-cd7de2b127aa', 'Unblock request from user: r@gmail.com', '2025-05-26 21:49:11', 0, 'CUSTOMER'),
('NOTIFe8d60379', 'customer_002', 'Payment completed for Booking ID: BOOK2d744e62', '2025-05-25 16:59:43', 0, 'System'),
('NOTIFeae3d88f', 'customer_002', 'New booking confirmed! Booking ID: BOOKe8901152', '2025-05-25 03:36:51', 1, 'System'),
('NOTIFf56c3bb5', 'customer_002', 'Booking BOOKa72fca89 cancelled. Reason: not going', '2025-05-25 02:56:53', 1, 'System'),
('NOTIFFC4A967C', 'admin_user_001', 'Report \'report-tickets\' exported to exports/tickets_report_1748294129303.txt', '2025-05-27 00:15:34', 0, 'System');

-- --------------------------------------------------------

--
-- Table structure for table `passenger`
--

DROP TABLE IF EXISTS `passenger`;
CREATE TABLE `passenger` (
  `passenger_id` varchar(30) NOT NULL,
  `first_name` varchar(30) NOT NULL,
  `last_name` varchar(30) NOT NULL,
  `date_of_birth` date NOT NULL,
  `passport_number` varchar(50) NOT NULL,
  `nationality` varchar(30) NOT NULL,
  `gender` varchar(10) NOT NULL,
  `special_assistance` tinyint(1) DEFAULT 0,
  `meal_preference` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `passenger`
--

INSERT INTO `passenger` (`passenger_id`, `first_name`, `last_name`, `date_of_birth`, `passport_number`, `nationality`, `gender`, `special_assistance`, `meal_preference`) VALUES
('PASS395c3ca2', 'ffdsbs', 'vfsdf', '2006-03-19', '1234567', 'fv', 'Male', 0, 'fv'),
('PASS3b8726f7', 'youssef', 'karem', '2006-03-19', 'AE1234 7887', 'Egyptian', 'Male', 1, 'meat'),
('PASS4c60dcfc', 'ftu', 'g8i', '2350-08-17', '1234567890', 'cyuiun', 'Male', 0, 'tf8iu'),
('PASS86b42dbb', '13456578', 'srdtfyuk', '1238-10-17', 'es7tyik', 'ds7yuik', 'Male', 0, 'dtxuyk'),
('PASS9db9faf7', 'y', 'w', '2006-03-19', '6896789', 'dwc', 'Male', 1, 'sc'),
('PASSA0C93D0C', 'ydyj', 'f68uj', '2025-05-28', '12345323456', 'xryh', 'Male', 0, ''),
('PASSa10fb258', 'id76yu', 'ryvik', '4572-07-27', '34567', 'xtujh', 'Male', 0, 'syt'),
('PASSafe5c4ca', 'tuyj', 'yfu', '2449-07-25', '234567845678', 'rdxc', 'Male', 0, 'a46tujhb'),
('PASSeef82173', 'azx6tyjh', 'a6ryh', '1238-10-17', '123456789', 'gcuj', 'Male', 1, 'du7tn');

-- --------------------------------------------------------

--
-- Table structure for table `payment`
--

DROP TABLE IF EXISTS `payment`;
CREATE TABLE `payment` (
  `payment_id` varchar(30) NOT NULL,
  `booking_id` varchar(30) DEFAULT NULL,
  `amount` decimal(10,4) DEFAULT NULL,
  `payment_date` datetime DEFAULT NULL,
  `payment_method` varchar(20) DEFAULT NULL,
  `payment_status` varchar(20) DEFAULT NULL,
  `transaction_id` varchar(50) DEFAULT NULL,
  `card_last_four_digits` varchar(4) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `payment`
--

INSERT INTO `payment` (`payment_id`, `booking_id`, `amount`, `payment_date`, `payment_method`, `payment_status`, `transaction_id`, `card_last_four_digits`) VALUES
('PAY219438da', 'BOOKa72fca89', 100.0000, '2025-05-25 02:17:42', 'Credit Card', 'Completed', 'TXNb6431f18', 'XXXX'),
('PAY361e2bc3', 'BOOK2d744e62', 100.0000, '2025-05-25 05:15:48', 'Credit Card', 'Completed', 'TXN68c19fe7', 'XXXX'),
('PAY4170c638', 'BOOKe8901152', 100.0000, '2025-05-25 03:36:48', 'Credit Card', 'Refund Requested', 'TXN3e9ce34b', 'XXXX'),
('PAY57276604', 'BOOK9C172F5A', 300.0000, '2025-05-28 02:28:21', 'Credit Card', 'Completed', 'TXN463fec4a-9', NULL),
('PAY8ca07066', 'BOOK2d744e62', 100.0000, '2025-05-25 16:59:39', 'Credit Card', 'Completed', 'TXNbc5f55b7', '6745'),
('PAYa36aa8a8', 'BOOK87a567de', 1000.0000, '2025-05-27 17:21:36', 'Credit Card', 'Completed', 'TXN0c0fa60a', '0123'),
('PAYbed6912b', 'BOOK6327eef7', 200.0000, '2025-05-25 17:01:42', 'Credit Card', 'Completed', 'TXN1748181702168', '6745'),
('PAYc44425ec', 'BOOKc12081b9', 300.0000, '2025-05-25 23:15:21', 'Credit Card', 'Completed', 'TXN87762285', '0123'),
('PAYdaf73926', 'BOOK42d4ed84', 200.0000, '2025-05-25 17:03:33', 'Credit Card', 'Completed', 'TXN1748181813478', '2345'),
('PAYf3912d9e', 'BOOK1e3c25cb', 300.0000, '2025-05-25 20:54:04', 'Credit Card', 'Completed', 'TXN1748195644513', '0123');

-- --------------------------------------------------------

--
-- Table structure for table `payment_methods`
--

DROP TABLE IF EXISTS `payment_methods`;
CREATE TABLE `payment_methods` (
  `method_id` varchar(255) NOT NULL,
  `user_id` varchar(255) NOT NULL,
  `method_type` varchar(50) NOT NULL,
  `card_last_four_digits` varchar(4) NOT NULL,
  `expiry_date` varchar(5) NOT NULL,
  `card_holder_name` varchar(255) NOT NULL,
  `is_default` tinyint(1) DEFAULT 0,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `payment_methods`
--

INSERT INTO `payment_methods` (`method_id`, `user_id`, `method_type`, `card_last_four_digits`, `expiry_date`, `card_holder_name`, `is_default`, `created_at`) VALUES
('PM-3c105a1e', '99e9f935-9160-462b-91c2-495aa4aa504e', 'Credit Card', '0123', '99/89', 'dfghj', 1, '2025-05-25 15:57:39'),
('PM-bd2fe63d', 'customer_002', 'Credit Card', '2345', '12/34', 'esdfghj', 1, '2025-05-25 14:03:18');

-- --------------------------------------------------------

--
-- Table structure for table `refundrequests`
--

DROP TABLE IF EXISTS `refundrequests`;
CREATE TABLE `refundrequests` (
  `RefundID` int(11) NOT NULL,
  `booking_id` varchar(20) DEFAULT NULL,
  `customer_id` varchar(36) DEFAULT NULL,
  `Amount` decimal(10,4) DEFAULT NULL,
  `RequestDate` datetime DEFAULT NULL,
  `Status` varchar(50) DEFAULT NULL,
  `reason` longtext DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `refundrequests`
--

INSERT INTO `refundrequests` (`RefundID`, `booking_id`, `customer_id`, `Amount`, `RequestDate`, `Status`, `reason`) VALUES
(1, 'BOOK1e3c25cb', '99e9f935-9160-462b-91c2-495aa4aa504e', 300.0000, '2025-05-26 01:34:32', 'Approved', 'non');

-- --------------------------------------------------------

--
-- Table structure for table `system_logs`
--

DROP TABLE IF EXISTS `system_logs`;
CREATE TABLE `system_logs` (
  `log_id` varchar(30) NOT NULL,
  `user_id` varchar(36) DEFAULT NULL,
  `action` text DEFAULT NULL,
  `log_type` varchar(20) DEFAULT NULL,
  `log_time` datetime DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `system_logs`
--

INSERT INTO `system_logs` (`log_id`, `user_id`, `action`, `log_type`, `log_time`) VALUES
('LOG08b60243', 'admin_user_001', 'User logged in: youssef@gmail.com', 'LOGIN', '2025-05-27 03:04:28'),
('LOG09728561', 'admin_user_001', 'User logged in: youssef@gmail.com', 'LOGIN', '2025-05-27 17:05:45'),
('LOG0d3cb218', '5f2375bc-a08d-425a-9899-49f544e03a13', 'User logged in: ag@gmail.com', 'LOGIN', '2025-05-27 22:17:56'),
('LOG111b1415', 'admin_user_001', 'User logged in: youssef@gmail.com', 'LOGIN', '2025-05-27 03:15:19'),
('LOG15bc93c6', '5f2375bc-a08d-425a-9899-49f544e03a13', 'User logged in: ag@gmail.com', 'LOGIN', '2025-05-28 00:39:41'),
('LOG172bf927', '5f2375bc-a08d-425a-9899-49f544e03a13', 'User logged in: ag@gmail.com', 'LOGIN', '2025-05-27 14:38:53'),
('LOG18a67f12', 'admin_user_001', 'User logged in: youssef@gmail.com', 'LOGIN', '2025-05-27 03:28:12'),
('LOG1dfff842', '99e9f935-9160-462b-91c2-495aa4aa504e', 'User logged in: s@gmail.com', 'LOGIN', '2025-05-27 17:11:08'),
('LOG1fd3bce2', 'admin_user_001', 'User logged in: youssef@gmail.com', 'LOGIN', '2025-05-27 00:11:21'),
('LOG20376bf1', '5f2375bc-a08d-425a-9899-49f544e03a13', 'User logged in: ag@gmail.com', 'LOGIN', '2025-05-27 21:54:34'),
('LOG22e05a0c', 'admin_user_001', 'User logged in: youssef@gmail.com', 'LOGIN', '2025-05-26 21:51:31'),
('LOG24d93dd3', 'admin_user_001', 'User logged in: youssef@gmail.com', 'LOGIN', '2025-05-26 20:25:58'),
('LOG263c69a8', 'admin_user_001', 'User logged in: youssef@gmail.com', 'LOGIN', '2025-05-27 00:18:18'),
('LOG2c534375', 'customer_001', 'User logged in: y@gmail.com', 'LOGIN', '2025-05-27 00:17:30'),
('LOG2cdfb734', 'admin_user_001', 'User logged in: youssef@gmail.com', 'LOGIN', '2025-05-27 03:18:58'),
('LOG3192b16d', 'customer_002', 'User logged in: yy@gmail.com', 'LOGIN', '2025-05-28 02:54:19'),
('LOG337d7390', '5f2375bc-a08d-425a-9899-49f544e03a13', 'User logged in: ag@gmail.com', 'LOGIN', '2025-05-28 02:27:34'),
('LOG3cbefe8e', 'admin_user_001', 'User logged in: youssef@gmail.com', 'LOGIN', '2025-05-27 00:33:28'),
('LOG3e85ed97', '99e9f935-9160-462b-91c2-495aa4aa504e', 'User logged in: s@gmail.com', 'LOGIN', '2025-05-26 18:49:16'),
('LOG451d90cf', 'admin_user_001', 'User logged in: youssef@gmail.com', 'LOGIN', '2025-05-27 17:07:23'),
('LOG4df8afca', '99e9f935-9160-462b-91c2-495aa4aa504e', 'User logged in: s@gmail.com', 'LOGIN', '2025-05-27 03:45:16'),
('LOG4eb5e87f', 'admin_user_001', 'User logged in: youssef@gmail.com', 'LOGIN', '2025-05-27 01:54:40'),
('LOG53c2bd37', '5f2375bc-a08d-425a-9899-49f544e03a13', 'User logged in: ag@gmail.com', 'LOGIN', '2025-05-28 01:36:43'),
('LOG567d6347', '5f2375bc-a08d-425a-9899-49f544e03a13', 'User logged in: ag@gmail.com', 'LOGIN', '2025-05-28 01:54:13'),
('LOG5ae2616f', 'customer_002', 'User logged in: yy@gmail.com', 'LOGIN', '2025-05-28 03:07:37'),
('LOG60195d26', '5f2375bc-a08d-425a-9899-49f544e03a13', 'User logged in: ag@gmail.com', 'LOGIN', '2025-05-28 02:15:41'),
('LOG618c6210', '5f2375bc-a08d-425a-9899-49f544e03a13', 'User logged in: ag@gmail.com', 'LOGIN', '2025-05-27 22:12:33'),
('LOG658f6599', 'customer_001', 'User logged in: y@gmail.com', 'LOGIN', '2025-05-28 02:41:50'),
('LOG66c3a784', '5f2375bc-a08d-425a-9899-49f544e03a13', 'User logged in: ag@gmail.com', 'LOGIN', '2025-05-27 20:21:42'),
('LOG678c0ab1', 'admin_user_001', 'User logged in: youssef@gmail.com', 'LOGIN', '2025-05-27 03:32:47'),
('LOG7486e85c', 'admin_user_001', 'User logged in: youssef@gmail.com', 'LOGIN', '2025-05-27 17:00:30'),
('LOG7bdbe471', 'admin_user_001', 'User logged in: youssef@gmail.com', 'LOGIN', '2025-05-27 03:24:11'),
('LOG7dab3f41', '5f2375bc-a08d-425a-9899-49f544e03a13', 'User logged in: ag@gmail.com', 'LOGIN', '2025-05-28 01:09:26'),
('LOG8283f1c2', 'admin_user_001', 'User logged in: youssef@gmail.com', 'LOGIN', '2025-05-27 02:55:40'),
('LOG84b09dda', 'customer_002', 'User logged in: yy@gmail.com', 'LOGIN', '2025-05-28 02:51:16'),
('LOG97228545', '5f2375bc-a08d-425a-9899-49f544e03a13', 'User logged in: ag@gmail.com', 'LOGIN', '2025-05-28 02:00:59'),
('LOG9a83c88b', '5f2375bc-a08d-425a-9899-49f544e03a13', 'User logged in: ag@gmail.com', 'LOGIN', '2025-05-28 02:22:01'),
('LOG9fea4272', '5f2375bc-a08d-425a-9899-49f544e03a13', 'User logged in: ag@gmail.com', 'LOGIN', '2025-05-27 20:29:15'),
('LOGa0013791', 'admin_user_001', 'User logged in: youssef@gmail.com', 'LOGIN', '2025-05-27 00:28:28'),
('LOGb2f653be', 'customer_002', 'User logged in: yy@gmail.com', 'LOGIN', '2025-05-28 02:46:33'),
('LOGbb94bf32', '5f2375bc-a08d-425a-9899-49f544e03a13', 'User logged in: ag@gmail.com', 'LOGIN', '2025-05-27 00:37:29'),
('LOGc7d2fe75', 'admin_user_001', 'User logged in: youssef@gmail.com', 'LOGIN', '2025-05-27 03:31:03'),
('LOGce6bf973', 'customer_002', 'User logged in: yy@gmail.com', 'LOGIN', '2025-05-28 03:03:58'),
('LOGd709bf4b', 'admin_user_001', 'User logged in: youssef@gmail.com', 'LOGIN', '2025-05-27 03:43:04'),
('LOGd921cb30', 'admin_user_001', 'User logged in: youssef@gmail.com', 'LOGIN', '2025-05-26 18:51:37'),
('LOGe87c5243', '5f2375bc-a08d-425a-9899-49f544e03a13', 'User logged in: ag@gmail.com', 'LOGIN', '2025-05-28 01:14:58'),
('LOGef5337fa', 'admin_user_001', 'User logged in: youssef@gmail.com', 'LOGIN', '2025-05-27 00:22:37');

-- --------------------------------------------------------

--
-- Table structure for table `system_settings`
--

DROP TABLE IF EXISTS `system_settings`;
CREATE TABLE `system_settings` (
  `setting_key` varchar(50) NOT NULL,
  `setting_value` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `ticket`
--

DROP TABLE IF EXISTS `ticket`;
CREATE TABLE `ticket` (
  `ticket_id` varchar(30) NOT NULL,
  `booking_id` varchar(30) DEFAULT NULL,
  `passenger_id` varchar(30) DEFAULT NULL,
  `seat_number` varchar(10) DEFAULT NULL,
  `boarding_time` datetime DEFAULT NULL,
  `gate_number` varchar(10) DEFAULT NULL,
  `ticket_status` varchar(20) DEFAULT NULL,
  `barcode` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `ticket`
--

INSERT INTO `ticket` (`ticket_id`, `booking_id`, `passenger_id`, `seat_number`, `boarding_time`, `gate_number`, `ticket_status`, `barcode`) VALUES
('TICKET0e5915b3', 'BOOKc12081b9', 'PASSafe5c4ca', 'd6b', '2025-06-01 12:30:00', 'TBD', 'Cancelled', 'BC-1408667261748204081875'),
('TICKET1542ec19', 'BOOK42d4ed84', 'PASS86b42dbb', 'zyujh', '2025-06-01 12:30:00', 'TBD', 'Confirmed', 'BC19179379081748181813475'),
('TICKET243ed504', 'BOOK6327eef7', 'PASS4c60dcfc', 'yihiop', '2025-06-01 12:30:00', 'TBD', 'Confirmed', 'BC-15033263471748181702082'),
('TICKET330e7862', 'BOOK2d744e62', 'PASS9db9faf7', '33dd', '2025-06-01 12:30:00', 'TBD', 'Confirmed', 'BC-7352446741748139347921'),
('TICKET92101a76', 'BOOKa72fca89', 'PASS395c3ca2', '22', '2025-06-01 12:30:00', 'TBD', 'Cancelled', 'BC2237222911748128662035'),
('TICKETba70c5f8', 'BOOK87a567de', 'PASSeef82173', 'drtug', '2025-06-02 12:30:00', 'TBD', 'Confirmed', 'BC18956929681748355611522'),
('TICKETdf73271c', 'BOOKe8901152', 'PASS3b8726f7', 'f_22', '2025-06-01 12:30:00', 'TBD', 'Cancelled', 'BC12302085771748133408565'),
('TICKETf770a7d3', 'BOOK1e3c25cb', 'PASSa10fb258', 'ud6yfv', '2025-06-01 12:30:00', 'TBD', 'Confirmed', 'BC16564930511748195644245'),
('TKTC6FF4B01', 'BOOK9C172F5A', 'PASSA0C93D0C', '12', NULL, NULL, 'Confirmed', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `user_id` varchar(36) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password_hash` varchar(100) NOT NULL,
  `email` varchar(100) DEFAULT NULL,
  `phone_number` varchar(20) DEFAULT NULL,
  `address` text DEFAULT NULL,
  `registration_date` datetime DEFAULT NULL,
  `last_login` datetime DEFAULT NULL,
  `role` varchar(50) DEFAULT 'CUSTOMER',
  `is_blacklisted` tinyint(1) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`user_id`, `username`, `password_hash`, `email`, `phone_number`, `address`, `registration_date`, `last_login`, `role`, `is_blacklisted`) VALUES
('07eef829-3105-44e6-98ec-cd7de2b127aa', 'rrr', '123', 'r@gmail.com', '123', '', NULL, '2025-05-26 21:49:01', 'CUSTOMER', 0),
('3503ec0c-a39e-4ccf-8cfa-a0dcf8732aba', 'uu', '123', 'u@gmail.com', '1', '', NULL, NULL, 'CUSTOMER', 0),
('5f2375bc-a08d-425a-9899-49f544e03a13', 'ag_001', '123', 'ag@gmail.com', '123', '1234rfvbn', '2025-05-27 00:36:55', '2025-05-28 02:27:31', 'AGENT', 0),
('99e9f935-9160-462b-91c2-495aa4aa504e', 'ss', '123', 's@gmail.com', '1234', '6', NULL, '2025-05-27 17:11:05', 'CUSTOMER', 0),
('admin_user_001', 'AdminYoussef', '123', 'youssef@gmail.com', '1234567890', '123 Admin St', NULL, '2025-05-27 17:07:21', 'ADMINISTRATOR', 0),
('cb85c049-82a0-465a-a8a1-0e54774373a4', 'josepe', '123', 'j@gmail.com', '12345678', '', NULL, NULL, 'CUSTOMER', 0),
('CUST22C47DFC', 'lll', 'hashed_123', 'l@gmail.com', '123', 'i', '2025-05-27 20:32:34', NULL, 'CUSTOMER', 0),
('customer_001', 'joe', '123', 'y@gmail.com', '1010523206', '', NULL, '2025-05-28 02:41:47', 'CUSTOMER', 0),
('customer_002', 'yyy', '123', 'yy@gmail.com', '123456', '', NULL, '2025-05-28 03:07:35', 'CUSTOMER', 0);

--
-- Triggers `users`
--
DROP TRIGGER IF EXISTS `generate_user_id`;
DELIMITER $$
CREATE TRIGGER `generate_user_id` BEFORE INSERT ON `users` FOR EACH ROW BEGIN
    DECLARE prefix VARCHAR(2);
    DECLARE next_id INT;
    DECLARE full_id VARCHAR(36);

    IF NEW.user_id IS NULL THEN
        IF NEW.role = 'ADMINISTRATOR' THEN
            SET prefix = '00';
        ELSEIF NEW.role = 'AGENT' THEN
            SET prefix = '01';
        ELSE
            SET prefix = '02'; -- Default to CUSTOMER
        END IF;

        SELECT IFNULL(MAX(CAST(SUBSTRING(user_id, 3) AS UNSIGNED)), 0) + 1
        INTO next_id
        FROM users
        WHERE user_id LIKE CONCAT(prefix, '%');

        SET full_id = CONCAT(prefix, LPAD(next_id, 3, '0'));
        SET NEW.user_id = full_id;
    END IF;
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `waitlist`
--

DROP TABLE IF EXISTS `waitlist`;
CREATE TABLE `waitlist` (
  `waitlist_id` varchar(30) NOT NULL,
  `flight_id` varchar(30) DEFAULT NULL,
  `creation_date` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `waitlist_customers`
--

DROP TABLE IF EXISTS `waitlist_customers`;
CREATE TABLE `waitlist_customers` (
  `waitlist_id` varchar(30) NOT NULL,
  `customer_id` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `administrators`
--
ALTER TABLE `administrators`
  ADD PRIMARY KEY (`admin_id`);

--
-- Indexes for table `agents`
--
ALTER TABLE `agents`
  ADD PRIMARY KEY (`agent_id`);

--
-- Indexes for table `airport`
--
ALTER TABLE `airport`
  ADD PRIMARY KEY (`airport_code`);

--
-- Indexes for table `blacklist`
--
ALTER TABLE `blacklist`
  ADD PRIMARY KEY (`blacklist_id`),
  ADD KEY `user_id` (`user_id`),
  ADD KEY `added_by` (`added_by`);

--
-- Indexes for table `booking`
--
ALTER TABLE `booking`
  ADD PRIMARY KEY (`booking_id`),
  ADD KEY `flight_id` (`flight_id`),
  ADD KEY `booking_ibfk_1` (`customer_id`);

--
-- Indexes for table `crew_member`
--
ALTER TABLE `crew_member`
  ADD PRIMARY KEY (`crew_member_id`);

--
-- Indexes for table `customers`
--
ALTER TABLE `customers`
  ADD PRIMARY KEY (`customer_id`);

--
-- Indexes for table `deepfakesuspiciousactivity`
--
ALTER TABLE `deepfakesuspiciousactivity`
  ADD PRIMARY KEY (`log_id`),
  ADD KEY `user_id` (`user_id`);

--
-- Indexes for table `deepfakeuser`
--
ALTER TABLE `deepfakeuser`
  ADD PRIMARY KEY (`original_user_id`);

--
-- Indexes for table `discounts`
--
ALTER TABLE `discounts`
  ADD PRIMARY KEY (`discount_id`),
  ADD KEY `booking_id` (`booking_id`),
  ADD KEY `applied_by` (`applied_by`);

--
-- Indexes for table `feedback`
--
ALTER TABLE `feedback`
  ADD PRIMARY KEY (`feedback_id`),
  ADD KEY `customer_id` (`customer_id`),
  ADD KEY `flight_id` (`flight_id`);

--
-- Indexes for table `file_manager`
--
ALTER TABLE `file_manager`
  ADD PRIMARY KEY (`file_path`);

--
-- Indexes for table `flight`
--
ALTER TABLE `flight`
  ADD PRIMARY KEY (`flight_id`),
  ADD KEY `departure_airport_id` (`departure_airport_id`),
  ADD KEY `arrival_airport_id` (`arrival_airport_id`);

--
-- Indexes for table `flight_flightcrew`
--
ALTER TABLE `flight_flightcrew`
  ADD PRIMARY KEY (`flight_id`,`crew_member_id`);

--
-- Indexes for table `notifications`
--
ALTER TABLE `notifications`
  ADD PRIMARY KEY (`notification_id`),
  ADD KEY `user_id` (`user_id`);

--
-- Indexes for table `passenger`
--
ALTER TABLE `passenger`
  ADD PRIMARY KEY (`passenger_id`),
  ADD UNIQUE KEY `passport_number` (`passport_number`);

--
-- Indexes for table `payment`
--
ALTER TABLE `payment`
  ADD PRIMARY KEY (`payment_id`),
  ADD UNIQUE KEY `transaction_id` (`transaction_id`),
  ADD KEY `booking_id` (`booking_id`);

--
-- Indexes for table `payment_methods`
--
ALTER TABLE `payment_methods`
  ADD PRIMARY KEY (`method_id`),
  ADD KEY `user_id` (`user_id`);

--
-- Indexes for table `refundrequests`
--
ALTER TABLE `refundrequests`
  ADD PRIMARY KEY (`RefundID`),
  ADD KEY `booking_id` (`booking_id`),
  ADD KEY `customer_id` (`customer_id`);

--
-- Indexes for table `system_logs`
--
ALTER TABLE `system_logs`
  ADD PRIMARY KEY (`log_id`),
  ADD KEY `user_id` (`user_id`);

--
-- Indexes for table `system_settings`
--
ALTER TABLE `system_settings`
  ADD PRIMARY KEY (`setting_key`);

--
-- Indexes for table `ticket`
--
ALTER TABLE `ticket`
  ADD PRIMARY KEY (`ticket_id`),
  ADD UNIQUE KEY `barcode` (`barcode`),
  ADD KEY `booking_id` (`booking_id`),
  ADD KEY `passenger_id` (`passenger_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`user_id`),
  ADD UNIQUE KEY `username` (`username`);

--
-- Indexes for table `waitlist`
--
ALTER TABLE `waitlist`
  ADD PRIMARY KEY (`waitlist_id`),
  ADD KEY `flight_id` (`flight_id`);

--
-- Indexes for table `waitlist_customers`
--
ALTER TABLE `waitlist_customers`
  ADD PRIMARY KEY (`waitlist_id`,`customer_id`),
  ADD KEY `customer_id` (`customer_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `refundrequests`
--
ALTER TABLE `refundrequests`
  MODIFY `RefundID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `blacklist`
--
ALTER TABLE `blacklist`
  ADD CONSTRAINT `blacklist_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`),
  ADD CONSTRAINT `blacklist_ibfk_2` FOREIGN KEY (`added_by`) REFERENCES `administrators` (`admin_id`);

--
-- Constraints for table `booking`
--
ALTER TABLE `booking`
  ADD CONSTRAINT `booking_ibfk_1` FOREIGN KEY (`customer_id`) REFERENCES `customers` (`customer_id`),
  ADD CONSTRAINT `booking_ibfk_2` FOREIGN KEY (`flight_id`) REFERENCES `flight` (`flight_id`);

--
-- Constraints for table `deepfakesuspiciousactivity`
--
ALTER TABLE `deepfakesuspiciousactivity`
  ADD CONSTRAINT `deepfakesuspiciousactivity_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `deepfakeuser` (`original_user_id`);

--
-- Constraints for table `deepfakeuser`
--
ALTER TABLE `deepfakeuser`
  ADD CONSTRAINT `deepfakeuser_ibfk_1` FOREIGN KEY (`original_user_id`) REFERENCES `users` (`user_id`);

--
-- Constraints for table `discounts`
--
ALTER TABLE `discounts`
  ADD CONSTRAINT `discounts_ibfk_1` FOREIGN KEY (`booking_id`) REFERENCES `booking` (`booking_id`),
  ADD CONSTRAINT `discounts_ibfk_2` FOREIGN KEY (`applied_by`) REFERENCES `agents` (`agent_id`);

--
-- Constraints for table `feedback`
--
ALTER TABLE `feedback`
  ADD CONSTRAINT `feedback_ibfk_1` FOREIGN KEY (`customer_id`) REFERENCES `customers` (`customer_id`),
  ADD CONSTRAINT `feedback_ibfk_2` FOREIGN KEY (`flight_id`) REFERENCES `flight` (`flight_id`);

--
-- Constraints for table `flight`
--
ALTER TABLE `flight`
  ADD CONSTRAINT `flight_ibfk_1` FOREIGN KEY (`departure_airport_id`) REFERENCES `airport` (`airport_code`),
  ADD CONSTRAINT `flight_ibfk_2` FOREIGN KEY (`arrival_airport_id`) REFERENCES `airport` (`airport_code`);

--
-- Constraints for table `notifications`
--
ALTER TABLE `notifications`
  ADD CONSTRAINT `notifications_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE;

--
-- Constraints for table `payment`
--
ALTER TABLE `payment`
  ADD CONSTRAINT `payment_ibfk_1` FOREIGN KEY (`booking_id`) REFERENCES `booking` (`booking_id`);

--
-- Constraints for table `payment_methods`
--
ALTER TABLE `payment_methods`
  ADD CONSTRAINT `payment_methods_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`);

--
-- Constraints for table `refundrequests`
--
ALTER TABLE `refundrequests`
  ADD CONSTRAINT `refundrequests_ibfk_1` FOREIGN KEY (`booking_id`) REFERENCES `booking` (`booking_id`),
  ADD CONSTRAINT `refundrequests_ibfk_2` FOREIGN KEY (`customer_id`) REFERENCES `customers` (`customer_id`);

--
-- Constraints for table `system_logs`
--
ALTER TABLE `system_logs`
  ADD CONSTRAINT `system_logs_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`);

--
-- Constraints for table `ticket`
--
ALTER TABLE `ticket`
  ADD CONSTRAINT `ticket_ibfk_1` FOREIGN KEY (`booking_id`) REFERENCES `booking` (`booking_id`),
  ADD CONSTRAINT `ticket_ibfk_2` FOREIGN KEY (`passenger_id`) REFERENCES `passenger` (`passenger_id`);

--
-- Constraints for table `waitlist`
--
ALTER TABLE `waitlist`
  ADD CONSTRAINT `waitlist_ibfk_1` FOREIGN KEY (`flight_id`) REFERENCES `flight` (`flight_id`);

--
-- Constraints for table `waitlist_customers`
--
ALTER TABLE `waitlist_customers`
  ADD CONSTRAINT `waitlist_customers_ibfk_1` FOREIGN KEY (`waitlist_id`) REFERENCES `waitlist` (`waitlist_id`),
  ADD CONSTRAINT `waitlist_customers_ibfk_2` FOREIGN KEY (`customer_id`) REFERENCES `customers` (`customer_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
