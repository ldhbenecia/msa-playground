-- user-service DB
CREATE DATABASE IF NOT EXISTS user_db;
GRANT ALL PRIVILEGES ON user_db.* TO 'root'@'%';

-- order-service DB
CREATE DATABASE IF NOT EXISTS order_db;
GRANT ALL PRIVILEGES ON order_db.* TO 'root'@'%';

FLUSH PRIVILEGES;