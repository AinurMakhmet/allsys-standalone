package constants;

import scalability.LargeDatasetGenerator;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ReadFromFileDatabase {

	public static void createInsertQueries(String employeeFile, String taskFile, String skillFile, String employeeSkillFile, String taskSkillFile) throws FileNotFoundException {
		Scanner scanner = new Scanner(new File(employeeFile));
		DefaultDatabase.InsertQueriesEmployee = new String[LargeDatasetGenerator.numberOfEmployees];
		int i=0;
		while(scanner.hasNextLine()) {
			DefaultDatabase.InsertQueriesEmployee[i++] = scanner.nextLine();
		}

		scanner = new Scanner(new File(taskFile));
		DefaultDatabase.InsertQueriesTask = new String[LargeDatasetGenerator.numberOfTasks];
		i=0;
		while(scanner.hasNextLine()) {
			DefaultDatabase.InsertQueriesTask[i++] = scanner.nextLine();
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
	}

	public static String[] InsertQueriesEmployee;

	public static String[] InsertQueriesTask;

	public static String[] InsertQueriesSkill;

	public static String[] InsertQueriesEmployeeSkill;

	public static String[] InsertQueriesTaskSkill;

}
