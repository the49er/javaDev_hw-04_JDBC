package com.goit.javadev.database.entity.developer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Developer {
    long id;
    String name;
    int age;
    Gender gender;
    int salary;
    int companyId;

    public enum Gender {
        MALE,
        FEMALE,
        OTHER
    }
}
