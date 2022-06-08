package homework_4;

import homework_4.query.Query;

public class HomeWorkQuery {
    public static void main(String[] args) {

        Query query = new Query();

        // вывести на консоль:

        // зарплату(сумму) всех разработчиков отдельного проекта;
        query.getProjectCost(2); //10450
        //query.getProjectCost(1); //8350

        //список разработчиков отдельного проекта;
        query.getAllDevsOnProject(2);

        //список всех Java разработчиков;
        query.getAllDevsByProgramLang("c#");

        //список всех middle разработчиков;
        query.getAllDevsBySkillLevel("junior");

        //список проектов в следующем формате: дата создания - название проекта - количество разработчиков
        // на этом проекте.
        query.getAllProject();
    }
}
