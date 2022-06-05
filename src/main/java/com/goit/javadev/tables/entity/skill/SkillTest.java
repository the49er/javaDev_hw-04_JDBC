package com.goit.javadev.tables.entity.skill;

import com.goit.javadev.feature.storage.Storage;

import java.sql.Connection;
import java.util.ArrayList;

import java.util.List;

public class SkillTest {
    public static void main(String[] args) {
        List<Skill> skillList = new ArrayList<>();
        Storage storage = Storage.getInstance();
        Connection connection = storage.getConnection();

        skillList.add(new Skill(1, ProgramLang.java, SkillLevel.junior));
        skillList.add(new Skill(2, ProgramLang.java, SkillLevel.middle));
        skillList.add(new Skill(3, ProgramLang.java, SkillLevel.senior));
        skillList.add(new Skill(4, ProgramLang.cPlus, SkillLevel.senior));
        skillList.add(new Skill(5, ProgramLang.cPlus, SkillLevel.middle));
        skillList.add(new Skill(6, ProgramLang.cPlus, SkillLevel.junior));
        SkillDaoService skillDaoService = new SkillDaoService(connection);
        skillDaoService.clearTable();
        skillDaoService.insertNewEntities(skillList);
    }
}
