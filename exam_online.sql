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

 Date: 31/05/2026 15:28:01
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
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of admin
-- ----------------------------
INSERT INTO `admin` VALUES (1, 'admin', 'e10adc3949ba59abbe56e057f20f883e', '管理员', 'admin@163.com', '13800138000', 1, '2026-05-04 13:55:55', '2026-05-30 17:06:43');
INSERT INTO `admin` VALUES (2, 'admin01', 'e10adc3949ba59abbe56e057f20f883e', '张总', 'zhang@163.com', '13800138001', 1, '2026-05-04 13:55:55', NULL);
INSERT INTO `admin` VALUES (3, 'admin02', 'e10adc3949ba59abbe56e057f20f883e', '陈副总', 'chen@163.com', '13800138002', 1, '2026-05-04 13:55:55', NULL);
INSERT INTO `admin` VALUES (4, 'admin03', 'e10adc3949ba59abbe56e057f20f883e', '王经理', 'wang@163.com', '13800138003', 1, '2026-05-04 13:55:55', NULL);
INSERT INTO `admin` VALUES (5, 'admin04', 'e10adc3949ba59abbe56e057f20f883e', '刘副经理', 'liu@163.com', '13800138004', 1, '2026-05-04 13:55:55', NULL);

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
) ENGINE = InnoDB AUTO_INCREMENT = 140 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

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
INSERT INTO `answer_record` VALUES (11, 1, 11, '4', 1, 0.00, '2026-05-04 13:55:55');
INSERT INTO `answer_record` VALUES (12, 1, 12, '堆排序是将待排序元素构建成堆结构，然后进行排序。', 1, 0.00, '2026-05-04 13:55:55');
INSERT INTO `answer_record` VALUES (13, 2, 1, 'C', 1, 2.00, '2026-05-04 13:55:55');
INSERT INTO `answer_record` VALUES (14, 2, 2, 'C', 1, 2.00, '2026-05-04 13:55:55');
INSERT INTO `answer_record` VALUES (15, 2, 3, 'C', 1, 2.00, '2026-05-04 13:55:55');
INSERT INTO `answer_record` VALUES (16, 2, 4, 'B', 1, 2.00, '2026-05-04 13:55:55');
INSERT INTO `answer_record` VALUES (17, 2, 5, 'D', 1, 2.00, '2026-05-04 13:55:55');
INSERT INTO `answer_record` VALUES (18, 3, 1, 'A', 0, 0.00, '2026-05-04 13:55:55');
INSERT INTO `answer_record` VALUES (19, 3, 2, 'C', 1, 2.00, '2026-05-04 13:55:55');
INSERT INTO `answer_record` VALUES (20, 3, 3, 'A', 0, 0.00, '2026-05-04 13:55:55');
INSERT INTO `answer_record` VALUES (21, 3, 4, 'B', 1, 2.00, '2026-05-04 13:55:55');
INSERT INTO `answer_record` VALUES (139, 22, 21, 'Vue3相比Vue2的主要改进包括：更小的体积与更快的性能（基于Proxy的响应式系统、优化了虚拟DOM和Tree‑Shaking）、引入更灵活的Composition API以替代Options API、提供原生的TypeScript支持，并新增Fragment、Teleport、Suspense等内置组件，整体提升了开发体验与大型应用的维护性。', NULL, 0.00, '2026-05-30 22:32:16');
INSERT INTO `answer_record` VALUES (140, 22, 21, NULL, 0, 0.00, '2026-05-30 22:32:17');

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
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of class
-- ----------------------------
INSERT INTO `class` VALUES (1, '计算机科学与技术2026级1班', 'CS2601', 1, 2026, 13, '2026-05-04 13:55:55');
INSERT INTO `class` VALUES (2, '软件工程2026级1班', 'SE2601', 1, 2026, 42, '2026-05-04 13:55:55');
INSERT INTO `class` VALUES (3, '计算机科学与技术2026级11班', 'CS2611', 1, 2026, 48, '2026-05-04 13:55:55');
INSERT INTO `class` VALUES (4, '软件工程2026级2班', 'SE2602', 1, 2026, 40, '2026-05-04 13:55:55');
INSERT INTO `class` VALUES (5, '数学与应用数学2026级1班', 'MA2601', 2, 2026, 38, '2026-05-04 13:55:55');
INSERT INTO `class` VALUES (6, '统计学2026级1班', 'ST2601', 2, 2026, 35, '2026-05-04 13:55:55');
INSERT INTO `class` VALUES (7, '英语2026级1班', 'EN2601', 3, 2026, 50, '2026-05-04 13:55:55');
INSERT INTO `class` VALUES (8, '日语2026级1班', 'JP2601', 3, 2026, 32, '2026-05-04 13:55:55');
INSERT INTO `class` VALUES (9, '经济学2026级1班', 'EC2601', 4, 2026, 55, '2026-05-04 13:55:55');
INSERT INTO `class` VALUES (10, '金融学2026级1班', 'FI2601', 4, 2026, 48, '2026-05-04 13:55:55');
INSERT INTO `class` VALUES (11, '电子信息工程2026级1班', 'EE2601', 5, 2026, 46, '2026-05-04 13:55:55');
INSERT INTO `class` VALUES (12, '通信工程2026级1班', 'CE2601', 5, 2026, 43, '2026-05-04 13:55:55');
INSERT INTO `class` VALUES (14, '1', '1', 1, 2026, 0, '2026-05-27 20:59:45');

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
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

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
INSERT INTO `department` VALUES (15, '外交学院', 'WJ', '', '2026-05-30 14:33:13');

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
) ENGINE = InnoDB AUTO_INCREMENT = 25 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of exam
-- ----------------------------
INSERT INTO `exam` VALUES (1, '2026年春季数据结构与算法期中考试', 1, 1, '2026-05-01 14:00:00', '2026-05-01 16:00:00', 3, 3, 1, '2026-05-04 13:55:55');
INSERT INTO `exam` VALUES (2, '2026年春季数据结构与算法期末考试', 2, 1, '2026-05-29 17:00:00', '2026-05-29 17:30:00', 1, 3, 1, '2026-05-04 13:55:55');
INSERT INTO `exam` VALUES (3, '2026年春季操作系统期中考试', 3, 1, '2026-05-03 14:00:00', '2026-05-03 16:00:00', 2, 3, 2, '2026-05-04 13:55:55');
INSERT INTO `exam` VALUES (4, '2026年春季计算机网络期末考试', 4, 2, '2026-05-04 09:00:00', '2026-05-04 11:00:00', 1, 3, 3, '2026-05-04 13:55:55');
INSERT INTO `exam` VALUES (5, '2026年春季数据库原理期末考试', 5, 2, '2026-05-05 14:00:00', '2026-05-05 16:00:00', 1, 3, 2, '2026-05-04 13:55:55');
INSERT INTO `exam` VALUES (6, '2026年春季高等数学期中考试', 6, 5, '2026-05-06 09:00:00', '2026-05-06 11:30:00', 2, 3, 4, '2026-05-04 13:55:55');
INSERT INTO `exam` VALUES (7, '2026年春季线性代数期末考试', 7, 6, '2026-05-07 14:00:00', '2026-05-07 16:00:00', 1, 3, 5, '2026-05-04 13:55:55');
INSERT INTO `exam` VALUES (8, '2026年春季概率论期末考试', 8, 6, '2026-05-08 09:00:00', '2026-05-08 11:00:00', 1, 3, 4, '2026-05-04 13:55:55');
INSERT INTO `exam` VALUES (9, '2026年春季大学英语期中考试', 9, 7, '2026-05-09 14:00:00', '2026-05-09 16:00:00', 2, 3, 6, '2026-05-04 13:55:55');
INSERT INTO `exam` VALUES (10, '2026年春季微观经济学期末考试', 10, 9, '2026-05-09 17:00:00', '2026-05-09 19:00:00', 1, 3, 7, '2026-05-04 13:55:55');
INSERT INTO `exam` VALUES (11, '2026年春季电路原理期中考试', 11, 11, '2026-05-10 14:00:00', '2026-05-10 16:00:00', 1, 3, 8, '2026-05-04 13:55:55');
INSERT INTO `exam` VALUES (16, '1', 1, 1, '2026-05-27 08:00:00', '2026-05-27 10:00:00', 0, NULL, 1, '2026-05-28 21:53:44');
INSERT INTO `exam` VALUES (17, '2026年春季数据结构与算法月考(1月)', 1, 1, '2026-01-15 14:00:00', '2026-01-15 16:00:00', 3, 3, 1, '2026-05-29 22:00:10');
INSERT INTO `exam` VALUES (18, '2026年春季高等数学月考(1月)', 6, 5, '2026-01-18 09:00:00', '2026-01-18 11:30:00', 3, 3, 4, '2026-05-29 22:00:10');
INSERT INTO `exam` VALUES (19, '2026年春季操作系统月考(2月)', 3, 1, '2026-02-20 14:00:00', '2026-02-20 16:00:00', 3, 3, 2, '2026-05-29 22:00:26');
INSERT INTO `exam` VALUES (20, '2026年春季计算机网络月考(2月)', 4, 2, '2026-02-22 09:00:00', '2026-02-22 11:00:00', 3, 3, 3, '2026-05-29 22:00:26');
INSERT INTO `exam` VALUES (21, '2026年春季数据库原理月考(3月)', 5, 2, '2026-03-12 14:00:00', '2026-03-12 16:00:00', 3, 3, 2, '2026-05-29 22:00:32');
INSERT INTO `exam` VALUES (22, '2026年春季线性代数月考(3月)', 7, 6, '2026-03-18 09:00:00', '2026-03-18 11:00:00', 3, 3, 5, '2026-05-29 22:00:32');
INSERT INTO `exam` VALUES (23, '2026年春季概率论月考(4月)', 8, 6, '2026-04-08 09:00:00', '2026-04-08 11:00:00', 3, 3, 4, '2026-05-29 22:00:37');
INSERT INTO `exam` VALUES (24, '2026年春季大学英语月考(4月)', 9, 7, '2026-04-15 14:00:00', '2026-04-15 16:00:00', 3, 3, 6, '2026-05-29 22:00:37');
INSERT INTO `exam` VALUES (25, 'Vue3 框架测试', 15, 1, '2026-05-30 21:30:00', '2026-05-30 23:30:00', 1, NULL, 1, '2026-05-30 21:15:39');

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
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of exam_paper
-- ----------------------------
INSERT INTO `exam_paper` VALUES (1, '数据结构与算法A卷', 1, 31.00, 60.00, 120, 12, 1, '2026-05-04 13:55:55', 1);
INSERT INTO `exam_paper` VALUES (2, '数据结构与算法B卷', 1, 10.00, 60.00, 120, 5, 1, '2026-05-04 13:55:55', 1);
INSERT INTO `exam_paper` VALUES (3, '操作系统期中考试A卷', 2, 100.00, 60.00, 120, 20, 2, '2026-05-04 13:55:55', 1);
INSERT INTO `exam_paper` VALUES (4, '计算机网络期末考试A卷', 3, 100.00, 60.00, 120, 25, 3, '2026-05-04 13:55:55', 1);
INSERT INTO `exam_paper` VALUES (5, '数据库原理期末考试A卷', 4, 100.00, 60.00, 120, 25, 2, '2026-05-04 13:55:55', 1);
INSERT INTO `exam_paper` VALUES (6, '高等数学期中考试A卷', 5, 100.00, 60.00, 150, 20, 4, '2026-05-04 13:55:55', 1);
INSERT INTO `exam_paper` VALUES (7, '线性代数期末考试A卷', 6, 100.00, 60.00, 120, 20, 5, '2026-05-04 13:55:55', 1);
INSERT INTO `exam_paper` VALUES (8, '概率论期末考试A卷', 7, 100.00, 60.00, 120, 20, 4, '2026-05-04 13:55:55', 1);
INSERT INTO `exam_paper` VALUES (9, '大学英语期中考试A卷', 8, 100.00, 60.00, 120, 30, 6, '2026-05-04 13:55:55', 1);
INSERT INTO `exam_paper` VALUES (10, '微观经济学期末考试A卷', 9, 100.00, 60.00, 120, 25, 7, '2026-05-04 13:55:55', 1);
INSERT INTO `exam_paper` VALUES (11, '电路原理期中考试A卷', 11, 100.00, 60.00, 120, 20, 8, '2026-05-04 13:55:55', 1);
INSERT INTO `exam_paper` VALUES (14, 'SpringBoot 框架A卷', 16, 100.00, NULL, 120, NULL, 1, '2026-05-30 14:42:59', 1);
INSERT INTO `exam_paper` VALUES (15, 'Vue 3 框架A卷', 17, 100.00, NULL, 120, NULL, 1, '2026-05-30 21:13:34', 1);

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
) ENGINE = InnoDB AUTO_INCREMENT = 22 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of exam_record
-- ----------------------------
INSERT INTO `exam_record` VALUES (1, 1, 1, '2026-05-28 13:00:00', '2026-05-28 15:50:00', 68.50, 68.50, 0.00, 2, 1, NULL);
INSERT INTO `exam_record` VALUES (2, 1, 2, '2026-05-28 13:00:00', '2026-05-28 15:50:00', 92.00, 72.00, 20.00, 1, 0, NULL);
INSERT INTO `exam_record` VALUES (3, 1, 3, '2026-05-28 13:00:00', '2026-05-28 15:50:00', 76.50, 56.50, 20.00, 1, 2, NULL);
INSERT INTO `exam_record` VALUES (4, 1, 4, '2026-05-28 13:00:00', '2026-05-28 15:50:00', 85.00, 65.00, 20.00, 1, 1, NULL);
INSERT INTO `exam_record` VALUES (5, 1, 5, '2026-05-28 13:00:00', '2026-05-28 15:50:00', 90.50, 70.50, 20.00, 1, 0, NULL);
INSERT INTO `exam_record` VALUES (6, 1, 6, '2026-05-28 13:00:00', '2026-05-28 15:50:00', 72.00, 52.00, 20.00, 1, 3, NULL);
INSERT INTO `exam_record` VALUES (7, 1, 7, '2026-05-28 13:00:00', '2026-05-28 15:50:00', 95.00, 75.00, 20.00, 1, 0, NULL);
INSERT INTO `exam_record` VALUES (8, 1, 8, '2026-05-28 13:00:00', '2026-05-28 15:50:00', 81.00, 61.00, 20.00, 1, 1, NULL);
INSERT INTO `exam_record` VALUES (9, 1, 9, '2026-05-28 13:00:00', '2026-05-28 15:50:00', 78.50, 58.50, 20.00, 1, 2, NULL);
INSERT INTO `exam_record` VALUES (10, 1, 10, '2026-05-28 13:00:00', '2026-05-28 15:50:00', 86.00, 66.00, 20.00, 1, 1, NULL);
INSERT INTO `exam_record` VALUES (11, 1, 11, '2026-05-28 13:00:00', '2026-05-28 15:50:00', 89.00, 69.00, 20.00, 1, 0, NULL);
INSERT INTO `exam_record` VALUES (12, 6, 1, '2026-05-28 13:00:00', '2026-05-28 15:50:00', 82.00, 82.00, 0.00, 1, 0, NULL);
INSERT INTO `exam_record` VALUES (13, 2, 1, '2026-05-29 17:00:41', NULL, 0.00, 0.00, 0.00, 0, 4, '0:0:0:0:0:0:0:1');
INSERT INTO `exam_record` VALUES (14, 17, 1, '2026-01-15 14:00:00', '2026-01-15 15:30:00', 72.50, 40.00, 32.50, 1, 0, NULL);
INSERT INTO `exam_record` VALUES (15, 18, 1, '2026-01-18 09:00:00', '2026-01-18 10:45:00', 68.00, 35.00, 33.00, 1, 0, NULL);
INSERT INTO `exam_record` VALUES (16, 19, 1, '2026-02-20 14:00:00', '2026-02-20 15:40:00', 75.00, 42.00, 33.00, 1, 0, NULL);
INSERT INTO `exam_record` VALUES (17, 20, 1, '2026-02-22 09:00:00', '2026-02-22 10:50:00', 79.50, 44.00, 35.50, 1, 0, NULL);
INSERT INTO `exam_record` VALUES (18, 21, 1, '2026-03-12 14:00:00', '2026-03-12 15:35:00', 81.00, 45.00, 36.00, 1, 0, NULL);
INSERT INTO `exam_record` VALUES (19, 22, 1, '2026-03-18 09:00:00', '2026-03-18 10:40:00', 77.50, 43.00, 34.50, 1, 0, NULL);
INSERT INTO `exam_record` VALUES (20, 23, 1, '2026-04-08 09:00:00', '2026-04-08 10:30:00', 83.50, 46.00, 37.50, 1, 0, NULL);
INSERT INTO `exam_record` VALUES (21, 24, 1, '2026-04-15 14:00:00', '2026-04-15 15:45:00', 86.00, 48.00, 38.00, 1, 0, NULL);
INSERT INTO `exam_record` VALUES (22, 25, 1, '2026-05-30 21:30:27', '2026-05-30 22:32:17', 0.00, 0.00, 0.00, 2, 7, '0:0:0:0:0:0:0:1');

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
) ENGINE = InnoDB AUTO_INCREMENT = 32 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

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
INSERT INTO `paper_question` VALUES (31, 14, 20, 1, 5.00);
INSERT INTO `paper_question` VALUES (32, 15, 21, 1, 10.00);

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
) ENGINE = InnoDB AUTO_INCREMENT = 45 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

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
INSERT INTO `question` VALUES (18, 1, 1, '11', '11', '11', 2.00, 1, '11', 1, '2026-05-28 16:33:41', '2026-05-28 16:33:41');
INSERT INTO `question` VALUES (19, 1, 6, '写个冒泡排序', '', '略', 5.00, 1, '排序算法', 1, '2026-05-28 18:03:03', '2026-05-28 18:03:03');
INSERT INTO `question` VALUES (20, 16, 5, '简单介绍一下 IOC 与 AOP', '', 'IoC（控制反转）将对象的创建与依赖关系的管理权从代码转移给容器，通过依赖注入实现模块间的松耦合；AOP（面向切面编程）则把日志、事务等横切关注点从业务逻辑中剥离，利用动态代理在运行时无侵入地增强方法。两者相辅相成：IoC为AOP提供灵活的代理对象管理，AOP则基于IoC容器优雅实现横切逻辑。', 5.00, 2, 'IOC,AOP', 1, '2026-05-30 14:42:18', '2026-05-30 14:42:18');
INSERT INTO `question` VALUES (21, 17, 5, '请说明 Vue3 相比于 Vue2 有哪些改进？', '', 'Vue3相比Vue2的主要改进包括：更小的体积与更快的性能（基于Proxy的响应式系统、优化了虚拟DOM和Tree‑Shaking）、引入更灵活的Composition API以替代Options API、提供原生的TypeScript支持，并新增Fragment、Teleport、Suspense等内置组件，整体提升了开发体验与大型应用的维护性。', 10.00, 2, 'Vue3，Vue2', 1, '2026-05-30 20:44:00', '2026-05-30 20:44:00');
INSERT INTO `question` VALUES (22, 16, 1, 'Spring Boot 中，用于开启自动配置的核心注解是？', 'A.@SpringBootApplication\\nB.@EnableAutoConfiguration\\nC.@Configuration\\nD.@ComponentScan', 'B', 2.00, 1, '核心注解', 1, '2026-05-30 20:48:51', '2026-05-30 20:48:51');
INSERT INTO `question` VALUES (23, 16, 1, 'Spring Boot 启动时，默认内嵌的 Web 容器是？', 'A.Jetty\\nB.Undertow\\nC.Tomcat\\nD.Netty', 'C', 2.00, 1, '', 1, '2026-05-30 20:51:02', '2026-05-30 20:51:02');
INSERT INTO `question` VALUES (24, 16, 1, '关于 application.properties 与 application.yml，下列说法正确的是？', 'A.两者只能同时存在一个\\nB.YAML 文件不支持列表写法\\nC.Spring Boot 优先加载application.yml\\nD.两者可以共存，且 application.properties 优先级更高', 'D', 2.00, 1, '', 1, '2026-05-30 20:52:10', '2026-05-30 20:53:26');
INSERT INTO `question` VALUES (25, 16, 1, '以下哪个依赖不是 Spring Boot Starter 中的核心 Starter？', 'A.spring-boot-starter-web\\nB.spring-boot-starter-data-jpa\\nC.spring-boot-starter-logging\\nD.spring-boot-starter-security-core', 'D', 2.00, 1, '', 1, '2026-05-30 20:52:58', '2026-05-30 20:52:58');
INSERT INTO `question` VALUES (26, 16, 1, 'Spring Boot Actuator 端点 /health 默认返回的信息包含？', 'A.所有组件的详细健康状态\\nB.仅 “UP” 或 “DOWN” 状态\\nC.服务器内存使用详情\\nD.线程堆栈信息', 'B', 2.00, 2, '', 1, '2026-05-30 20:54:29', '2026-05-30 21:03:53');
INSERT INTO `question` VALUES (27, 16, 2, '@SpringBootApplication 注解组合了以下哪些注解？', 'A.@Configuration\\nB.@EnableAutoConfiguration\\nC.@ComponentScan\\nD.@SpringBootConfiguration', 'A|B|C|D', 5.00, 1, '', 1, '2026-05-30 20:56:25', '2026-05-30 20:56:25');
INSERT INTO `question` VALUES (28, 16, 2, 'Spring Boot 支持的外部配置源包括？\n', 'A.命令行参数\\nB.Java 系统属性（System.getProperties()）\\nC.操作系统环境变量\\nD.application-{profile}.properties 文件', 'A|B|C|D', 5.00, 1, '', 1, '2026-05-30 20:58:27', '2026-05-30 20:58:27');
INSERT INTO `question` VALUES (29, 16, 2, '关于 spring-boot-starter-parent 的作用，下列说法正确的有？\n', 'A.提供了统一的依赖版本管理\\nB.默认定义了 Java 编译版本\\nC.包含了 Spring Boot 的 Maven 插件配置\\nD.强制要求子模块必须继承它', 'A|B|C', 5.00, 2, '', 1, '2026-05-30 20:59:11', '2026-05-30 20:59:11');
INSERT INTO `question` VALUES (30, 16, 2, 'Spring Boot 中配置日志级别的方式有？\n', 'A.在 application.properties 中设置 logging.level.com.example=DEBUG\\nB.使用 logback-spring.xml 配置文件\\nC.通过 Actuator 的 /loggers 端点动态修改\\nD.只能通过代码设置', 'A|B|C', 5.00, 2, '', 1, '2026-05-30 20:59:59', '2026-05-30 20:59:59');
INSERT INTO `question` VALUES (31, 16, 3, 'Spring Boot 要求项目必须继承 spring-boot-starter-parent 才能工作。', '', '0', 2.00, 1, '', 1, '2026-05-30 21:00:58', '2026-05-30 21:00:58');
INSERT INTO `question` VALUES (32, 16, 3, '@RestController 注解等同于 @Controller + @ResponseBody。', '', '1', 2.00, 1, '', 1, '2026-05-30 21:01:25', '2026-05-30 21:01:25');
INSERT INTO `question` VALUES (33, 16, 3, 'Spring Boot 的嵌入式 Tomcat 不支持 JSP 渲染。', '', '0', 2.00, 1, '', 1, '2026-05-30 21:01:43', '2026-05-30 21:01:43');
INSERT INTO `question` VALUES (34, 16, 3, 'spring-boot-devtools 在生产环境中应默认启用，以便热部署。', '', '0', 2.00, 2, '', 1, '2026-05-30 21:01:59', '2026-05-30 21:01:59');
INSERT INTO `question` VALUES (35, 16, 3, '使用 @ConfigurationProperties 绑定时，属性名支持松散绑定（如 user-name 映射到 userName）。\n', '', '1', 2.00, 1, '', 1, '2026-05-30 21:02:30', '2026-05-30 21:02:30');
INSERT INTO `question` VALUES (36, 16, 4, 'Spring Boot 应用的入口类通常需要使用 ________ 注解标注，并调用 SpringApplication.run() 方法。', '', '@SpringBootApplication', 2.00, 1, '', 1, '2026-05-30 21:06:15', '2026-05-30 21:06:15');
INSERT INTO `question` VALUES (37, 16, 4, '在 Spring Boot 中，要开启 Actuator 的所有 Web 端点，需要在配置文件中设置 management.endpoints.web.exposure.include = ________。', '', '*', 2.00, 1, '', 1, '2026-05-30 21:06:34', '2026-05-30 21:06:34');
INSERT INTO `question` VALUES (38, 16, 4, '如果要切换内嵌 Web 容器为 Undertow，需要排除 Tomcat 依赖并添加 spring-boot-starter-________。', '', 'undertow', 2.00, 2, '', 1, '2026-05-30 21:07:29', '2026-05-30 21:07:29');
INSERT INTO `question` VALUES (39, 16, 4, 'Spring Boot 中通过 @Value 注解注入配置值时，若配置不存在希望不报错，可以设置默认值写法如 @Value(\"${some.key:________}\")。', '', 'defaultValue', 2.00, 2, '', 1, '2026-05-30 21:07:51', '2026-05-30 21:07:51');
INSERT INTO `question` VALUES (40, 16, 4, 'Spring Boot 支持的两种配置文件格式是 .properties 和 .________。', '', 'yaml', 2.00, 1, '', 1, '2026-05-30 21:08:19', '2026-05-30 21:08:19');
INSERT INTO `question` VALUES (41, 16, 5, '简述 Spring Boot 自动配置的原理。', '', 'Spring Boot 启动时，@EnableAutoConfiguration 会通过 SpringFactoriesLoader 加载 META-INF/spring.factories 文件中所有 EnableAutoConfiguration 键对应的配置类。这些配置类上通常带有 @Conditional 系列条件注解（如 @ConditionalOnClass、@ConditionalOnMissingBean），根据类路径是否存在某个类、容器中是否已有某个 Bean 等条件，决定是否执行配置逻辑，从而按需创建 Bean，实现了“约定大于配置”的自动装配。', 5.00, 2, '', 1, '2026-05-30 21:08:58', '2026-05-30 21:08:58');
INSERT INTO `question` VALUES (42, 16, 5, 'application.properties 和 application.yml 各自的优缺点？', '', 'properties：格式简单，键值对无缩进，适合简单配置；但不支持层级结构，重复前缀冗余。  yml：层级清晰，可读性好，支持列表和复合结构；但对缩进敏感，且不能通过 @PropertySource 直接加载 YAML（需要额外处理）。', 5.00, 2, '', 1, '2026-05-30 21:09:16', '2026-05-30 21:09:16');
INSERT INTO `question` VALUES (43, 16, 5, 'Spring Boot Actuator 中 /info 和 /health 端点的常见用途是什么？如何自定义 /info 内容？\n', '', '/health：用于检查应用健康状态，常用于容器编排探针（liveness/readiness）。  /info：暴露应用元信息（版本、构建时间、Git 提交等）。  自定义 /info：在 application.properties 中用 info.app.name=xxx 形式配置，或实现 InfoContributor 接口。', 5.00, 3, '', 1, '2026-05-30 21:10:42', '2026-05-30 21:11:00');
INSERT INTO `question` VALUES (44, 16, 6, '编写一个 Spring Boot REST 接口，提供两个功能：\nGET /hello 返回字符串 “Hello Spring Boot”。\nPOST /add 接收 JSON 对象 { \"a\": 10, \"b\": 5 }，返回两数之和。\n要求：使用 @RestController，定义一个 AddRequest 实体类。', '', '// AddRequest.java public class AddRequest {     private int a;     private int b;     // getters and setters     public int getA() { return a; }     public void setA(int a) { this.a = a; }     public int getB() { return b; }     public void setB(int b) { this.b = b; } }  // DemoController.java @RestController public class DemoController {     @GetMapping(\"/hello\")     public String hello() {         return \"Hello Spring Boot\";     }      @PostMapping(\"/add\")     public int add(@RequestBody AddRequest req) {         return req.getA() + req.getB();     } }', 15.00, 2, '', 1, '2026-05-30 21:12:04', '2026-05-30 21:12:04');
INSERT INTO `question` VALUES (45, 16, 6, '编写一个 Spring Boot 启动类，并配置一个自定义的 CommandLineRunner，在启动时打印 “Application started!” 以及所有环境变量中以 JAVA_ 开头的变量名。', '', '@SpringBootApplication public class MyApplication {     public static void main(String[] args) {         SpringApplication.run(MyApplication.class, args);     }      @Bean     public CommandLineRunner runner() {         return args -> {             System.out.println(\"Application started!\");             System.getenv().forEach((key, value) -> {                 if (key.startsWith(\"JAVA_\")) {                     System.out.println(key + \" = \" + value);                 }             });         };     } }', 15.00, 2, '', 1, '2026-05-30 21:12:26', '2026-05-30 21:12:26');

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
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of student
-- ----------------------------
INSERT INTO `student` VALUES (1, '2021001001', 'e10adc3949ba59abbe56e057f20f883e', '张三', 1, 1, 'zhangsan@student.school.edu.cn', '18800188001', 1, '2026-05-04 13:55:55', '2026-05-31 13:39:28');
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
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

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
INSERT INTO `subject` VALUES (13, '外交管理', 'WJ2601', 15, 1.5, NULL, '2026-05-30 14:34:17');
INSERT INTO `subject` VALUES (14, '电气及其自动化', 'JX2601', 6, 3.0, NULL, '2026-05-30 14:34:54');
INSERT INTO `subject` VALUES (15, '智能建造', 'TM2601', 7, 2.0, NULL, '2026-05-30 14:35:52');
INSERT INTO `subject` VALUES (16, 'SpringBoot 框架基础', 'CS2601', 1, 3.0, NULL, '2026-05-30 14:36:39');
INSERT INTO `subject` VALUES (17, 'Vue3 框架基础', 'CS2602', 1, 3.0, NULL, '2026-05-30 14:37:04');

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
) ENGINE = InnoDB AUTO_INCREMENT = 132 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

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
INSERT INTO `system_log` VALUES (46, 1, 1, '登录系统', '管理员admin登录成功', '0:0:0:0:0:0:0:1', '2026-05-27 16:29:16');
INSERT INTO `system_log` VALUES (47, 1, 1, '登录系统', '管理员admin登录成功', '0:0:0:0:0:0:0:1', '2026-05-27 17:51:38');
INSERT INTO `system_log` VALUES (48, 1, 1, '新增', '新增学生：2（学号：22）', '0:0:0:0:0:0:0:1', '2026-05-27 21:21:51');
INSERT INTO `system_log` VALUES (49, 1, 1, '删除', '删除学生：2（学号：22）', '0:0:0:0:0:0:0:1', '2026-05-27 21:22:19');
INSERT INTO `system_log` VALUES (50, 1, 1, '登录系统', '管理员admin登录成功', '0:0:0:0:0:0:0:1', '2026-05-28 15:41:21');
INSERT INTO `system_log` VALUES (51, 1, 1, '登录系统', '管理员admin登录成功', '0:0:0:0:0:0:0:1', '2026-05-28 15:49:08');
INSERT INTO `system_log` VALUES (69, 2, 1, '更新试卷', '数据结构与算法A卷', '0:0:0:0:0:0:0:1', '2026-05-30 11:08:09');
INSERT INTO `system_log` VALUES (70, 2, 1, '更新试卷', '数据结构与算法B卷', '0:0:0:0:0:0:0:1', '2026-05-30 11:08:28');
INSERT INTO `system_log` VALUES (71, 2, 1, '删除试卷', 'ID: 13', '0:0:0:0:0:0:0:1', '2026-05-30 11:13:15');
INSERT INTO `system_log` VALUES (72, 1, 1, '登录系统', '管理员admin登录成功', '0:0:0:0:0:0:0:1', '2026-05-30 11:19:38');
INSERT INTO `system_log` VALUES (73, 1, 1, '登录系统', '管理员admin登录成功', '0:0:0:0:0:0:0:1', '2026-05-30 13:42:17');
INSERT INTO `system_log` VALUES (74, 1, 1, '登录系统', '管理员admin登录成功', '0:0:0:0:0:0:0:1', '2026-05-30 13:45:04');
INSERT INTO `system_log` VALUES (75, 1, 1, '登录系统', '管理员admin登录成功', '0:0:0:0:0:0:0:1', '2026-05-30 13:51:11');
INSERT INTO `system_log` VALUES (76, 1, 1, '登录系统', '管理员admin登录成功', '0:0:0:0:0:0:0:1', '2026-05-30 14:27:27');
INSERT INTO `system_log` VALUES (77, 1, 1, '新增', '新增院系：外交学院', '0:0:0:0:0:0:0:1', '2026-05-30 14:33:13');
INSERT INTO `system_log` VALUES (78, 1, 1, '修改', '修改院系：外交学院', '0:0:0:0:0:0:0:1', '2026-05-30 14:33:48');
INSERT INTO `system_log` VALUES (79, 1, 1, '新增', '新增学科：外交管理', '0:0:0:0:0:0:0:1', '2026-05-30 14:34:17');
INSERT INTO `system_log` VALUES (80, 1, 1, '修改', '修改院系：外交学院', '0:0:0:0:0:0:0:1', '2026-05-30 14:34:20');
INSERT INTO `system_log` VALUES (81, 1, 1, '修改', '修改学科：外交管理', '0:0:0:0:0:0:0:1', '2026-05-30 14:34:27');
INSERT INTO `system_log` VALUES (82, 1, 1, '修改', '修改院系：外交学院', '0:0:0:0:0:0:0:1', '2026-05-30 14:34:29');
INSERT INTO `system_log` VALUES (83, 1, 1, '新增', '新增学科：电气及其自动化', '0:0:0:0:0:0:0:1', '2026-05-30 14:34:54');
INSERT INTO `system_log` VALUES (84, 1, 1, '修改', '修改院系：机械工程学院', '0:0:0:0:0:0:0:1', '2026-05-30 14:34:56');
INSERT INTO `system_log` VALUES (85, 1, 1, '修改', '修改院系：土木工程学院', '0:0:0:0:0:0:0:1', '2026-05-30 14:35:31');
INSERT INTO `system_log` VALUES (86, 1, 1, '新增', '新增学科：智能建造', '0:0:0:0:0:0:0:1', '2026-05-30 14:35:53');
INSERT INTO `system_log` VALUES (87, 1, 1, '修改', '修改院系：土木工程学院', '0:0:0:0:0:0:0:1', '2026-05-30 14:35:53');
INSERT INTO `system_log` VALUES (88, 1, 1, '新增', '新增学科：SpringBoot 框架基础', '0:0:0:0:0:0:0:1', '2026-05-30 14:36:39');
INSERT INTO `system_log` VALUES (89, 1, 1, '新增', '新增学科：Vue3 框架基础', '0:0:0:0:0:0:0:1', '2026-05-30 14:37:04');
INSERT INTO `system_log` VALUES (90, 1, 1, '修改', '修改院系：计算机科学与技术学院', '0:0:0:0:0:0:0:1', '2026-05-30 14:37:09');
INSERT INTO `system_log` VALUES (91, 1, 1, '登录系统', '管理员admin登录成功', '0:0:0:0:0:0:0:1', '2026-05-30 14:39:03');
INSERT INTO `system_log` VALUES (92, 2, 1, '创建题目', '简单介绍一下 IOC 与 AOP', '0:0:0:0:0:0:0:1', '2026-05-30 14:42:18');
INSERT INTO `system_log` VALUES (93, 2, 1, '创建试卷', 'SpringBoot 框架A卷', '0:0:0:0:0:0:0:1', '2026-05-30 14:42:59');
INSERT INTO `system_log` VALUES (94, 2, 1, '添加试题到试卷', 'ID: 14', '0:0:0:0:0:0:0:1', '2026-05-30 14:43:16');
INSERT INTO `system_log` VALUES (95, 1, 1, '登录系统', '管理员admin登录成功', '0:0:0:0:0:0:0:1', '2026-05-30 17:04:45');
INSERT INTO `system_log` VALUES (96, 1, 1, '登录系统', '管理员admin登录成功', '0:0:0:0:0:0:0:1', '2026-05-30 17:05:03');
INSERT INTO `system_log` VALUES (97, 1, 1, '登录系统', '管理员admin登录成功', '0:0:0:0:0:0:0:1', '2026-05-30 17:06:43');
INSERT INTO `system_log` VALUES (98, 2, 1, '创建题目', '请说明 Vue3 相比于 Vue2 有哪些改进？', '0:0:0:0:0:0:0:1', '2026-05-30 20:44:00');
INSERT INTO `system_log` VALUES (99, 2, 1, '创建题目', 'Spring Boot 中，用于开启自动配置的核心注解是？', '0:0:0:0:0:0:0:1', '2026-05-30 20:48:51');
INSERT INTO `system_log` VALUES (100, 2, 1, '创建题目', 'Spring Boot 启动时，默认内嵌的 Web 容器是？', '0:0:0:0:0:0:0:1', '2026-05-30 20:51:02');
INSERT INTO `system_log` VALUES (101, 2, 1, '创建题目', '关于 application.properties 与 ap...', '0:0:0:0:0:0:0:1', '2026-05-30 20:52:10');
INSERT INTO `system_log` VALUES (102, 2, 1, '创建题目', '以下哪个依赖不是 Spring Boot Starter 中...', '0:0:0:0:0:0:0:1', '2026-05-30 20:52:58');
INSERT INTO `system_log` VALUES (103, 2, 1, '更新题目', '关于 application.properties 与 ap...', '0:0:0:0:0:0:0:1', '2026-05-30 20:53:26');
INSERT INTO `system_log` VALUES (104, 2, 1, '创建题目', 'Spring Boot Actuator 端点 /healt...', '0:0:0:0:0:0:0:1', '2026-05-30 20:54:29');
INSERT INTO `system_log` VALUES (105, 2, 1, '创建题目', '@SpringBootApplication 注解组合了以下...', '0:0:0:0:0:0:0:1', '2026-05-30 20:56:25');
INSERT INTO `system_log` VALUES (106, 2, 1, '创建题目', 'Spring Boot 支持的外部配置源包括？\n', '0:0:0:0:0:0:0:1', '2026-05-30 20:58:27');
INSERT INTO `system_log` VALUES (107, 2, 1, '创建题目', '关于 spring-boot-starter-parent ...', '0:0:0:0:0:0:0:1', '2026-05-30 20:59:11');
INSERT INTO `system_log` VALUES (108, 2, 1, '创建题目', 'Spring Boot 中配置日志级别的方式有？\n', '0:0:0:0:0:0:0:1', '2026-05-30 20:59:59');
INSERT INTO `system_log` VALUES (109, 2, 1, '创建题目', 'Spring Boot 要求项目必须继承 spring-bo...', '0:0:0:0:0:0:0:1', '2026-05-30 21:00:58');
INSERT INTO `system_log` VALUES (110, 2, 1, '创建题目', '@RestController 注解等同于 @Control...', '0:0:0:0:0:0:0:1', '2026-05-30 21:01:25');
INSERT INTO `system_log` VALUES (111, 2, 1, '创建题目', 'Spring Boot 的嵌入式 Tomcat 不支持 JS...', '0:0:0:0:0:0:0:1', '2026-05-30 21:01:43');
INSERT INTO `system_log` VALUES (112, 2, 1, '创建题目', 'spring-boot-devtools 在生产环境中应默认...', '0:0:0:0:0:0:0:1', '2026-05-30 21:01:59');
INSERT INTO `system_log` VALUES (113, 2, 1, '创建题目', '使用 @ConfigurationProperties 绑定...', '0:0:0:0:0:0:0:1', '2026-05-30 21:02:30');
INSERT INTO `system_log` VALUES (114, 2, 1, '更新题目', 'Spring Boot Actuator 端点 /healt...', '0:0:0:0:0:0:0:1', '2026-05-30 21:03:39');
INSERT INTO `system_log` VALUES (115, 2, 1, '更新题目', 'Spring Boot Actuator 端点 /healt...', '0:0:0:0:0:0:0:1', '2026-05-30 21:03:53');
INSERT INTO `system_log` VALUES (116, 2, 1, '创建题目', 'Spring Boot 应用的入口类通常需要使用 _____...', '0:0:0:0:0:0:0:1', '2026-05-30 21:06:15');
INSERT INTO `system_log` VALUES (117, 2, 1, '创建题目', '在 Spring Boot 中，要开启 Actuator 的...', '0:0:0:0:0:0:0:1', '2026-05-30 21:06:34');
INSERT INTO `system_log` VALUES (118, 2, 1, '创建题目', '如果要切换内嵌 Web 容器为 Undertow，需要排除 ...', '0:0:0:0:0:0:0:1', '2026-05-30 21:07:29');
INSERT INTO `system_log` VALUES (119, 2, 1, '创建题目', 'Spring Boot 中通过 @Value 注解注入配置值...', '0:0:0:0:0:0:0:1', '2026-05-30 21:07:51');
INSERT INTO `system_log` VALUES (120, 2, 1, '创建题目', 'Spring Boot 支持的两种配置文件格式是 .prop...', '0:0:0:0:0:0:0:1', '2026-05-30 21:08:19');
INSERT INTO `system_log` VALUES (121, 2, 1, '创建题目', '简述 Spring Boot 自动配置的原理。', '0:0:0:0:0:0:0:1', '2026-05-30 21:08:58');
INSERT INTO `system_log` VALUES (122, 2, 1, '创建题目', 'application.properties 和 appli...', '0:0:0:0:0:0:0:1', '2026-05-30 21:09:16');
INSERT INTO `system_log` VALUES (123, 2, 1, '创建题目', 'Spring Boot Actuator 中 /info 和...', '0:0:0:0:0:0:0:1', '2026-05-30 21:10:42');
INSERT INTO `system_log` VALUES (124, 2, 1, '更新题目', 'Spring Boot Actuator 中 /info 和...', '0:0:0:0:0:0:0:1', '2026-05-30 21:11:00');
INSERT INTO `system_log` VALUES (125, 2, 1, '更新题目', 'Spring Boot Actuator 中 /info 和...', '0:0:0:0:0:0:0:1', '2026-05-30 21:11:07');
INSERT INTO `system_log` VALUES (126, 2, 1, '更新题目', 'Spring Boot Actuator 中 /info 和...', '0:0:0:0:0:0:0:1', '2026-05-30 21:11:17');
INSERT INTO `system_log` VALUES (127, 2, 1, '创建题目', '编写一个 Spring Boot REST 接口，提供两个功...', '0:0:0:0:0:0:0:1', '2026-05-30 21:12:04');
INSERT INTO `system_log` VALUES (128, 2, 1, '创建题目', '编写一个 Spring Boot 启动类，并配置一个自定义的...', '0:0:0:0:0:0:0:1', '2026-05-30 21:12:26');
INSERT INTO `system_log` VALUES (129, 2, 1, '创建试卷', 'Vue 3 框架A卷', '0:0:0:0:0:0:0:1', '2026-05-30 21:13:34');
INSERT INTO `system_log` VALUES (130, 2, 1, '添加试题到试卷', 'ID: 15', '0:0:0:0:0:0:0:1', '2026-05-30 21:13:45');
INSERT INTO `system_log` VALUES (131, 2, 1, '创建考试', 'Vue3 框架测试', '0:0:0:0:0:0:0:1', '2026-05-30 21:15:39');
INSERT INTO `system_log` VALUES (132, 2, 1, '发布考试', 'ID: 25', '0:0:0:0:0:0:0:1', '2026-05-30 21:17:13');

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
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of teacher
-- ----------------------------
INSERT INTO `teacher` VALUES (1, 'T2021001', 'e10adc3949ba59abbe56e057f20f883e', '张教授', 1, 1, '教授', 'zhang@school.edu.cn', '13900139001', 1, '2026-05-04 13:55:55', '2026-05-30 22:34:34');
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
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

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
