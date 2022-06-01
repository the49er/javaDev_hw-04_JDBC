USE homework_4;
CREATE TABLE company_customer (
company_id BIGINT NULL,
customer_id BIGINT NULL,
UNIQUE (company_id, customer_id),
FOREIGN KEY (company_id) REFERENCES companies(id),
FOREIGN KEY (customer_id) REFERENCES customers(id)
ON DELETE SET NULL
);

