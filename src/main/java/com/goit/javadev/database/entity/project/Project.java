package com.goit.javadev.database.entity.project;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Project {
    private long id;
    private String name;
    private String description;
    private LocalDate date;
    private int customerId;
    private int companyId;

    LocalDate getDate(String str){
        return LocalDate.parse(str.format(String.valueOf(DateTimeFormatter.ofPattern("yyyy-mm-dd"))));
    }
}
