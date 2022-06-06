USE homework_4;
CREATE TABLE developer_project (
developer_id BIGINT NULL,
project_id BIGINT NULL,
UNIQUE (developer_id, project_id),
FOREIGN KEY (developer_id) REFERENCES developers(id)
ON DELETE SET NULL,
FOREIGN KEY (project_id) REFERENCES projects(id)
ON DELETE SET NULL
);


