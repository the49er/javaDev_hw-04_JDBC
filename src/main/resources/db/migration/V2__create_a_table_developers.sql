USE homework_4;
CREATE TABLE developers (
id BIGINT NOT NULL AUTO_INCREMENT,
name VARCHAR(100),
age INT,
gender VARCHAR(100),
salary INT,
company_id BIGINT NOT NULL,
CONSTRAINT gender_enum_values
CHECK (gender IN ('male', 'female', 'other')),
PRIMARY KEY(id)
);



