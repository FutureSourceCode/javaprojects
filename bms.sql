CREATE DATABASE bookms CHARACTER SET utf8mb;

CREATE TABLE `announcement` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '公告ID',
  `content` text NOT NULL COMMENT '发布内容',
  `publisher` varchar(50) NOT NULL COMMENT '发布者（用户名）',
  `publish_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '发布时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


INSERT INTO `announcement` VALUES (1,'图书馆将于2025年12月1日闭馆维护一天','admin','2025-11-24 11:24:07'),(2,'新书《Java编程思想》已上架，欢迎借阅','librarian','2025-11-24 11:24:07'),(3,'预约图书超过3天未取将自动取消','admin','2025-11-24 11:24:07'),(4,'我饿了','wn','2025-11-24 13:35:17'),(6,'您预约的《java编程思想》已到库，请尽快到图书馆办理借阅手续！（预约人：wn）','wn','2025-11-24 14:27:56'),(7,'您预约的《Java编程思想》已到库，请尽快到图书馆办理借阅手续！（预约人：小凯）','wn','2025-11-24 14:30:19'),(9,'【还书提醒】您借阅的《Spring实战》已到归还日期，请尽快到图书馆办理归还手续！（借阅者：wdk）','wn','2025-11-24 15:12:44'),(10,'您预约的《Java编程思想》已到库，请尽快到图书馆办理借阅手续！（预约人：wdk）','wn','2025-11-24 15:36:25');

CREATE TABLE `book` (
  `id` int NOT NULL AUTO_INCREMENT,
  `book_no` varchar(50) NOT NULL COMMENT '图书编号',
  `book_name` varchar(100) NOT NULL COMMENT '图书名字',
  `author` varchar(50) NOT NULL COMMENT '图书作者',
  `stock` int NOT NULL DEFAULT '0' COMMENT '库存',
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `book` VALUES (1,'B001','Java编程思想','Bruce Eckel',98),(2,'B002','Spring实战','Craig Walls',5),(3,'B003','凯歌','kaige',0);

CREATE TABLE `borrow` (
  `id` int NOT NULL AUTO_INCREMENT,
  `book_no` varchar(50) NOT NULL,
  `user_id` int NOT NULL,
  `borrow_data` datetime DEFAULT CURRENT_TIMESTAMP,
  `return_data` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  KEY `book_no` (`book_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `reservation` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '预约记录ID（主键）',
  `book_name` varchar(100) NOT NULL COMMENT '预约图书名称',
  `username` varchar(50) NOT NULL COMMENT '预约者姓名',
  `reserve_time` datetime NOT NULL COMMENT '预约时间',
  `status` varchar(50) DEFAULT 'pending' COMMENT '预约状态',
  PRIMARY KEY (`id`),
  UNIQUE KEY `reserve_time` (`reserve_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `reservation` VALUES (14,'Java编程思想','wdk','2025-11-24 09:24:23','fulfilled'),(15,'java编程思想','wn','2025-11-24 10:29:28','fulfilled'),(16,'Java编程思想','小凯','2025-11-24 14:29:35','fulfilled');

CREATE TABLE `user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL,
  `role` varchar(20) NOT NULL COMMENT 'admin/user',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


