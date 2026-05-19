CREATE DATABASE IF NOT EXISTS exam_online CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE exam_online;

DROP TABLE IF EXISTS answer_record;
DROP TABLE IF EXISTS wrong_question;
DROP TABLE IF EXISTS exam_record;
DROP TABLE IF EXISTS paper_question;
DROP TABLE IF EXISTS exam;
DROP TABLE IF EXISTS exam_paper;
DROP TABLE IF EXISTS question;
DROP TABLE IF EXISTS subject;
DROP TABLE IF EXISTS student;
DROP TABLE IF EXISTS teacher;
DROP TABLE IF EXISTS class;
DROP TABLE IF EXISTS department;
DROP TABLE IF EXISTS admin;
DROP TABLE IF EXISTS system_log;

CREATE TABLE admin (
    admin_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    real_name VARCHAR(50) NOT NULL,
    email VARCHAR(100),
    phone VARCHAR(20),
    status TINYINT DEFAULT 1,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    last_login_time DATETIME
);

CREATE TABLE department (
    department_id INT PRIMARY KEY AUTO_INCREMENT,
    department_name VARCHAR(100) NOT NULL,
    department_code VARCHAR(20) NOT NULL UNIQUE,
    description VARCHAR(255),
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE class (
    class_id INT PRIMARY KEY AUTO_INCREMENT,
    class_name VARCHAR(100) NOT NULL,
    class_code VARCHAR(20) NOT NULL UNIQUE,
    department_id INT,
    grade YEAR,
    student_count INT DEFAULT 0,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (department_id) REFERENCES department(department_id)
);

CREATE TABLE teacher (
    teacher_id INT PRIMARY KEY AUTO_INCREMENT,
    teacher_no VARCHAR(20) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    real_name VARCHAR(50) NOT NULL,
    gender TINYINT DEFAULT 0,
    department_id INT,
    email VARCHAR(100),
    phone VARCHAR(20),
    status TINYINT DEFAULT 1,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    last_login_time DATETIME,
    FOREIGN KEY (department_id) REFERENCES department(department_id)
);

CREATE TABLE student (
    student_id INT PRIMARY KEY AUTO_INCREMENT,
    student_no VARCHAR(20) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    real_name VARCHAR(50) NOT NULL,
    gender TINYINT DEFAULT 0,
    class_id INT,
    email VARCHAR(100),
    phone VARCHAR(20),
    status TINYINT DEFAULT 1,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    last_login_time DATETIME,
    FOREIGN KEY (class_id) REFERENCES class(class_id)
);

CREATE TABLE subject (
    subject_id INT PRIMARY KEY AUTO_INCREMENT,
    subject_name VARCHAR(100) NOT NULL,
    subject_code VARCHAR(20) NOT NULL UNIQUE,
    department_id INT,
    credit DECIMAL(3,1) DEFAULT 3.0,
    description VARCHAR(255),
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (department_id) REFERENCES department(department_id)
);

CREATE TABLE question (
    question_id INT PRIMARY KEY AUTO_INCREMENT,
    subject_id INT,
    question_type TINYINT NOT NULL,
    question_content TEXT NOT NULL,
    options TEXT,
    correct_answer TEXT NOT NULL,
    score DECIMAL(5,2) DEFAULT 2.00,
    difficulty TINYINT DEFAULT 2,
    knowledge_point VARCHAR(100),
    create_by INT,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (subject_id) REFERENCES subject(subject_id),
    FOREIGN KEY (create_by) REFERENCES teacher(teacher_id)
);

CREATE TABLE exam_paper (
    paper_id INT PRIMARY KEY AUTO_INCREMENT,
    paper_name VARCHAR(200) NOT NULL,
    subject_id INT,
    total_score DECIMAL(6,2) DEFAULT 100.00,
    pass_score DECIMAL(6,2) DEFAULT 60.00,
    duration INT DEFAULT 120,
    question_count INT DEFAULT 0,
    create_by INT,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    status TINYINT DEFAULT 1,
    FOREIGN KEY (subject_id) REFERENCES subject(subject_id),
    FOREIGN KEY (create_by) REFERENCES teacher(teacher_id)
);

CREATE TABLE paper_question (
    id INT PRIMARY KEY AUTO_INCREMENT,
    paper_id INT,
    question_id INT,
    question_order INT,
    score DECIMAL(5,2) DEFAULT 2.00,
    FOREIGN KEY (paper_id) REFERENCES exam_paper(paper_id),
    FOREIGN KEY (question_id) REFERENCES question(question_id)
);

CREATE TABLE exam (
    exam_id INT PRIMARY KEY AUTO_INCREMENT,
    exam_name VARCHAR(200) NOT NULL,
    paper_id INT,
    class_id INT,
    start_time DATETIME NOT NULL,
    end_time DATETIME NOT NULL,
    status TINYINT DEFAULT 0,
    allow_screen_switch TINYINT DEFAULT 3,
    create_by INT,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (paper_id) REFERENCES exam_paper(paper_id),
    FOREIGN KEY (class_id) REFERENCES class(class_id),
    FOREIGN KEY (create_by) REFERENCES teacher(teacher_id)
);

CREATE TABLE exam_record (
    record_id INT PRIMARY KEY AUTO_INCREMENT,
    exam_id INT,
    student_id INT,
    start_time DATETIME,
    submit_time DATETIME,
    total_score DECIMAL(6,2) DEFAULT 0.00,
    objective_score DECIMAL(6,2) DEFAULT 0.00,
    subjective_score DECIMAL(6,2) DEFAULT 0.00,
    status TINYINT DEFAULT 0,
    screen_switch_count INT DEFAULT 0,
    ip_address VARCHAR(50),
    FOREIGN KEY (exam_id) REFERENCES exam(exam_id),
    FOREIGN KEY (student_id) REFERENCES student(student_id)
);

CREATE TABLE answer_record (
    answer_id INT PRIMARY KEY AUTO_INCREMENT,
    record_id INT,
    question_id INT,
    student_answer TEXT,
    is_correct TINYINT,
    score DECIMAL(5,2) DEFAULT 0.00,
    answer_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (record_id) REFERENCES exam_record(record_id),
    FOREIGN KEY (question_id) REFERENCES question(question_id)
);

CREATE TABLE wrong_question (
    wrong_id INT PRIMARY KEY AUTO_INCREMENT,
    student_id INT,
    question_id INT,
    student_answer TEXT,
    correct_answer TEXT,
    wrong_count INT DEFAULT 1,
    last_wrong_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    subject_id INT,
    FOREIGN KEY (student_id) REFERENCES student(student_id),
    FOREIGN KEY (question_id) REFERENCES question(question_id),
    FOREIGN KEY (subject_id) REFERENCES subject(subject_id)
);

CREATE TABLE system_log (
    log_id INT PRIMARY KEY AUTO_INCREMENT,
    user_type TINYINT NOT NULL,
    user_id INT,
    operation VARCHAR(100) NOT NULL,
    operation_detail TEXT,
    ip_address VARCHAR(50),
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO admin (username, password, real_name, email, phone, status) VALUES
('admin', 'e10adc3949ba59abbe56e057f20f883e', '系统管理员', 'admin@school.edu.cn', '13800138000', 1),
('superadmin', 'e10adc3949ba59abbe56e057f20f883e', '超级管理员', 'super@school.edu.cn', '13800138001', 1),
('admin01', 'e10adc3949ba59abbe56e057f20f883e', '李明', 'liming@school.edu.cn', '13800138002', 1),
('admin02', 'e10adc3949ba59abbe56e057f20f883e', '王芳', 'wangfang@school.edu.cn', '13800138003', 1),
('admin03', 'e10adc3949ba59abbe56e057f20f883e', '张伟', 'zhangwei@school.edu.cn', '13800138004', 1),
('admin04', 'e10adc3949ba59abbe56e057f20f883e', '刘洋', 'liuyang@school.edu.cn', '13800138005', 1),
('admin05', 'e10adc3949ba59abbe56e057f20f883e', '陈静', 'chenjing@school.edu.cn', '13800138006', 1),
('admin06', 'e10adc3949ba59abbe56e057f20f883e', '赵强', 'zhaoqiang@school.edu.cn', '13800138007', 1),
('admin07', 'e10adc3949ba59abbe56e057f20f883e', '孙丽', 'sunli@school.edu.cn', '13800138008', 1),
('admin08', 'e10adc3949ba59abbe56e057f20f883e', '周涛', 'zhoutao@school.edu.cn', '13800138009', 1),
('admin09', 'e10adc3949ba59abbe56e057f20f883e', '吴磊', 'wulei@school.edu.cn', '13800138010', 1);

INSERT INTO department (department_name, department_code, description) VALUES
('计算机科学与技术学院', 'CS', '计算机科学与技术、软件工程、网络工程'),
('数学与统计学院', 'MATH', '数学与应用数学、统计学、信息与计算科学'),
('外国语学院', 'FL', '英语、日语、法语、德语'),
('经济管理学院', 'ECON', '经济学、金融学、工商管理、会计学'),
('电子信息学院', 'EE', '电子信息工程、通信工程、自动化'),
('机械工程学院', 'ME', '机械设计制造及其自动化、车辆工程'),
('土木工程学院', 'CE', '土木工程、建筑学、工程管理'),
('文学院', 'LIT', '汉语言文学、新闻学、广告学'),
('法学院', 'LAW', '法学、知识产权'),
('医学院', 'MED', '临床医学、护理学、药学'),
('理学院', 'SCI', '物理学、化学、生物学');

INSERT INTO class (class_name, class_code, department_id, grade, student_count) VALUES
('计算机科学与技术2021级1班', 'CS2101', 1, 2021, 45),
('软件工程2021级1班', 'SE2101', 1, 2021, 42),
('计算机科学与技术2022级1班', 'CS2201', 1, 2022, 48),
('软件工程2022级1班', 'SE2201', 1, 2022, 40),
('数学与应用数学2021级1班', 'MA2101', 2, 2021, 38),
('统计学2021级1班', 'ST2101', 2, 2021, 35),
('英语2021级1班', 'EN2101', 3, 2021, 50),
('日语2021级1班', 'JP2101', 3, 2021, 32),
('经济学2021级1班', 'EC2101', 4, 2021, 55),
('金融学2021级1班', 'FI2101', 4, 2021, 48),
('电子信息工程2021级1班', 'EE2101', 5, 2021, 46),
('通信工程2021级1班', 'CE2101', 5, 2021, 43);

INSERT INTO teacher (teacher_no, password, real_name, gender, department_id, email, phone) VALUES
('T2021001', 'e10adc3949ba59abbe56e057f20f883e', '张教授', 1, 1, 'zhang@school.edu.cn', '13900139001'),
('T2021002', 'e10adc3949ba59abbe56e057f20f883e', '李副教授', 2, 1, 'li@school.edu.cn', '13900139002'),
('T2021003', 'e10adc3949ba59abbe56e057f20f883e', '王讲师', 1, 1, 'wang@school.edu.cn', '13900139003'),
('T2021004', 'e10adc3949ba59abbe56e057f20f883e', '刘教授', 1, 2, 'liu@school.edu.cn', '13900139004'),
('T2021005', 'e10adc3949ba59abbe56e057f20f883e', '陈副教授', 2, 2, 'chen@school.edu.cn', '13900139005'),
('T2021006', 'e10adc3949ba59abbe56e057f20f883e', '杨教授', 2, 3, 'yang@school.edu.cn', '13900139006'),
('T2021007', 'e10adc3949ba59abbe56e057f20f883e', '赵副教授', 1, 4, 'zhao@school.edu.cn', '13900139007'),
('T2021008', 'e10adc3949ba59abbe56e057f20f883e', '黄讲师', 2, 5, 'huang@school.edu.cn', '13900139008'),
('T2021009', 'e10adc3949ba59abbe56e057f20f883e', '周教授', 1, 6, 'zhou@school.edu.cn', '13900139009'),
('T2021010', 'e10adc3949ba59abbe56e057f20f883e', '吴副教授', 2, 7, 'wu@school.edu.cn', '13900139100'),
('T2021011', 'e10adc3949ba59abbe56e057f20f883e', '郑讲师', 1, 1, 'zheng@school.edu.cn', '13900139101');

INSERT INTO student (student_no, password, real_name, gender, class_id, email, phone) VALUES
('2021001001', 'e10adc3949ba59abbe56e057f20f883e', '张三', 1, 1, 'zhangsan@student.school.edu.cn', '18800188001'),
('2021001002', 'e10adc3949ba59abbe56e057f20f883e', '李四', 2, 1, 'lisi@student.school.edu.cn', '18800188002'),
('2021001003', 'e10adc3949ba59abbe56e057f20f883e', '王五', 1, 1, 'wangwu@student.school.edu.cn', '18800188003'),
('2021001004', 'e10adc3949ba59abbe56e057f20f883e', '赵六', 1, 1, 'zhaoliu@student.school.edu.cn', '18800188004'),
('2021001005', 'e10adc3949ba59abbe56e057f20f883e', '钱七', 2, 1, 'qianqi@student.school.edu.cn', '18800188005'),
('2021001006', 'e10adc3949ba59abbe56e057f20f883e', '孙八', 1, 1, 'sunba@student.school.edu.cn', '18800188006'),
('2021001007', 'e10adc3949ba59abbe56e057f20f883e', '周九', 2, 1, 'zhoujiu@student.school.edu.cn', '18800188007'),
('2021001008', 'e10adc3949ba59abbe56e057f20f883e', '吴十', 1, 1, 'wushi@student.school.edu.cn', '18800188008'),
('2021001009', 'e10adc3949ba59abbe56e057f20f883e', '郑一', 1, 1, 'zhengyi@student.school.edu.cn', '18800188009'),
('2021001010', 'e10adc3949ba59abbe56e057f20f883e', '冯二', 2, 1, 'fenger@student.school.edu.cn', '18800188010'),
('2021001011', 'e10adc3949ba59abbe56e057f20f883e', '陈三', 1, 1, 'chensan@student.school.edu.cn', '18800188011'),
('2021002001', 'e10adc3949ba59abbe56e057f20f883e', '林四', 2, 2, 'linsi@student.school.edu.cn', '18800188012');

INSERT INTO subject (subject_name, subject_code, department_id, credit, description) VALUES
('数据结构与算法', 'CS101', 1, 4.0, '计算机专业核心基础课程'),
('操作系统', 'CS102', 1, 4.0, '计算机系统核心课程'),
('计算机网络', 'CS103', 1, 3.5, '网络通信原理与技术'),
('数据库原理', 'CS104', 1, 4.0, '数据库设计与应用'),
('高等数学', 'MATH101', 2, 5.0, '工科数学基础'),
('线性代数', 'MATH102', 2, 3.0, '矩阵理论与应用'),
('概率论与数理统计', 'MATH103', 2, 3.0, '随机数学基础'),
('大学英语', 'ENG101', 3, 4.0, '英语综合能力培养'),
('微观经济学', 'ECO101', 4, 3.0, '经济学基础理论'),
('宏观经济学', 'ECO102', 4, 3.0, '宏观经济分析'),
('电路原理', 'EE101', 5, 4.0, '电子电路基础'),
('信号与系统', 'EE102', 5, 4.0, '信号处理基础');

INSERT INTO question (subject_id, question_type, question_content, options, correct_answer, score, difficulty, knowledge_point, create_by) VALUES
(1, 1, '栈和队列的共同特点是？', 'A.都是先进先出\nB.都是先进后出\nC.只允许在端点处插入和删除元素\nD.没有共同点', 'C', 2.00, 2, '栈与队列', 1),
(1, 1, '一个栈的入栈序列是a,b,c,d,e，则栈的不可能的输出序列是？', 'A.edcba\nB.decba\nC.dceab\nD.abcde', 'C', 2.00, 3, '栈的特性', 1),
(1, 1, '深度为5的二叉树至多有____个结点。', 'A.16\nB.32\nC.31\nD.10', 'C', 2.00, 2, '二叉树性质', 1),
(1, 1, '链表不具有的特点是？', 'A.插入、删除不需要移动元素\nB.可随机访问任一元素\nC.不必事先估计存储空间\nD.所需空间与线性长度成正比', 'B', 2.00, 1, '链表特性', 1),
(1, 1, '冒泡排序在最坏情况下的时间复杂度为？', 'A.O(1)\nB.O(logn)\nC.O(n)\nD.O(n²)', 'D', 2.00, 2, '排序算法', 2),
(1, 2, '下列属于线性数据结构的有？', 'A.栈\nB.队列\nC.树\nD.图', 'A|B', 3.00, 2, '数据结构分类', 1),
(1, 2, '二叉树的遍历方式包括？', 'A.前序遍历\nB.中序遍历\nC.后序遍历\nD.层次遍历', 'A|B|C|D', 3.00, 1, '二叉树遍历', 1),
(1, 3, '快速排序是一种稳定的排序算法。', NULL, '0', 1.00, 2, '排序算法', 2),
(1, 3, '哈希表的查找效率与装填因子无关。', NULL, '0', 1.00, 3, '哈希表', 2),
(1, 3, '二叉搜索树的查找效率在最坏情况下为O(n)。', NULL, '1', 1.00, 2, '二叉搜索树', 1),
(1, 4, '一棵二叉树的度为2的结点有3个，度为1的结点有2个，则叶子结点有____个。', NULL, '4', 2.00, 3, '二叉树性质', 1),
(1, 5, '简述堆排序的基本思想。', NULL, '堆排序是将待排序序列构造成一个大顶堆或小顶堆，此时整个序列的最大值或最小值就是堆顶的根节点。将其与末尾元素交换，然后将剩余n-1个元素重新构造成一个堆，重复执行直至得到一个有序序列。', 10.00, 3, '排序算法', 1),
(2, 1, '进程和程序的本质区别是？', 'A.前者存储在内存，后者在外存\nB.前者是动态的，后者是静态的\nC.前者分时使用CPU，后者独占CPU\nD.前者在一个文件中，后者在多个文件中', 'B', 2.00, 2, '进程概念', 2),
(2, 1, '死锁产生的四个必要条件是互斥、请求与保持、不剥夺和？', 'A.循环等待\nB.请求与释放\nC.资源互斥\nD.资源独占', 'A', 2.00, 2, '死锁', 2),
(2, 1, '虚拟存储技术的基本思想是？', 'A.扩充内存物理空间\nB.扩充外存空间\nC.用外存空间换取内存空间\nD.用缓存空间换取内存空间', 'C', 2.00, 3, '虚拟存储', 2),
(2, 1, '分页系统中，地址转换工作是由____完成的。', 'A.硬件\nB.操作系统\nC.用户程序\nD.装入程序', 'A', 2.00, 2, '存储管理', 2);

INSERT INTO exam_paper (paper_name, subject_id, total_score, pass_score, duration, question_count, create_by, status) VALUES
('数据结构与算法期中考试A卷', 1, 100.00, 60.00, 120, 20, 1, 1),
('数据结构与算法期末考试A卷', 1, 100.00, 60.00, 150, 25, 1, 1),
('操作系统期中考试A卷', 2, 100.00, 60.00, 120, 20, 2, 1),
('计算机网络期末考试A卷', 3, 100.00, 60.00, 120, 25, 3, 1),
('数据库原理期末考试A卷', 4, 100.00, 60.00, 120, 25, 2, 1),
('高等数学期中考试A卷', 5, 100.00, 60.00, 150, 20, 4, 1),
('线性代数期末考试A卷', 6, 100.00, 60.00, 120, 20, 5, 1),
('概率论期末考试A卷', 7, 100.00, 60.00, 120, 20, 4, 1),
('大学英语期中考试A卷', 8, 100.00, 60.00, 120, 30, 6, 1),
('微观经济学期末考试A卷', 9, 100.00, 60.00, 120, 25, 7, 1),
('电路原理期中考试A卷', 11, 100.00, 60.00, 120, 20, 8, 1);

INSERT INTO paper_question (paper_id, question_id, question_order, score) VALUES
(1, 1, 1, 2.00),
(1, 2, 2, 2.00),
(1, 3, 3, 2.00),
(1, 4, 4, 2.00),
(1, 5, 5, 2.00),
(1, 6, 6, 3.00),
(1, 7, 7, 3.00),
(1, 8, 8, 1.00),
(1, 9, 9, 1.00),
(1, 10, 10, 1.00),
(1, 11, 11, 2.00),
(1, 12, 12, 10.00),
(2, 1, 1, 2.00),
(2, 2, 2, 2.00),
(2, 3, 3, 2.00),
(2, 4, 4, 2.00),
(2, 5, 5, 2.00),
(3, 13, 1, 2.00),
(3, 14, 2, 2.00),
(3, 15, 3, 2.00),
(3, 16, 4, 2.00);

INSERT INTO exam (exam_name, paper_id, class_id, start_time, end_time, status, allow_screen_switch, create_by) VALUES
('2024年春季数据结构与算法期中考试', 1, 1, '2024-04-15 14:00:00', '2024-04-15 16:00:00', 2, 3, 1),
('2024年春季数据结构与算法期末考试', 2, 1, '2024-06-20 09:00:00', '2024-06-20 11:30:00', 1, 3, 1),
('2024年春季操作系统期中考试', 3, 1, '2024-04-20 14:00:00', '2024-04-20 16:00:00', 2, 3, 2),
('2024年春季计算机网络期末考试', 4, 2, '2024-06-25 09:00:00', '2024-06-25 11:00:00', 1, 3, 3),
('2024年春季数据库原理期末考试', 5, 2, '2024-06-22 14:00:00', '2024-06-22 16:00:00', 1, 3, 2),
('2024年春季高等数学期中考试', 6, 5, '2024-04-18 09:00:00', '2024-04-18 11:30:00', 2, 3, 4),
('2024年春季线性代数期末考试', 7, 6, '2024-06-18 14:00:00', '2024-06-18 16:00:00', 1, 3, 5),
('2024年春季概率论期末考试', 8, 6, '2024-06-19 09:00:00', '2024-06-19 11:00:00', 1, 3, 4),
('2024年春季大学英语期中考试', 9, 7, '2024-04-22 14:00:00', '2024-04-22 16:00:00', 2, 3, 6),
('2024年春季微观经济学期末考试', 10, 9, '2024-06-21 09:00:00', '2024-06-21 11:00:00', 1, 3, 7),
('2024年春季电路原理期中考试', 11, 11, '2024-04-25 14:00:00', '2024-04-25 16:00:00', 1, 3, 8);

INSERT INTO exam_record (exam_id, student_id, start_time, submit_time, total_score, objective_score, subjective_score, status, screen_switch_count) VALUES
(1, 1, '2024-04-15 14:05:00', '2024-04-15 15:50:00', 88.50, 68.50, 20.00, 2, 1),
(1, 2, '2024-04-15 14:03:00', '2024-04-15 15:45:00', 92.00, 72.00, 20.00, 2, 0),
(1, 3, '2024-04-15 14:01:00', '2024-04-15 15:30:00', 76.50, 56.50, 20.00, 2, 2),
(1, 4, '2024-04-15 14:10:00', '2024-04-15 15:55:00', 85.00, 65.00, 20.00, 2, 1),
(1, 5, '2024-04-15 14:02:00', '2024-04-15 16:00:00', 90.50, 70.50, 20.00, 2, 0),
(1, 6, '2024-04-15 14:08:00', '2024-04-15 15:40:00', 72.00, 52.00, 20.00, 2, 3),
(1, 7, '2024-04-15 14:06:00', '2024-04-15 15:52:00', 95.00, 75.00, 20.00, 2, 0),
(1, 8, '2024-04-15 14:04:00', '2024-04-15 15:48:00', 81.00, 61.00, 20.00, 2, 1),
(1, 9, '2024-04-15 14:12:00', '2024-04-15 15:35:00', 78.50, 58.50, 20.00, 2, 2),
(1, 10, '2024-04-15 14:00:00', '2024-04-15 15:55:00', 86.00, 66.00, 20.00, 2, 1),
(1, 11, '2024-04-15 14:07:00', '2024-04-15 15:58:00', 89.00, 69.00, 20.00, 2, 0),
(6, 1, '2024-04-18 09:05:00', '2024-04-18 11:20:00', 82.00, 82.00, 0.00, 2, 0);

INSERT INTO answer_record (record_id, question_id, student_answer, is_correct, score) VALUES
(1, 1, 'C', 1, 2.00),
(1, 2, 'C', 1, 2.00),
(1, 3, 'C', 1, 2.00),
(1, 4, 'A', 0, 0.00),
(1, 5, 'D', 1, 2.00),
(1, 6, 'A|B', 1, 3.00),
(1, 7, 'A|B|C', 0, 0.00),
(1, 8, '0', 1, 1.00),
(1, 9, '1', 0, 0.00),
(1, 10, '1', 1, 1.00),
(1, 11, '4', 1, 2.00),
(1, 12, '堆排序是将待排序元素构建成堆结构，然后进行排序。', 1, 20.00),
(2, 1, 'C', 1, 2.00),
(2, 2, 'C', 1, 2.00),
(2, 3, 'C', 1, 2.00),
(2, 4, 'B', 1, 2.00),
(2, 5, 'D', 1, 2.00),
(3, 1, 'A', 0, 0.00),
(3, 2, 'C', 1, 2.00),
(3, 3, 'A', 0, 0.00),
(3, 4, 'B', 1, 2.00);

INSERT INTO wrong_question (student_id, question_id, student_answer, correct_answer, wrong_count, last_wrong_time, subject_id) VALUES
(1, 4, 'A', 'B', 1, '2024-04-15 15:50:00', 1),
(1, 7, 'A|B|C', 'A|B|C|D', 1, '2024-04-15 15:50:00', 1),
(1, 9, '1', '0', 1, '2024-04-15 15:50:00', 1),
(3, 4, 'C', 'B', 1, '2024-04-15 15:30:00', 1),
(3, 7, 'A|B|D', 'A|B|C|D', 1, '2024-04-15 15:30:00', 1),
(3, 9, '1', '0', 1, '2024-04-15 15:30:00', 1),
(4, 5, 'C', 'D', 1, '2024-04-15 15:55:00', 1),
(4, 8, '1', '0', 1, '2024-04-15 15:55:00', 1),
(6, 1, 'A', 'C', 1, '2024-04-15 15:40:00', 1),
(6, 3, 'A', 'C', 1, '2024-04-15 15:40:00', 1),
(6, 5, 'C', 'D', 1, '2024-04-15 15:40:00', 1),
(8, 2, 'B', 'C', 1, '2024-04-15 15:48:00', 1);

INSERT INTO system_log (user_type, user_id, operation, operation_detail, ip_address) VALUES
(1, 1, '登录系统', '管理员admin登录成功', '192.168.1.100'),
(2, 1, '登录系统', '教师张教授登录成功', '192.168.1.101'),
(3, 1, '登录系统', '学生张三登录成功', '192.168.1.102'),
(2, 1, '创建试卷', '创建了试卷: 数据结构与算法期中考试A卷', '192.168.1.101'),
(2, 1, '发布考试', '发布了考试: 2024年春季数据结构与算法期中考试', '192.168.1.101'),
(3, 1, '参加考试', '参加了考试: 2024年春季数据结构与算法期中考试', '192.168.1.102'),
(3, 2, '参加考试', '参加了考试: 2024年春季数据结构与算法期中考试', '192.168.1.103'),
(3, 3, '参加考试', '参加了考试: 2024年春季数据结构与算法期中考试', '192.168.1.104'),
(3, 1, '提交试卷', '提交了试卷: 2024年春季数据结构与算法期中考试', '192.168.1.102'),
(2, 1, '阅卷完成', '完成了考试阅卷: 2024年春季数据结构与算法期中考试', '192.168.1.101'),
(1, 1, '查看统计', '查看了系统统计数据', '192.168.1.100');
