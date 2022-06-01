USE homework_4;
ALTER TABLE projects
ADD CONSTRAINT customer_id_fk
FOREIGN KEY (customer_id)
REFERENCES customers(id)
ON DELETE SET NULL;


