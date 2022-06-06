package com.goit.javadev;

import com.goit.javadev.feature.storage.Storage;
import com.goit.javadev.tables.entity.company.CompanyDaoService;
import com.goit.javadev.tables.entity.customer.CustomerDaoService;
import com.goit.javadev.tables.entity.developer.Developer;
import com.goit.javadev.tables.entity.developer.DeveloperDaoService;
import com.goit.javadev.tables.entity.project.Project;
import com.goit.javadev.tables.entity.project.ProjectDaoService;
import com.goit.javadev.tables.entity.skill.ProgramLang;
import com.goit.javadev.tables.entity.skill.Skill;
import com.goit.javadev.tables.entity.skill.SkillDaoService;
import com.goit.javadev.tables.entity.skill.SkillLevel;

import java.sql.Connection;
import java.time.LocalDate;

public class DbDaoTest {

    public static void main(String[] args) {
        Storage storage = Storage.getInstance();
        Connection connection = storage.getConnection();

        DeveloperDaoService developerDaoService = new DeveloperDaoService(connection);
        CompanyDaoService companyDaoService = new CompanyDaoService(connection);
        CustomerDaoService customerDaoService = new CustomerDaoService(connection);
        ProjectDaoService projectDaoService = new ProjectDaoService(connection);
        SkillDaoService skillDaoService = new SkillDaoService(connection);


        developerDaoService.insertNewEntity(new Developer(1, "Test1", 30, Developer.Gender.male, 3000, 1));
        developerDaoService.getEntityById(1);
        developerDaoService.deleteById(1);
        developerDaoService.updateEntityFieldsById(new Developer(1, "UpdateTest", 50, Developer.Gender.other, 5000, 2), 2);

        projectDaoService.insertNewEntity(new Project(1, "TestNameProject", "TestDescriptionOfProject1", LocalDate.now(), 1, 2));
        projectDaoService.getEntityById(1);
        projectDaoService.deleteById(1);
        projectDaoService.updateEntityFieldsById(new Project(2, "TestNameProject2", "TestDescriptionOfProject2", LocalDate.now().plusMonths(1), 3, 2), 2);

        skillDaoService.insertNewEntity(new Skill(1, ProgramLang.java, SkillLevel.senior));
        skillDaoService.getEntityById(1);
        skillDaoService.deleteById(1);
        skillDaoService.updateEntityFieldsById(new Skill(1, ProgramLang.javaScript, SkillLevel.middle), 2);

    }
}
