DROP SCHEMA IF EXISTS `homework_4`;
CREATE SCHEMA IF NOT EXISTS `homework_4`;
USE homework_4;
CREATE USER IF NOT EXISTS 'test_user'@'localhost' IDENTIFIED BY 'password';
GRANT ALL PRIVILEGES ON homework_4. * TO 'test_user'@'localhost';
FLUSH PRIVILEGES;
