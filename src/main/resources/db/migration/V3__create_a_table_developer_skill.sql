USE homework_4;
CREATE TABLE developer_skill(
developer_id BIGINT NULL,
skill_id BIGINT NULL,
UNIQUE (developer_id, skill_id),
FOREIGN KEY (developer_id) REFERENCES developers(id),
FOREIGN KEY (skill_id) REFERENCES skills(id)
ON DELETE SET NULL
);


