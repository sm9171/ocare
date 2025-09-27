-- 데이터베이스 초기화 스크립트
-- UTF8MB4 문자셋으로 데이터베이스 생성

CREATE DATABASE IF NOT EXISTS ocare_db 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

USE ocare_db;

-- 사용자 권한 설정
GRANT ALL PRIVILEGES ON ocare_db.* TO 'ocare_user'@'%';
FLUSH PRIVILEGES;

-- 기본 테이블 생성 (JPA가 자동 생성하므로 필요시에만 사용)
-- CREATE TABLE IF NOT EXISTS users (
--     id BIGINT AUTO_INCREMENT PRIMARY KEY,
--     record_key VARCHAR(255) UNIQUE NOT NULL,
--     username VARCHAR(50) NOT NULL,
--     email VARCHAR(100) UNIQUE NOT NULL,
--     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
--     updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
-- );

-- 인덱스 최적화를 위한 설정
SET GLOBAL innodb_buffer_pool_size = 512 * 1024 * 1024;
SET GLOBAL max_connections = 200;