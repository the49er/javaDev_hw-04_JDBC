package com.goit.javadev.tables.entity.developer;

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
    private long id;
    private String name;
    private int age;
    private Gender gender;
    private int salary;
    private int companyId;

    public enum Gender {
        male,
        female,
        other
    }
}
