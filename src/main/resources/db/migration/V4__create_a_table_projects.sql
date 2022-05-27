USE homework_4;
CREATE TABLE projects (
id BIGINT NOT NULL AUTO_INCREMENT,
name VARCHAR(100),
description VARCHAR(150),
customer_id BIGINT NOT NULL,
company_id BIGINT NOT NULL,
PRIMARY KEY(id)
);


