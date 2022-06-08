package homework_4;

import com.goit.javadev.database.feature.storage.Storage;
import com.goit.javadev.database.entity.developer.Developer;
import com.goit.javadev.database.entity_services.DeveloperDaoService;
import com.goit.javadev.database.entity.project.Project;
import com.goit.javadev.database.entity_services.ProjectDaoService;
import com.goit.javadev.database.entity.skill.ProgramLang;
import com.goit.javadev.database.entity.skill.Skill;
import com.goit.javadev.database.entity_services.SkillDaoService;
import com.goit.javadev.database.entity.skill.SkillLevel;

import java.sql.Connection;
import java.time.LocalDate;

public class DbDaoTest {

    public static void main(String[] args) {
        Storage storage = Storage.getInstance();
        Connection connection = storage.getConnection();

        DeveloperDaoService developerDaoService = new DeveloperDaoService(connection);
        ProjectDaoService projectDaoService = new ProjectDaoService(connection);
        SkillDaoService skillDaoService = new SkillDaoService(connection);


        developerDaoService.insertNewEntity(
                new Developer(1, "Test1", 30, Developer.Gender.MALE, 3000, 1));
        developerDaoService.getEntityById(1);
        developerDaoService.deleteById(1);
        developerDaoService.updateEntityFieldsById(
                new Developer(1, "UpdateTest", 50, Developer.Gender.OTHER, 5000, 2), 2);

        projectDaoService.insertNewEntity(
                new Project(1, "TestNameProject", "TestDescriptionOfProject1", LocalDate.now(), 1, 2));
        projectDaoService.getEntityById(1);
        projectDaoService.deleteById(1);
        projectDaoService.updateEntityFieldsById(
                new Project(2, "TestNameProject2", "TestDescriptionOfProject2", LocalDate.now().plusMonths(1), 3, 2), 2);

        skillDaoService.insertNewEntity(
                new Skill(1, ProgramLang.JAVA, SkillLevel.SENIOR));
        skillDaoService.getEntityById(1);
        skillDaoService.deleteById(1);
        skillDaoService.updateEntityFieldsById(
                new Skill(1, ProgramLang.JAVA_SCRIPT, SkillLevel.MIDDLE), 2);

    }
}
