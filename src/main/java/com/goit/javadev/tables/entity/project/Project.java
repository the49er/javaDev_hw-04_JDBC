package com.goit.javadev.tables.entity.project;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
class Project {
    long id;
    String name;
    String description;
    LocalDate date;
    int customerId;
    int companyId;
}
