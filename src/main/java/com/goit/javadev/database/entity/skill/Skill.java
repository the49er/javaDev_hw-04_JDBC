package com.goit.javadev.database.entity.skill;


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
public class Skill {
    long id;
    ProgramLang programLang;
    SkillLevel skillLevel;
}
