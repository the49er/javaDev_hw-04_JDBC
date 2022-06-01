USE homework_4;
ALTER TABLE projects
ADD CONSTRAINT company_id_fk
FOREIGN KEY (company_id)
REFERENCES companies(id)
ON DELETE SET NULL;


