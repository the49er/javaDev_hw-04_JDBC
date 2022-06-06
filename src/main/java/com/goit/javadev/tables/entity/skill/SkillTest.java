package com.goit.javadev.tables.entity.skill;

import com.goit.javadev.feature.storage.Storage;

import java.sql.Connection;
import java.util.ArrayList;

import java.util.Arrays;
import java.util.List;

public class SkillTest {
    public static void main(String[] args) {
        List<Skill> skillList = new ArrayList<>();
        Storage storage = Storage.getInstance();
        Connection connection = storage.getConnection();

        System.out.println(ProgramLang.valueOf("java"));

        System.out.println(SkillLevel.junior.getLevel());
        String str = "juNior";
        String str2 = "Seneer";
        System.out.println(ProgramLang.getFromString(str2));
        System.out.println(Arrays.toString(SkillLevel.values()));

        System.out.println(SkillLevel.getMsg());

//        String jsonFileCompOut = "files/out/skills.json";
//        Gson gson = new Gson().newBuilder().setPrettyPrinting().create();
//
//        skillList.add(new Skill(1, ProgramLang.java, SkillLevel.junior));
//        skillList.add(new Skill(2, ProgramLang.java, SkillLevel.middle));
//        skillList.add(new Skill(3, ProgramLang.java, SkillLevel.senior));
//        skillList.add(new Skill(4, ProgramLang.cPlus, SkillLevel.senior));
//        skillList.add(new Skill(5, ProgramLang.cPlus, SkillLevel.middle));
//        skillList.add(new Skill(6, ProgramLang.cPlus, SkillLevel.junior));
//        skillList.add(new Skill(7, ProgramLang.cSharp, SkillLevel.junior));
//        skillList.add(new Skill(8, ProgramLang.cSharp, SkillLevel.senior));
//        skillList.add(new Skill(9, ProgramLang.cSharp, SkillLevel.middle));
//        skillList.add(new Skill(10, ProgramLang.javaScript, SkillLevel.junior));
//        skillList.add(new Skill(11, ProgramLang.javaScript, SkillLevel.senior));
//        skillList.add(new Skill(12, ProgramLang.javaScript, SkillLevel.middle));
//
//        String outputString = gson.toJson(skillList);
//        try(FileWriter fileWriter = new FileWriter(jsonFileCompOut)) {
//            fileWriter.write(outputString);
//        } catch (IOException e) {
//            e.printStackTrace();
//       }
    }
}
