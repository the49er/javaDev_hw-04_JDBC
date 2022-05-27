USE homework_4;
CREATE TABLE developer_project (
developer_id BIGINT NOT NULL,
project_id BIGINT NOT NULL,
PRIMARY KEY (developer_id, project_id),
FOREIGN KEY (developer_id) REFERENCES developers(id),
FOREIGN KEY (project_id) REFERENCES projects(id)
);


