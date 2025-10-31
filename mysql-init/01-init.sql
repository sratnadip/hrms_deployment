-- Create database if not exists
CREATE DATABASE IF NOT EXISTS hrmsportal;
USE hrmsportal;

-- Grant privileges
GRANT ALL PRIVILEGES ON hrmsportal.* TO 'root'@'%';
FLUSH PRIVILEGES;

-- Basic configuration
SET GLOBAL sql_mode = 'STRICT_TRANS_TABLES,NO_ZERO_DATE,NO_ZERO_IN_DATE,ERROR_FOR_DIVISION_BY_ZERO';
