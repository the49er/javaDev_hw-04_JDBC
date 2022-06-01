USE homework_4;
ALTER TABLE developers
ADD CONSTRAINT employer_company_id_fk
FOREIGN KEY (company_id)
REFERENCES companies(id)
ON DELETE SET NULL;


