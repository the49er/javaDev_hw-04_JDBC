USE homework_4;
CREATE TABLE company_customer (
company_id BIGINT NOT NULL,
customer_id BIGINT NOT NULL,
PRIMARY KEY (company_id, customer_id),
FOREIGN KEY (company_id) REFERENCES companies(id),
FOREIGN KEY (customer_id) REFERENCES customers(id)
);


