package constants;

import scalability.LargeDatasetGenerator;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 *The insiparation of having this class is taken from a class of another software developed in
 * 2nd year Software Engineering Group project at King's College London-
 * https://github.com/musalbas/Nuclibook/blob/master/src/main/java/nuclibook/constants/DefaultDatabase.java

 * The class is used to populate database with predefined data.
 * Used to test software during development process.
 */
public class DefaultDatabase {
	/**
	 * reads sql statements from the files created using LargeDatasetGeenrator
	 * and write data to the DefaultDatabase.
	 * @param employeeFile file that contains sql insert statements of employee entities
	 * @param taskFile file that contains sql insert statements of task entities
	 * @param skillFile file that contains sql insert statements of skill entities
	 * @param employeeSkillFile file that contains sql insert statements of employeeSkill entities
	 * @param taskSkillFile file that contains sql insert statements of taskSkill entities
	 * @param projectFile file that contains sql insert statements of project entities
	 * @throws FileNotFoundException
	 */
	public static void createInsertQueries(String employeeFile, String taskFile, String skillFile, String employeeSkillFile, String taskSkillFile, String projectFile) throws FileNotFoundException {
		Scanner scanner = new Scanner(new File(taskFile));
		DefaultDatabase.InsertQueriesTask = new String[LargeDatasetGenerator.numberOfTasks];
		int i=0;
		while(scanner.hasNextLine()) {
			DefaultDatabase.InsertQueriesTask[i++] = scanner.nextLine();
		}

		scanner = new Scanner(new File(employeeFile));
		DefaultDatabase.InsertQueriesEmployee = new String[LargeDatasetGenerator.numberOfEmployees];
		i=0;
		while(scanner.hasNextLine()) {
			DefaultDatabase.InsertQueriesEmployee[i++] = scanner.nextLine();
		}

		scanner = new Scanner(new File(skillFile));
		DefaultDatabase.InsertQueriesSkill = new String[LargeDatasetGenerator.totalNumberOfSkills];
		i=0;
		while(scanner.hasNextLine()) {
			DefaultDatabase.InsertQueriesSkill[i++] = scanner.nextLine();
		}

		scanner = new Scanner(new File(employeeSkillFile));
		DefaultDatabase.InsertQueriesEmployeeSkill = new String[LargeDatasetGenerator.numberOfEmployeeSkillsRows];
		i=0;
		while(scanner.hasNextLine()) {
			DefaultDatabase.InsertQueriesEmployeeSkill[i++] = scanner.nextLine();
		}

		scanner = new Scanner(new File(taskSkillFile));
		DefaultDatabase.InsertQueriesTaskSkill = new String[LargeDatasetGenerator.numberOfTaskSkillsRows];
		i=0;
		while(scanner.hasNextLine()) {
			DefaultDatabase.InsertQueriesTaskSkill[i++] = scanner.nextLine();
		}

		scanner = new Scanner(new File(projectFile));
		DefaultDatabase.InsertQueriesProject = new String[LargeDatasetGenerator.numberOfProjects];
		i=0;
		while(scanner.hasNextLine()) {
			DefaultDatabase.InsertQueriesProject[i++] = scanner.nextLine();
		}
	}

	public static String[] InsertQueriesEmployee = new String[]{
			"INSERT INTO `employee` (`first_name`, `last_name`, `daily_salary`) VALUES ('Employee', '1', 3);",
			"INSERT INTO `employee` (`first_name`, `last_name`, `daily_salary`) VALUES ('Employee', '2', 2);",
			"INSERT INTO `employee` (`first_name`, `last_name`, `daily_salary`) VALUES ('Employee', '3', 4);"

	};

	public static String[] InsertQueriesTask = new String[]{
			"INSERT INTO `task` (`name`, `priority`, `start_time`, `end_time`, `project_id`) VALUES ('Task1', 'HIGH', '2017-1-4', '2017-1-7', 1);",
			"INSERT INTO `task` (`name`, `priority`, `start_time`, `end_time`, `project_id`) VALUES ('Task2', 'LOW', '2017-1-8', '2017-1-9', 1);",
			"INSERT INTO `task` (`name`, `priority`, `start_time`, `end_time`, `project_id`) VALUES ('Task3', 'HIGH', '2017-1-26', '2017-1-29', 2);",
			"INSERT INTO `task` (`name`, `priority`, `start_time`, `end_time`, `project_id`) VALUES ('Task4', 'MEDIUM', '2017-1-26', '2017-1-29', 2);",
	};

	public static String[] InsertQueriesSkill = new String[]{
			"INSERT INTO `skill` (`name`, `level`) VALUES ('skill1', '1');",
			"INSERT INTO `skill` (`name`, `level`) VALUES ('skill2', '1');",
			"INSERT INTO `skill` (`name`, `level`) VALUES ('skill3', '1');",
			"INSERT INTO `skill` (`name`, `level`) VALUES ('skill4', '1');",
			"INSERT INTO `skill` (`name`, `level`) VALUES ('skill5', '1');",
			"INSERT INTO `skill` (`name`, `level`) VALUES ('skill6', '1');",
			"INSERT INTO `skill` (`name`, `level`) VALUES ('skill7', '1');",
			"INSERT INTO `skill` (`name`, `level`) VALUES ('skill8', '1');",
			"INSERT INTO `skill` (`name`, `level`) VALUES ('skill9', '1');",
			"INSERT INTO `skill` (`name`, `level`) VALUES ('skill10', '1');",
			"INSERT INTO `skill` (`name`, `level`) VALUES ('skill11', '1');",
			"INSERT INTO `skill` (`name`, `level`) VALUES ('skill12', '1');",
	};

	public static String[] InsertQueriesEmployeeSkill = new String[]{
			"INSERT INTO `employee_skill` (`employee_id`, `skill_id`) VALUES ('1', '1');",
			"INSERT INTO `employee_skill` (`employee_id`, `skill_id`) VALUES ('1', '2');",
			"INSERT INTO `employee_skill` (`employee_id`, `skill_id`) VALUES ('2', '1');",
			"INSERT INTO `employee_skill` (`employee_id`, `skill_id`) VALUES ('2', '2');",
			"INSERT INTO `employee_skill` (`employee_id`, `skill_id`) VALUES ('3', '2');",
			"INSERT INTO `employee_skill` (`employee_id`, `skill_id`) VALUES ('3', '4');",
			"INSERT INTO `employee_skill` (`employee_id`, `skill_id`) VALUES ('2', '4');"
	};

	public static String[] InsertQueriesTaskSkill = new String[]{
			"INSERT INTO `task_skill` (`task_id`, `skill_id`) VALUES ('1', '1');",
			"INSERT INTO `task_skill` (`task_id`, `skill_id`) VALUES ('2', '2');",
			"INSERT INTO `task_skill` (`task_id`, `skill_id`) VALUES ('3', '3');",
			"INSERT INTO `task_skill` (`task_id`, `skill_id`) VALUES ('4', '4');"
	};

	public static String[] InsertQueriesProject = new String[]{
			"INSERT INTO `project` (`name`, `price`) VALUES ('Project1', 25);",
			"INSERT INTO `project` (`name`, `price`) VALUES ('Project2', 30);"
	};
}
