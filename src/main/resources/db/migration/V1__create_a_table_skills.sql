USE homework_4;
CREATE TABLE skills (
id BIGINT NOT NULL AUTO_INCREMENT,
programming_lang VARCHAR(50) NOT NULL,
level VARCHAR(50) NOT NULL,
CONSTRAINT level_enum_values
CHECK (level IN ('Junior', 'Middle', 'Senior')),
CONSTRAINT lang_enum_values
CHECK (programming_lang IN ('C#', 'Java', 'JavaScript', 'C++')),
PRIMARY KEY(id)
);



