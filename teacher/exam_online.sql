/*
 Navicat Premium Data Transfer

 Source Server         : demo
 Source Server Type    : MySQL
 Source Server Version : 80031
 Source Host           : localhost:3306
 Source Schema         : exam_online

 Target Server Type    : MySQL
 Target Server Version : 80031
 File Encoding         : 65001

 Date: 27/05/2026 13:30:36
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for admin
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin`  (
  `admin_id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `real_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `status` tinyint NULL DEFAULT 1,
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `last_login_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`admin_id`) USING BTREE,
  UNIQUE INDEX `username`(`username`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of admin
-- ----------------------------
INSERT INTO `admin` VALUES (1, 'admin', 'e10adc3949ba59abbe56e057f20f883e', '系统管理员', 'test@example.com', '19216810001', 1, '2026-05-04 13:55:55', '2026-05-27 13:08:55');
INSERT INTO `admin` VALUES (2, 'superadmin', 'e10adc3949ba59abbe56e057f20f883e', '超级管理员', 'super@school.edu.cn', '13800138001', 1, '2026-05-04 13:55:55', NULL);
INSERT INTO `admin` VALUES (3, 'admin01', 'e10adc3949ba59abbe56e057f20f883e', '李明', 'liming@school.edu.cn', '13800138002', 1, '2026-05-04 13:55:55', NULL);
INSERT INTO `admin` VALUES (4, 'admin02', 'e10adc3949ba59abbe56e057f20f883e', '王芳', 'wangfang@school.edu.cn', '13800138003', 1, '2026-05-04 13:55:55', NULL);
INSERT INTO `admin` VALUES (5, 'admin03', 'e10adc3949ba59abbe56e057f20f883e', '张伟', 'zhangwei@school.edu.cn', '13800138004', 1, '2026-05-04 13:55:55', NULL);
INSERT INTO `admin` VALUES (6, 'admin04', 'e10adc3949ba59abbe56e057f20f883e', '刘洋', 'liuyang@school.edu.cn', '13800138005', 1, '2026-05-04 13:55:55', NULL);
INSERT INTO `admin` VALUES (7, 'admin05', 'e10adc3949ba59abbe56e057f20f883e', '陈静', 'chenjing@school.edu.cn', '13800138006', 1, '2026-05-04 13:55:55', NULL);
INSERT INTO `admin` VALUES (8, 'admin06', 'e10adc3949ba59abbe56e057f20f883e', '赵强', 'zhaoqiang@school.edu.cn', '13800138007', 1, '2026-05-04 13:55:55', NULL);
INSERT INTO `admin` VALUES (9, 'admin07', 'e10adc3949ba59abbe56e057f20f883e', '孙丽', 'sunli@school.edu.cn', '13800138008', 1, '2026-05-04 13:55:55', NULL);
INSERT INTO `admin` VALUES (10, 'admin08', 'e10adc3949ba59abbe56e057f20f883e', '周涛', 'zhoutao@school.edu.cn', '13800138009', 1, '2026-05-04 13:55:55', NULL);
INSERT INTO `admin` VALUES (11, 'admin09', 'e10adc3949ba59abbe56e057f20f883e', '吴磊', 'wulei@school.edu.cn', '13800138010', 1, '2026-05-04 13:55:55', NULL);

-- ----------------------------
-- Table structure for answer_record
-- ----------------------------
DROP TABLE IF EXISTS `answer_record`;
CREATE TABLE `answer_record`  (
  `answer_id` int NOT NULL AUTO_INCREMENT,
  `record_id` int NULL DEFAULT NULL,
  `question_id` int NULL DEFAULT NULL,
  `student_answer` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `is_correct` tinyint NULL DEFAULT NULL,
  `score` decimal(5, 2) NULL DEFAULT 0.00,
  `answer_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`answer_id`) USING BTREE,
  INDEX `record_id`(`record_id`) USING BTREE,
  INDEX `question_id`(`question_id`) USING BTREE,
  CONSTRAINT `answer_record_ibfk_1` FOREIGN KEY (`record_id`) REFERENCES `exam_record` (`record_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `answer_record_ibfk_2` FOREIGN KEY (`question_id`) REFERENCES `question` (`question_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of answer_record
-- ----------------------------
INSERT INTO `answer_record` VALUES (1, 1, 1, 'C', 1, 2.00, '2026-05-04 13:55:55');
INSERT INTO `answer_record` VALUES (2, 1, 2, 'C', 1, 2.00, '2026-05-04 13:55:55');
INSERT INTO `answer_record` VALUES (3, 1, 3, 'C', 1, 2.00, '2026-05-04 13:55:55');
INSERT INTO `answer_record` VALUES (4, 1, 4, 'A', 0, 0.00, '2026-05-04 13:55:55');
INSERT INTO `answer_record` VALUES (5, 1, 5, 'D', 1, 2.00, '2026-05-04 13:55:55');
INSERT INTO `answer_record` VALUES (6, 1, 6, 'A|B', 1, 3.00, '2026-05-04 13:55:55');
INSERT INTO `answer_record` VALUES (7, 1, 7, 'A|B|C', 0, 0.00, '2026-05-04 13:55:55');
INSERT INTO `answer_record` VALUES (8, 1, 8, '0', 1, 1.00, '2026-05-04 13:55:55');
INSERT INTO `answer_record` VALUES (9, 1, 9, '1', 0, 0.00, '2026-05-04 13:55:55');
INSERT INTO `answer_record` VALUES (10, 1, 10, '1', 1, 1.00, '2026-05-04 13:55:55');
INSERT INTO `answer_record` VALUES (11, 1, 11, '4', 1, 2.00, '2026-05-04 13:55:55');
INSERT INTO `answer_record` VALUES (12, 1, 12, '堆排序是将待排序元素构建成堆结构，然后进行排序。', 1, 20.00, '2026-05-04 13:55:55');
INSERT INTO `answer_record` VALUES (13, 2, 1, 'C', 1, 2.00, '2026-05-04 13:55:55');
INSERT INTO `answer_record` VALUES (14, 2, 2, 'C', 1, 2.00, '2026-05-04 13:55:55');
INSERT INTO `answer_record` VALUES (15, 2, 3, 'C', 1, 2.00, '2026-05-04 13:55:55');
INSERT INTO `answer_record` VALUES (16, 2, 4, 'B', 1, 2.00, '2026-05-04 13:55:55');
INSERT INTO `answer_record` VALUES (17, 2, 5, 'D', 1, 2.00, '2026-05-04 13:55:55');
INSERT INTO `answer_record` VALUES (18, 3, 1, 'A', 0, 0.00, '2026-05-04 13:55:55');
INSERT INTO `answer_record` VALUES (19, 3, 2, 'C', 1, 2.00, '2026-05-04 13:55:55');
INSERT INTO `answer_record` VALUES (20, 3, 3, 'A', 0, 0.00, '2026-05-04 13:55:55');
INSERT INTO `answer_record` VALUES (21, 3, 4, 'B', 1, 2.00, '2026-05-04 13:55:55');

-- ----------------------------
-- Table structure for class
-- ----------------------------
DROP TABLE IF EXISTS `class`;
CREATE TABLE `class`  (
  `class_id` int NOT NULL AUTO_INCREMENT,
  `class_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `class_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `department_id` int NULL DEFAULT NULL,
  `grade` year NULL DEFAULT NULL,
  `student_count` int NULL DEFAULT 0,
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`class_id`) USING BTREE,
  UNIQUE INDEX `class_code`(`class_code`) USING BTREE,
  INDEX `department_id`(`department_id`) USING BTREE,
  CONSTRAINT `class_ibfk_1` FOREIGN KEY (`department_id`) REFERENCES `department` (`department_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of class
-- ----------------------------
INSERT INTO `class` VALUES (1, '计算机科学与技术2021级1班', 'CS2101', 1, 2021, 13, '2026-05-04 13:55:55');
INSERT INTO `class` VALUES (2, '软件工程2021级1班', 'SE2101', 1, 2021, 42, '2026-05-04 13:55:55');
INSERT INTO `class` VALUES (3, '计算机科学与技术2022级1班', 'CS2201', 1, 2022, 48, '2026-05-04 13:55:55');
INSERT INTO `class` VALUES (4, '软件工程2022级1班', 'SE2201', 1, 2022, 40, '2026-05-04 13:55:55');
INSERT INTO `class` VALUES (5, '数学与应用数学2021级1班', 'MA2101', 2, 2021, 38, '2026-05-04 13:55:55');
INSERT INTO `class` VALUES (6, '统计学2021级1班', 'ST2101', 2, 2021, 35, '2026-05-04 13:55:55');
INSERT INTO `class` VALUES (7, '英语2021级1班', 'EN2101', 3, 2021, 50, '2026-05-04 13:55:55');
INSERT INTO `class` VALUES (8, '日语2021级1班', 'JP2101', 3, 2021, 32, '2026-05-04 13:55:55');
INSERT INTO `class` VALUES (9, '经济学2021级1班', 'EC2101', 4, 2021, 55, '2026-05-04 13:55:55');
INSERT INTO `class` VALUES (10, '金融学2021级1班', 'FI2101', 4, 2021, 48, '2026-05-04 13:55:55');
INSERT INTO `class` VALUES (11, '电子信息工程2021级1班', 'EE2101', 5, 2021, 46, '2026-05-04 13:55:55');
INSERT INTO `class` VALUES (12, '通信工程2021级1班', 'CE2101', 5, 2021, 43, '2026-05-04 13:55:55');

-- ----------------------------
-- Table structure for department
-- ----------------------------
DROP TABLE IF EXISTS `department`;
CREATE TABLE `department`  (
  `department_id` int NOT NULL AUTO_INCREMENT,
  `department_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `department_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`department_id`) USING BTREE,
  UNIQUE INDEX `department_code`(`department_code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of department
-- ----------------------------
INSERT INTO `department` VALUES (1, '计算机科学与技术学院', 'CS', '计算机科学与技术、软件工程、网络工程', '2026-05-04 13:55:55');
INSERT INTO `department` VALUES (2, '数学与统计学院', 'MATH', '数学与应用数学、统计学、信息与计算科学', '2026-05-04 13:55:55');
INSERT INTO `department` VALUES (3, '外国语学院', 'FL', '英语、日语、法语、德语', '2026-05-04 13:55:55');
INSERT INTO `department` VALUES (4, '经济管理学院', 'ECON', '经济学、金融学、工商管理、会计学', '2026-05-04 13:55:55');
INSERT INTO `department` VALUES (5, '电子信息学院', 'EE', '电子信息工程、通信工程、自动化', '2026-05-04 13:55:55');
INSERT INTO `department` VALUES (6, '机械工程学院', 'ME', '机械设计制造及其自动化、车辆工程', '2026-05-04 13:55:55');
INSERT INTO `department` VALUES (7, '土木工程学院', 'CE', '土木工程、建筑学、工程管理', '2026-05-04 13:55:55');
INSERT INTO `department` VALUES (8, '文学院', 'LIT', '汉语言文学、新闻学、广告学', '2026-05-04 13:55:55');
INSERT INTO `department` VALUES (9, '法学院', 'LAW', '法学、知识产权', '2026-05-04 13:55:55');
INSERT INTO `department` VALUES (10, '医学院', 'MED', '临床医学、护理学、药学', '2026-05-04 13:55:55');
INSERT INTO `department` VALUES (11, '理学院', 'SCI', '物理学、化学、生物学', '2026-05-04 13:55:55');
INSERT INTO `department` VALUES (12, '11', '1', '1', '2026-05-20 13:08:55');
INSERT INTO `department` VALUES (13, '11', '11', '11', '2026-05-20 13:23:48');

-- ----------------------------
-- Table structure for exam
-- ----------------------------
DROP TABLE IF EXISTS `exam`;
CREATE TABLE `exam`  (
  `exam_id` int NOT NULL AUTO_INCREMENT,
  `exam_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `paper_id` int NULL DEFAULT NULL,
  `class_id` int NULL DEFAULT NULL,
  `start_time` datetime NOT NULL,
  `end_time` datetime NOT NULL,
  `status` tinyint NULL DEFAULT 0,
  `allow_screen_switch` tinyint NULL DEFAULT 3,
  `create_by` int NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`exam_id`) USING BTREE,
  INDEX `paper_id`(`paper_id`) USING BTREE,
  INDEX `class_id`(`class_id`) USING BTREE,
  INDEX `create_by`(`create_by`) USING BTREE,
  CONSTRAINT `exam_ibfk_1` FOREIGN KEY (`paper_id`) REFERENCES `exam_paper` (`paper_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `exam_ibfk_2` FOREIGN KEY (`class_id`) REFERENCES `class` (`class_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `exam_ibfk_3` FOREIGN KEY (`create_by`) REFERENCES `teacher` (`teacher_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of exam
-- ----------------------------
INSERT INTO `exam` VALUES (1, '2024年春季数据结构与算法期中考试', 1, 1, '2024-04-15 14:00:00', '2024-04-15 16:00:00', 2, 3, 1, '2026-05-04 13:55:55');
INSERT INTO `exam` VALUES (2, '2024年春季数据结构与算法期末考试', 2, 1, '2024-06-20 09:00:00', '2024-06-20 11:30:00', 1, 3, 1, '2026-05-04 13:55:55');
INSERT INTO `exam` VALUES (3, '2024年春季操作系统期中考试', 3, 1, '2024-04-20 14:00:00', '2024-04-20 16:00:00', 2, 3, 2, '2026-05-04 13:55:55');
INSERT INTO `exam` VALUES (4, '2024年春季计算机网络期末考试', 4, 2, '2024-06-25 09:00:00', '2024-06-25 11:00:00', 1, 3, 3, '2026-05-04 13:55:55');
INSERT INTO `exam` VALUES (5, '2024年春季数据库原理期末考试', 5, 2, '2024-06-22 14:00:00', '2024-06-22 16:00:00', 1, 3, 2, '2026-05-04 13:55:55');
INSERT INTO `exam` VALUES (6, '2024年春季高等数学期中考试', 6, 5, '2024-04-18 09:00:00', '2024-04-18 11:30:00', 2, 3, 4, '2026-05-04 13:55:55');
INSERT INTO `exam` VALUES (7, '2024年春季线性代数期末考试', 7, 6, '2024-06-18 14:00:00', '2024-06-18 16:00:00', 1, 3, 5, '2026-05-04 13:55:55');
INSERT INTO `exam` VALUES (8, '2024年春季概率论期末考试', 8, 6, '2024-06-19 09:00:00', '2024-06-19 11:00:00', 1, 3, 4, '2026-05-04 13:55:55');
INSERT INTO `exam` VALUES (9, '2024年春季大学英语期中考试', 9, 7, '2024-04-22 14:00:00', '2024-04-22 16:00:00', 2, 3, 6, '2026-05-04 13:55:55');
INSERT INTO `exam` VALUES (10, '2024年春季微观经济学期末考试', 10, 9, '2024-06-21 09:00:00', '2024-06-21 11:00:00', 1, 3, 7, '2026-05-04 13:55:55');
INSERT INTO `exam` VALUES (11, '2024年春季电路原理期中考试', 11, 11, '2024-04-25 14:00:00', '2024-04-25 16:00:00', 1, 3, 8, '2026-05-04 13:55:55');
INSERT INTO `exam` VALUES (12, '1', 1, NULL, '2026-05-23 00:00:00', '2026-05-23 00:00:00', 1, NULL, 1, '2026-05-23 19:54:22');
INSERT INTO `exam` VALUES (13, '11', 1, NULL, '2026-05-25 00:00:00', '2026-05-25 00:00:00', 1, NULL, 1, '2026-05-25 16:52:14');

-- ----------------------------
-- Table structure for exam_paper
-- ----------------------------
DROP TABLE IF EXISTS `exam_paper`;
CREATE TABLE `exam_paper`  (
  `paper_id` int NOT NULL AUTO_INCREMENT,
  `paper_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `subject_id` int NULL DEFAULT NULL,
  `total_score` decimal(6, 2) NULL DEFAULT 100.00,
  `pass_score` decimal(6, 2) NULL DEFAULT 60.00,
  `duration` int NULL DEFAULT 120,
  `question_count` int NULL DEFAULT 0,
  `create_by` int NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `status` tinyint NULL DEFAULT 1,
  PRIMARY KEY (`paper_id`) USING BTREE,
  INDEX `subject_id`(`subject_id`) USING BTREE,
  INDEX `create_by`(`create_by`) USING BTREE,
  CONSTRAINT `exam_paper_ibfk_1` FOREIGN KEY (`subject_id`) REFERENCES `subject` (`subject_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `exam_paper_ibfk_2` FOREIGN KEY (`create_by`) REFERENCES `teacher` (`teacher_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of exam_paper
-- ----------------------------
INSERT INTO `exam_paper` VALUES (1, '数据结构与算法期中考试A卷', 1, 100.00, 60.00, 120, 20, 1, '2026-05-04 13:55:55', 1);
INSERT INTO `exam_paper` VALUES (2, '数据结构与算法期末考试A卷', 1, 100.00, 60.00, 150, 25, 1, '2026-05-04 13:55:55', 1);
INSERT INTO `exam_paper` VALUES (3, '操作系统期中考试A卷', 2, 100.00, 60.00, 120, 20, 2, '2026-05-04 13:55:55', 1);
INSERT INTO `exam_paper` VALUES (4, '计算机网络期末考试A卷', 3, 100.00, 60.00, 120, 25, 3, '2026-05-04 13:55:55', 1);
INSERT INTO `exam_paper` VALUES (5, '数据库原理期末考试A卷', 4, 100.00, 60.00, 120, 25, 2, '2026-05-04 13:55:55', 1);
INSERT INTO `exam_paper` VALUES (6, '高等数学期中考试A卷', 5, 100.00, 60.00, 150, 20, 4, '2026-05-04 13:55:55', 1);
INSERT INTO `exam_paper` VALUES (7, '线性代数期末考试A卷', 6, 100.00, 60.00, 120, 20, 5, '2026-05-04 13:55:55', 1);
INSERT INTO `exam_paper` VALUES (8, '概率论期末考试A卷', 7, 100.00, 60.00, 120, 20, 4, '2026-05-04 13:55:55', 1);
INSERT INTO `exam_paper` VALUES (9, '大学英语期中考试A卷', 8, 100.00, 60.00, 120, 30, 6, '2026-05-04 13:55:55', 1);
INSERT INTO `exam_paper` VALUES (10, '微观经济学期末考试A卷', 9, 100.00, 60.00, 120, 25, 7, '2026-05-04 13:55:55', 1);
INSERT INTO `exam_paper` VALUES (11, '电路原理期中考试A卷', 11, 100.00, 60.00, 120, 20, 8, '2026-05-04 13:55:55', 1);
INSERT INTO `exam_paper` VALUES (13, 'os', 2, 100.00, NULL, 60, NULL, 1, '2026-05-25 16:58:59', 1);

-- ----------------------------
-- Table structure for exam_record
-- ----------------------------
DROP TABLE IF EXISTS `exam_record`;
CREATE TABLE `exam_record`  (
  `record_id` int NOT NULL AUTO_INCREMENT,
  `exam_id` int NULL DEFAULT NULL,
  `student_id` int NULL DEFAULT NULL,
  `start_time` datetime NULL DEFAULT NULL,
  `submit_time` datetime NULL DEFAULT NULL,
  `total_score` decimal(6, 2) NULL DEFAULT 0.00,
  `objective_score` decimal(6, 2) NULL DEFAULT 0.00,
  `subjective_score` decimal(6, 2) NULL DEFAULT 0.00,
  `status` tinyint NULL DEFAULT 0,
  `screen_switch_count` int NULL DEFAULT 0,
  `ip_address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  PRIMARY KEY (`record_id`) USING BTREE,
  INDEX `exam_id`(`exam_id`) USING BTREE,
  INDEX `student_id`(`student_id`) USING BTREE,
  CONSTRAINT `exam_record_ibfk_1` FOREIGN KEY (`exam_id`) REFERENCES `exam` (`exam_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `exam_record_ibfk_2` FOREIGN KEY (`student_id`) REFERENCES `student` (`student_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of exam_record
-- ----------------------------
INSERT INTO `exam_record` VALUES (1, 1, 1, '2024-04-15 14:05:00', '2024-04-15 15:50:00', 88.50, 68.50, 20.00, 2, 1, NULL);
INSERT INTO `exam_record` VALUES (2, 1, 2, '2024-04-15 14:03:00', '2024-04-15 15:45:00', 92.00, 72.00, 20.00, 2, 0, NULL);
INSERT INTO `exam_record` VALUES (3, 1, 3, '2024-04-15 14:01:00', '2024-04-15 15:30:00', 76.50, 56.50, 20.00, 2, 2, NULL);
INSERT INTO `exam_record` VALUES (4, 1, 4, '2024-04-15 14:10:00', '2024-04-15 15:55:00', 85.00, 65.00, 20.00, 2, 1, NULL);
INSERT INTO `exam_record` VALUES (5, 1, 5, '2024-04-15 14:02:00', '2024-04-15 16:00:00', 90.50, 70.50, 20.00, 2, 0, NULL);
INSERT INTO `exam_record` VALUES (6, 1, 6, '2024-04-15 14:08:00', '2024-04-15 15:40:00', 72.00, 52.00, 20.00, 2, 3, NULL);
INSERT INTO `exam_record` VALUES (7, 1, 7, '2024-04-15 14:06:00', '2024-04-15 15:52:00', 95.00, 75.00, 20.00, 2, 0, NULL);
INSERT INTO `exam_record` VALUES (8, 1, 8, '2024-04-15 14:04:00', '2024-04-15 15:48:00', 81.00, 61.00, 20.00, 2, 1, NULL);
INSERT INTO `exam_record` VALUES (9, 1, 9, '2024-04-15 14:12:00', '2024-04-15 15:35:00', 78.50, 58.50, 20.00, 2, 2, NULL);
INSERT INTO `exam_record` VALUES (10, 1, 10, '2024-04-15 14:00:00', '2024-04-15 15:55:00', 86.00, 66.00, 20.00, 2, 1, NULL);
INSERT INTO `exam_record` VALUES (11, 1, 11, '2024-04-15 14:07:00', '2024-04-15 15:58:00', 89.00, 69.00, 20.00, 2, 0, NULL);
INSERT INTO `exam_record` VALUES (12, 6, 1, '2024-04-18 09:05:00', '2024-04-18 11:20:00', 82.00, 82.00, 0.00, 2, 0, NULL);

-- ----------------------------
-- Table structure for paper_question
-- ----------------------------
DROP TABLE IF EXISTS `paper_question`;
CREATE TABLE `paper_question`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `paper_id` int NULL DEFAULT NULL,
  `question_id` int NULL DEFAULT NULL,
  `question_order` int NULL DEFAULT NULL,
  `score` decimal(5, 2) NULL DEFAULT 2.00,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `paper_id`(`paper_id`) USING BTREE,
  INDEX `question_id`(`question_id`) USING BTREE,
  CONSTRAINT `paper_question_ibfk_1` FOREIGN KEY (`paper_id`) REFERENCES `exam_paper` (`paper_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `paper_question_ibfk_2` FOREIGN KEY (`question_id`) REFERENCES `question` (`question_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 22 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of paper_question
-- ----------------------------
INSERT INTO `paper_question` VALUES (1, 1, 1, 1, 2.00);
INSERT INTO `paper_question` VALUES (2, 1, 2, 2, 2.00);
INSERT INTO `paper_question` VALUES (3, 1, 3, 3, 2.00);
INSERT INTO `paper_question` VALUES (4, 1, 4, 4, 2.00);
INSERT INTO `paper_question` VALUES (5, 1, 5, 5, 2.00);
INSERT INTO `paper_question` VALUES (6, 1, 6, 6, 3.00);
INSERT INTO `paper_question` VALUES (7, 1, 7, 7, 3.00);
INSERT INTO `paper_question` VALUES (8, 1, 8, 8, 1.00);
INSERT INTO `paper_question` VALUES (9, 1, 9, 9, 1.00);
INSERT INTO `paper_question` VALUES (10, 1, 10, 10, 1.00);
INSERT INTO `paper_question` VALUES (11, 1, 11, 11, 2.00);
INSERT INTO `paper_question` VALUES (12, 1, 12, 12, 10.00);
INSERT INTO `paper_question` VALUES (13, 2, 1, 1, 2.00);
INSERT INTO `paper_question` VALUES (14, 2, 2, 2, 2.00);
INSERT INTO `paper_question` VALUES (15, 2, 3, 3, 2.00);
INSERT INTO `paper_question` VALUES (16, 2, 4, 4, 2.00);
INSERT INTO `paper_question` VALUES (17, 2, 5, 5, 2.00);
INSERT INTO `paper_question` VALUES (18, 3, 13, 1, 2.00);
INSERT INTO `paper_question` VALUES (19, 3, 14, 2, 2.00);
INSERT INTO `paper_question` VALUES (20, 3, 15, 3, 2.00);
INSERT INTO `paper_question` VALUES (21, 3, 16, 4, 2.00);

-- ----------------------------
-- Table structure for question
-- ----------------------------
DROP TABLE IF EXISTS `question`;
CREATE TABLE `question`  (
  `question_id` int NOT NULL AUTO_INCREMENT,
  `subject_id` int NULL DEFAULT NULL,
  `question_type` tinyint NOT NULL,
  `question_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `options` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `correct_answer` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `score` decimal(5, 2) NULL DEFAULT 2.00,
  `difficulty` tinyint NULL DEFAULT 2,
  `knowledge_point` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `create_by` int NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`question_id`) USING BTREE,
  INDEX `subject_id`(`subject_id`) USING BTREE,
  INDEX `create_by`(`create_by`) USING BTREE,
  CONSTRAINT `question_ibfk_1` FOREIGN KEY (`subject_id`) REFERENCES `subject` (`subject_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `question_ibfk_2` FOREIGN KEY (`create_by`) REFERENCES `teacher` (`teacher_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of question
-- ----------------------------
INSERT INTO `question` VALUES (1, 1, 1, '栈和队列的共同特点是？', 'A.都是先进先出\nB.都是先进后出\nC.只允许在端点处插入和删除元素\nD.没有共同点', 'C', 2.00, 2, '栈与队列', 1, '2026-05-04 13:55:55', '2026-05-04 13:55:55');
INSERT INTO `question` VALUES (2, 1, 1, '一个栈的入栈序列是a,b,c,d,e，则栈的不可能的输出序列是？', 'A.edcba\nB.decba\nC.dceab\nD.abcde', 'C', 2.00, 3, '栈的特性', 1, '2026-05-04 13:55:55', '2026-05-04 13:55:55');
INSERT INTO `question` VALUES (3, 1, 1, '深度为5的二叉树至多有____个结点。', 'A.16\nB.32\nC.31\nD.10', 'C', 2.00, 2, '二叉树性质', 1, '2026-05-04 13:55:55', '2026-05-04 13:55:55');
INSERT INTO `question` VALUES (4, 1, 1, '链表不具有的特点是？', 'A.插入、删除不需要移动元素\nB.可随机访问任一元素\nC.不必事先估计存储空间\nD.所需空间与线性长度成正比', 'B', 2.00, 1, '链表特性', 1, '2026-05-04 13:55:55', '2026-05-04 13:55:55');
INSERT INTO `question` VALUES (5, 1, 1, '冒泡排序在最坏情况下的时间复杂度为？', 'A.O(1)\nB.O(logn)\nC.O(n)\nD.O(n²)', 'D', 2.00, 2, '排序算法', 2, '2026-05-04 13:55:55', '2026-05-04 13:55:55');
INSERT INTO `question` VALUES (6, 1, 2, '下列属于线性数据结构的有？', 'A.栈\nB.队列\nC.树\nD.图', 'A|B', 3.00, 2, '数据结构分类', 1, '2026-05-04 13:55:55', '2026-05-04 13:55:55');
INSERT INTO `question` VALUES (7, 1, 2, '二叉树的遍历方式包括？', 'A.前序遍历\nB.中序遍历\nC.后序遍历\nD.层次遍历', 'A|B|C|D', 3.00, 1, '二叉树遍历', 1, '2026-05-04 13:55:55', '2026-05-04 13:55:55');
INSERT INTO `question` VALUES (8, 1, 3, '快速排序是一种稳定的排序算法。', NULL, '0', 1.00, 2, '排序算法', 2, '2026-05-04 13:55:55', '2026-05-04 13:55:55');
INSERT INTO `question` VALUES (9, 1, 3, '哈希表的查找效率与装填因子无关。', NULL, '0', 1.00, 3, '哈希表', 2, '2026-05-04 13:55:55', '2026-05-04 13:55:55');
INSERT INTO `question` VALUES (10, 1, 3, '二叉搜索树的查找效率在最坏情况下为O(n)。', NULL, '1', 1.00, 2, '二叉搜索树', 1, '2026-05-04 13:55:55', '2026-05-04 13:55:55');
INSERT INTO `question` VALUES (11, 1, 4, '一棵二叉树的度为2的结点有3个，度为1的结点有2个，则叶子结点有____个。', NULL, '4', 2.00, 3, '二叉树性质', 1, '2026-05-04 13:55:55', '2026-05-04 13:55:55');
INSERT INTO `question` VALUES (12, 1, 5, '简述堆排序的基本思想。', NULL, '堆排序是将待排序序列构造成一个大顶堆或小顶堆，此时整个序列的最大值或最小值就是堆顶的根节点。将其与末尾元素交换，然后将剩余n-1个元素重新构造成一个堆，重复执行直至得到一个有序序列。', 10.00, 3, '排序算法', 1, '2026-05-04 13:55:55', '2026-05-04 13:55:55');
INSERT INTO `question` VALUES (13, 2, 1, '进程和程序的本质区别是？', 'A.前者存储在内存，后者在外存\nB.前者是动态的，后者是静态的\nC.前者分时使用CPU，后者独占CPU\nD.前者在一个文件中，后者在多个文件中', 'B', 2.00, 2, '进程概念', 2, '2026-05-04 13:55:55', '2026-05-04 13:55:55');
INSERT INTO `question` VALUES (14, 2, 1, '死锁产生的四个必要条件是互斥、请求与保持、不剥夺和？', 'A.循环等待\nB.请求与释放\nC.资源互斥\nD.资源独占', 'A', 2.00, 2, '死锁', 2, '2026-05-04 13:55:55', '2026-05-04 13:55:55');
INSERT INTO `question` VALUES (15, 2, 1, '虚拟存储技术的基本思想是？', 'A.扩充内存物理空间\nB.扩充外存空间\nC.用外存空间换取内存空间\nD.用缓存空间换取内存空间', 'C', 2.00, 3, '虚拟存储', 2, '2026-05-04 13:55:55', '2026-05-04 13:55:55');
INSERT INTO `question` VALUES (16, 2, 1, '分页系统中，地址转换工作是由____完成的。', 'A.硬件\nB.操作系统\nC.用户程序\nD.装入程序', 'A', 2.00, 2, '存储管理', 2, '2026-05-04 13:55:55', '2026-05-04 13:55:55');

-- ----------------------------
-- Table structure for student
-- ----------------------------
DROP TABLE IF EXISTS `student`;
CREATE TABLE `student`  (
  `student_id` int NOT NULL AUTO_INCREMENT,
  `student_no` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `real_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `gender` tinyint NULL DEFAULT 0,
  `class_id` int NULL DEFAULT NULL,
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `status` tinyint NULL DEFAULT 1,
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `last_login_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`student_id`) USING BTREE,
  UNIQUE INDEX `student_no`(`student_no`) USING BTREE,
  INDEX `class_id`(`class_id`) USING BTREE,
  CONSTRAINT `student_ibfk_1` FOREIGN KEY (`class_id`) REFERENCES `class` (`class_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of student
-- ----------------------------
INSERT INTO `student` VALUES (1, '2021001001', 'e10adc3949ba59abbe56e057f20f883e', '张三', 1, 1, 'zhangsan@student.school.edu.cn', '18800188001', 1, '2026-05-04 13:55:55', '2026-05-27 13:04:33');
INSERT INTO `student` VALUES (2, '2021001002', 'e10adc3949ba59abbe56e057f20f883e', '李四', 2, 1, 'lisi@student.school.edu.cn', '18800188002', 1, '2026-05-04 13:55:55', NULL);
INSERT INTO `student` VALUES (3, '2021001003', 'e10adc3949ba59abbe56e057f20f883e', '王五', 1, 1, 'wangwu@student.school.edu.cn', '18800188003', 1, '2026-05-04 13:55:55', NULL);
INSERT INTO `student` VALUES (4, '2021001004', 'e10adc3949ba59abbe56e057f20f883e', '赵六', 1, 1, 'zhaoliu@student.school.edu.cn', '18800188004', 1, '2026-05-04 13:55:55', NULL);
INSERT INTO `student` VALUES (5, '2021001005', 'e10adc3949ba59abbe56e057f20f883e', '钱七', 2, 1, 'qianqi@student.school.edu.cn', '18800188005', 1, '2026-05-04 13:55:55', NULL);
INSERT INTO `student` VALUES (6, '2021001006', 'e10adc3949ba59abbe56e057f20f883e', '孙八', 1, 1, 'sunba@student.school.edu.cn', '18800188006', 1, '2026-05-04 13:55:55', NULL);
INSERT INTO `student` VALUES (7, '2021001007', 'e10adc3949ba59abbe56e057f20f883e', '周九', 2, 1, 'zhoujiu@student.school.edu.cn', '18800188007', 1, '2026-05-04 13:55:55', NULL);
INSERT INTO `student` VALUES (8, '2021001008', 'e10adc3949ba59abbe56e057f20f883e', '吴十', 1, 1, 'wushi@student.school.edu.cn', '18800188008', 1, '2026-05-04 13:55:55', NULL);
INSERT INTO `student` VALUES (9, '2021001009', 'e10adc3949ba59abbe56e057f20f883e', '郑一', 1, 1, 'zhengyi@student.school.edu.cn', '18800188009', 1, '2026-05-04 13:55:55', NULL);
INSERT INTO `student` VALUES (10, '2021001010', 'e10adc3949ba59abbe56e057f20f883e', '冯二', 2, 1, 'fenger@student.school.edu.cn', '18800188010', 1, '2026-05-04 13:55:55', NULL);
INSERT INTO `student` VALUES (11, '2021001011', 'e10adc3949ba59abbe56e057f20f883e', '陈三', 1, 1, 'chensan@student.school.edu.cn', '18800188011', 1, '2026-05-04 13:55:55', NULL);
INSERT INTO `student` VALUES (12, '2021002001', 'e10adc3949ba59abbe56e057f20f883e', '林四', 2, 2, 'linsi@student.school.edu.cn', '18800188012', 1, '2026-05-04 13:55:55', NULL);
INSERT INTO `student` VALUES (13, '1', 'e10adc3949ba59abbe56e057f20f883e', 'student1', 1, 1, '271827318@163.com', '17236272817', NULL, '2026-05-19 18:56:38', NULL);
INSERT INTO `student` VALUES (15, '2', 'e10adc3949ba59abbe56e057f20f883e', 'student2', 1, 1, '1111@163.com', '1827363728', 1, '2026-05-19 21:38:04', NULL);

-- ----------------------------
-- Table structure for subject
-- ----------------------------
DROP TABLE IF EXISTS `subject`;
CREATE TABLE `subject`  (
  `subject_id` int NOT NULL AUTO_INCREMENT,
  `subject_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `subject_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `department_id` int NULL DEFAULT NULL,
  `credit` decimal(3, 1) NULL DEFAULT 3.0,
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`subject_id`) USING BTREE,
  UNIQUE INDEX `subject_code`(`subject_code`) USING BTREE,
  INDEX `department_id`(`department_id`) USING BTREE,
  CONSTRAINT `subject_ibfk_1` FOREIGN KEY (`department_id`) REFERENCES `department` (`department_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of subject
-- ----------------------------
INSERT INTO `subject` VALUES (1, '数据结构与算法', 'CS101', 1, 4.0, '计算机专业核心基础课程', '2026-05-04 13:55:55');
INSERT INTO `subject` VALUES (2, '操作系统', 'CS102', 1, 4.0, '计算机系统核心课程', '2026-05-04 13:55:55');
INSERT INTO `subject` VALUES (3, '计算机网络', 'CS103', 1, 3.5, '网络通信原理与技术', '2026-05-04 13:55:55');
INSERT INTO `subject` VALUES (4, '数据库原理', 'CS104', 1, 4.0, '数据库设计与应用', '2026-05-04 13:55:55');
INSERT INTO `subject` VALUES (5, '高等数学', 'MATH101', 2, 5.0, '工科数学基础', '2026-05-04 13:55:55');
INSERT INTO `subject` VALUES (6, '线性代数', 'MATH102', 2, 3.0, '矩阵理论与应用', '2026-05-04 13:55:55');
INSERT INTO `subject` VALUES (7, '概率论与数理统计', 'MATH103', 2, 3.0, '随机数学基础', '2026-05-04 13:55:55');
INSERT INTO `subject` VALUES (8, '大学英语', 'ENG101', 3, 4.0, '英语综合能力培养', '2026-05-04 13:55:55');
INSERT INTO `subject` VALUES (9, '微观经济学', 'ECO101', 4, 3.0, '经济学基础理论', '2026-05-04 13:55:55');
INSERT INTO `subject` VALUES (10, '宏观经济学', 'ECO102', 4, 3.0, '宏观经济分析', '2026-05-04 13:55:55');
INSERT INTO `subject` VALUES (11, '电路原理', 'EE101', 5, 4.0, '电子电路基础', '2026-05-04 13:55:55');
INSERT INTO `subject` VALUES (12, '信号与系统', 'EE102', 5, 4.0, '信号处理基础', '2026-05-04 13:55:55');

-- ----------------------------
-- Table structure for system_log
-- ----------------------------
DROP TABLE IF EXISTS `system_log`;
CREATE TABLE `system_log`  (
  `log_id` int NOT NULL AUTO_INCREMENT,
  `user_type` tinyint NOT NULL,
  `user_id` int NULL DEFAULT NULL,
  `operation` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `operation_detail` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `ip_address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`log_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 46 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of system_log
-- ----------------------------
INSERT INTO `system_log` VALUES (1, 1, 1, '登录系统', '管理员admin登录成功', '192.168.1.100', '2026-05-04 13:55:55');
INSERT INTO `system_log` VALUES (2, 2, 1, '登录系统', '教师张教授登录成功', '192.168.1.101', '2026-05-04 13:55:55');
INSERT INTO `system_log` VALUES (3, 3, 1, '登录系统', '学生张三登录成功', '192.168.1.102', '2026-05-04 13:55:55');
INSERT INTO `system_log` VALUES (4, 2, 1, '创建试卷', '创建了试卷: 数据结构与算法期中考试A卷', '192.168.1.101', '2026-05-04 13:55:55');
INSERT INTO `system_log` VALUES (5, 2, 1, '发布考试', '发布了考试: 2024年春季数据结构与算法期中考试', '192.168.1.101', '2026-05-04 13:55:55');
INSERT INTO `system_log` VALUES (6, 3, 1, '参加考试', '参加了考试: 2024年春季数据结构与算法期中考试', '192.168.1.102', '2026-05-04 13:55:55');
INSERT INTO `system_log` VALUES (7, 3, 2, '参加考试', '参加了考试: 2024年春季数据结构与算法期中考试', '192.168.1.103', '2026-05-04 13:55:55');
INSERT INTO `system_log` VALUES (8, 3, 3, '参加考试', '参加了考试: 2024年春季数据结构与算法期中考试', '192.168.1.104', '2026-05-04 13:55:55');
INSERT INTO `system_log` VALUES (9, 3, 1, '提交试卷', '提交了试卷: 2024年春季数据结构与算法期中考试', '192.168.1.102', '2026-05-04 13:55:55');
INSERT INTO `system_log` VALUES (10, 2, 1, '阅卷完成', '完成了考试阅卷: 2024年春季数据结构与算法期中考试', '192.168.1.101', '2026-05-04 13:55:55');
INSERT INTO `system_log` VALUES (11, 1, 1, '查看统计', '查看了系统统计数据', '192.168.1.100', '2026-05-04 13:55:55');
INSERT INTO `system_log` VALUES (12, 1, 1, '登录系统', '管理员admin登录成功', '0:0:0:0:0:0:0:1', '2026-05-05 12:46:34');
INSERT INTO `system_log` VALUES (13, 1, 1, '登录系统', '管理员admin登录成功', '0:0:0:0:0:0:0:1', '2026-05-05 12:46:59');
INSERT INTO `system_log` VALUES (14, 1, 1, '登录系统', '管理员admin登录成功', '0:0:0:0:0:0:0:1', '2026-05-06 19:23:12');
INSERT INTO `system_log` VALUES (15, 1, 1, '登录系统', '管理员admin登录成功', '0:0:0:0:0:0:0:1', '2026-05-06 19:25:30');
INSERT INTO `system_log` VALUES (16, 1, 1, '登录系统', '管理员admin登录成功', '0:0:0:0:0:0:0:1', '2026-05-07 21:43:23');
INSERT INTO `system_log` VALUES (17, 1, 1, '登录系统', '管理员admin登录成功', '0:0:0:0:0:0:0:1', '2026-05-07 22:12:01');
INSERT INTO `system_log` VALUES (18, 1, 1, '登录系统', '管理员admin登录成功', '0:0:0:0:0:0:0:1', '2026-05-11 20:13:06');
INSERT INTO `system_log` VALUES (19, 1, 1, '登录系统', '管理员admin登录成功', '0:0:0:0:0:0:0:1', '2026-05-12 09:57:11');
INSERT INTO `system_log` VALUES (20, 1, 1, '登录系统', '管理员admin登录成功', '0:0:0:0:0:0:0:1', '2026-05-13 16:27:53');
INSERT INTO `system_log` VALUES (21, 1, 1, '登录系统', '管理员admin登录成功', '0:0:0:0:0:0:0:1', '2026-05-13 17:46:58');
INSERT INTO `system_log` VALUES (22, 1, 1, '登录系统', '管理员admin登录成功', '0:0:0:0:0:0:0:1', '2026-05-14 21:38:18');
INSERT INTO `system_log` VALUES (23, 1, 1, '登录系统', '管理员admin登录成功', '0:0:0:0:0:0:0:1', '2026-05-14 21:38:48');
INSERT INTO `system_log` VALUES (24, 1, 1, '登录系统', '管理员admin登录成功', '0:0:0:0:0:0:0:1', '2026-05-15 08:30:08');
INSERT INTO `system_log` VALUES (25, 1, 1, '登录系统', '管理员admin登录成功', '0:0:0:0:0:0:0:1', '2026-05-15 08:58:40');
INSERT INTO `system_log` VALUES (26, 1, 1, '登录系统', '管理员admin登录成功', '0:0:0:0:0:0:0:1', '2026-05-15 10:44:13');
INSERT INTO `system_log` VALUES (27, 1, 1, '登录系统', '管理员admin登录成功', '0:0:0:0:0:0:0:1', '2026-05-15 11:16:25');
INSERT INTO `system_log` VALUES (28, 1, 1, '登录系统', '管理员admin登录成功', '0:0:0:0:0:0:0:1', '2026-05-15 17:47:00');
INSERT INTO `system_log` VALUES (29, 1, 1, '登录系统', '管理员admin登录成功', '0:0:0:0:0:0:0:1', '2026-05-15 23:14:29');
INSERT INTO `system_log` VALUES (30, 1, 1, '登录系统', '管理员admin登录成功', '0:0:0:0:0:0:0:1', '2026-05-18 08:16:33');
INSERT INTO `system_log` VALUES (31, 1, 1, '登录系统', '管理员admin登录成功', '0:0:0:0:0:0:0:1', '2026-05-19 17:13:59');
INSERT INTO `system_log` VALUES (32, 1, 1, '登录系统', '管理员admin登录成功', '0:0:0:0:0:0:0:1', '2026-05-21 15:04:24');
INSERT INTO `system_log` VALUES (33, 1, 1, '登录系统', '管理员admin登录成功', '0:0:0:0:0:0:0:1', '2026-05-21 15:19:55');
INSERT INTO `system_log` VALUES (34, 1, 1, '登录系统', '管理员admin登录成功', '0:0:0:0:0:0:0:1', '2026-05-22 21:50:19');
INSERT INTO `system_log` VALUES (35, 1, 1, '登录系统', '管理员admin登录成功', '0:0:0:0:0:0:0:1', '2026-05-22 21:53:01');
INSERT INTO `system_log` VALUES (36, 1, 1, '登录系统', '管理员admin登录成功', '0:0:0:0:0:0:0:1', '2026-05-24 13:51:54');
INSERT INTO `system_log` VALUES (37, 1, 1, '登录系统', '管理员admin登录成功', '0:0:0:0:0:0:0:1', '2026-05-26 16:23:50');
INSERT INTO `system_log` VALUES (38, 1, 1, '登录系统', '管理员admin登录成功', '0:0:0:0:0:0:0:1', '2026-05-26 16:30:46');
INSERT INTO `system_log` VALUES (39, 1, 1, '登录系统', '管理员admin登录成功', '0:0:0:0:0:0:0:1', '2026-05-27 11:27:43');
INSERT INTO `system_log` VALUES (40, 1, 1, '登录系统', '管理员admin登录成功', '0:0:0:0:0:0:0:1', '2026-05-27 12:07:46');
INSERT INTO `system_log` VALUES (41, 1, 1, '登录系统', '管理员admin登录成功', '0:0:0:0:0:0:0:1', '2026-05-27 12:23:45');
INSERT INTO `system_log` VALUES (42, 1, 1, '登录系统', '管理员admin登录成功', '0:0:0:0:0:0:0:1', '2026-05-27 12:23:58');
INSERT INTO `system_log` VALUES (43, 1, 1, '登录系统', '管理员admin登录成功', '0:0:0:0:0:0:0:1', '2026-05-27 12:50:03');
INSERT INTO `system_log` VALUES (44, 1, 1, '登录系统', '管理员admin登录成功', '0:0:0:0:0:0:0:1', '2026-05-27 13:06:43');
INSERT INTO `system_log` VALUES (45, 1, 1, '登录系统', '管理员admin登录成功', '0:0:0:0:0:0:0:1', '2026-05-27 13:08:55');

-- ----------------------------
-- Table structure for teacher
-- ----------------------------
DROP TABLE IF EXISTS `teacher`;
CREATE TABLE `teacher`  (
  `teacher_id` int NOT NULL AUTO_INCREMENT,
  `teacher_no` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `real_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `gender` tinyint NULL DEFAULT 0,
  `department_id` int NULL DEFAULT NULL,
  `title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `status` tinyint NULL DEFAULT 1,
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `last_login_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`teacher_id`) USING BTREE,
  UNIQUE INDEX `teacher_no`(`teacher_no`) USING BTREE,
  INDEX `department_id`(`department_id`) USING BTREE,
  CONSTRAINT `teacher_ibfk_1` FOREIGN KEY (`department_id`) REFERENCES `department` (`department_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of teacher
-- ----------------------------
INSERT INTO `teacher` VALUES (1, 'T2021001', 'e10adc3949ba59abbe56e057f20f883e', '张教授', 1, 1, '教授', 'zhang@school.edu.cn', '13900139001', 1, '2026-05-04 13:55:55', '2026-05-27 13:26:32');
INSERT INTO `teacher` VALUES (2, 'T2021002', 'e10adc3949ba59abbe56e057f20f883e', '李副教授', 2, 1, '副教授', 'li@school.edu.cn', '13900139002', 1, '2026-05-04 13:55:55', NULL);
INSERT INTO `teacher` VALUES (3, 'T2021003', 'e10adc3949ba59abbe56e057f20f883e', '王讲师', 1, 1, '讲师', 'wang@school.edu.cn', '13900139003', 1, '2026-05-04 13:55:55', NULL);
INSERT INTO `teacher` VALUES (4, 'T2021004', 'e10adc3949ba59abbe56e057f20f883e', '刘教授', 1, 2, '副教授', 'liu@school.edu.cn', '13900139004', 1, '2026-05-04 13:55:55', NULL);
INSERT INTO `teacher` VALUES (5, 'T2021005', 'e10adc3949ba59abbe56e057f20f883e', '陈副教授', 2, 2, '讲师', 'chen@school.edu.cn', '13900139005', 1, '2026-05-04 13:55:55', NULL);
INSERT INTO `teacher` VALUES (6, 'T2021006', 'e10adc3949ba59abbe56e057f20f883e', '杨教授', 2, 3, '教授', 'yang@school.edu.cn', '13900139006', 1, '2026-05-04 13:55:55', NULL);
INSERT INTO `teacher` VALUES (7, 'T2021007', 'e10adc3949ba59abbe56e057f20f883e', '赵副教授', 1, 4, '副教授', 'zhao@school.edu.cn', '13900139007', 1, '2026-05-04 13:55:55', NULL);
INSERT INTO `teacher` VALUES (8, 'T2021008', 'e10adc3949ba59abbe56e057f20f883e', '黄讲师', 2, 5, '讲师', 'huang@school.edu.cn', '13900139008', 1, '2026-05-04 13:55:55', NULL);
INSERT INTO `teacher` VALUES (9, 'T2021009', 'e10adc3949ba59abbe56e057f20f883e', '周教授', 1, 6, '教授', 'zhou@school.edu.cn', '13900139009', 1, '2026-05-04 13:55:55', NULL);
INSERT INTO `teacher` VALUES (10, 'T2021010', 'e10adc3949ba59abbe56e057f20f883e', '吴副教授', 2, 7, '副教授', 'wu@school.edu.cn', '13900139100', 1, '2026-05-04 13:55:55', NULL);
INSERT INTO `teacher` VALUES (11, 'T2021011', 'e10adc3949ba59abbe56e057f20f883e', '郑讲师', 1, 1, '讲师', 'zheng@school.edu.cn', '13900139101', 1, '2026-05-04 13:55:55', NULL);
INSERT INTO `teacher` VALUES (13, '1', 'e10adc3949ba59abbe56e057f20f883e', '111', 1, 1, '教授', '111@163.com', '17362637223', 1, '2026-05-20 12:16:24', NULL);

-- ----------------------------
-- Table structure for wrong_question
-- ----------------------------
DROP TABLE IF EXISTS `wrong_question`;
CREATE TABLE `wrong_question`  (
  `wrong_id` int NOT NULL AUTO_INCREMENT,
  `student_id` int NULL DEFAULT NULL,
  `question_id` int NULL DEFAULT NULL,
  `student_answer` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `correct_answer` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `wrong_count` int NULL DEFAULT 1,
  `last_wrong_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `subject_id` int NULL DEFAULT NULL,
  PRIMARY KEY (`wrong_id`) USING BTREE,
  INDEX `student_id`(`student_id`) USING BTREE,
  INDEX `question_id`(`question_id`) USING BTREE,
  INDEX `subject_id`(`subject_id`) USING BTREE,
  CONSTRAINT `wrong_question_ibfk_1` FOREIGN KEY (`student_id`) REFERENCES `student` (`student_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `wrong_question_ibfk_2` FOREIGN KEY (`question_id`) REFERENCES `question` (`question_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `wrong_question_ibfk_3` FOREIGN KEY (`subject_id`) REFERENCES `subject` (`subject_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wrong_question
-- ----------------------------
INSERT INTO `wrong_question` VALUES (1, 1, 4, 'A', 'B', 1, '2024-04-15 15:50:00', 1);
INSERT INTO `wrong_question` VALUES (2, 1, 7, 'A|B|C', 'A|B|C|D', 1, '2024-04-15 15:50:00', 1);
INSERT INTO `wrong_question` VALUES (3, 1, 9, '1', '0', 1, '2024-04-15 15:50:00', 1);
INSERT INTO `wrong_question` VALUES (4, 3, 4, 'C', 'B', 1, '2024-04-15 15:30:00', 1);
INSERT INTO `wrong_question` VALUES (5, 3, 7, 'A|B|D', 'A|B|C|D', 1, '2024-04-15 15:30:00', 1);
INSERT INTO `wrong_question` VALUES (6, 3, 9, '1', '0', 1, '2024-04-15 15:30:00', 1);
INSERT INTO `wrong_question` VALUES (7, 4, 5, 'C', 'D', 1, '2024-04-15 15:55:00', 1);
INSERT INTO `wrong_question` VALUES (8, 4, 8, '1', '0', 1, '2024-04-15 15:55:00', 1);
INSERT INTO `wrong_question` VALUES (9, 6, 1, 'A', 'C', 1, '2024-04-15 15:40:00', 1);
INSERT INTO `wrong_question` VALUES (10, 6, 3, 'A', 'C', 1, '2024-04-15 15:40:00', 1);
INSERT INTO `wrong_question` VALUES (11, 6, 5, 'C', 'D', 1, '2024-04-15 15:40:00', 1);
INSERT INTO `wrong_question` VALUES (12, 8, 2, 'B', 'C', 1, '2024-04-15 15:48:00', 1);

SET FOREIGN_KEY_CHECKS = 1;
