use homework_4;
select * from developers;
select * from companies;
SELECT max(id) AS maxId FROM companies;
SELECT id, name, age, gender, salary, company_id FROM `homework_4`.developers WHERE name LIKE '%i%';
SELECT id, name, age, gender, salary, company_id FROM `homework_4`.developers WHERE id = 1;
SELECT count(id) as id from `homework_4`.developers;
delete from `homework_4`.developers where id = 1;
delete from `homework_4`.companies where id = 1;
INSERT INTO `homework_4`.skills (programming_lang, level) VALUES ('C++', 'Junior');
select * from `homework_4`.skills;
select * from `homework_4`.projects;
select * from `homework_4`.customers;
select * from `homework_4`.company_customer;
