package com.goit.javadev.tables.entity.skill;


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
    private long id;
    private ProgramLang programLang;
    private SkillLevel skillLevel;
}
