package constants;

public class DefaultDatabase {

	public static String[] InsertQueriesEmployee = new String[]{
			//"SET SQL_MODE = \"NO_AUTO_VALUE_ON_ZERO\";",
			//"SET time_zone = \"+00:00\";",
			"INSERT INTO `employee` (`first_name`, `last_name`) VALUES ('Aydin', 'Dede');",
			"INSERT INTO `employee` (`first_name`, `last_name`) VALUES ('Ahmet', 'Emin');",
			"INSERT INTO `employee` (`first_name`, `last_name`) VALUES ('Ahu', 'Ekin');",
//			"INSERT INTO `employee` (`first_name`, `last_name`) VALUES ('Adem', 'Kartal');",
//			"INSERT INTO `employee` (`first_name`, `last_name`) VALUES ('Akila', 'Aksoy');",
//			"INSERT INTO `employee` (`first_name`, `last_name`) VALUES ('Aliye', 'Nas');",
//			"INSERT INTO `employee` (`first_name`, `last_name`) VALUES ('Alp', 'Albayrak');",
//			"INSERT INTO `employee` (`first_name`, `last_name`) VALUES ('Altan', 'Gunduz');",
//			"INSERT INTO `employee` (`first_name`, `last_name`) VALUES ('Asli', 'Kiraz');",
	};

	public static String[] InsertQueriesTask = new String[]{

			"INSERT INTO `task` (`name`) VALUES ('java-dev');",
			"INSERT INTO `task` (`name`) VALUES ('scala-dev');",
			"INSERT INTO `task` (`name`) VALUES ('cpp-dev');",
			//"INSERT INTO `task` (`name`) VALUES ('java-test');",
			//"INSERT INTO `task` (`name`) VALUES ('devops');",
			//"INSERT INTO `task` (`name`) VALUES ('integration-test');",
			//"INSERT INTO `task` (`name`) VALUES ('network-config');",
	};

	public static String[] InsertQueriesSkill = new String[]{
			"INSERT INTO `skill` (`name`, `level`) VALUES ('java', '5');",
			"INSERT INTO `skill` (`name`, `level`) VALUES ('scala', '5');",
			"INSERT INTO `skill` (`name`, `level`) VALUES ('cpp', '5');",
			//"INSERT INTO `task` (`name`, `level`) VALUES ('java', '5');",
			//"INSERT INTO `task` (`name`, `level`) VALUES ('java', '5');",
	};

	public static String[] InsertQueriesEmployeeSkill = new String[]{

			"INSERT INTO `employee_skill` (`employee_id`, `skill_id`) VALUES ('1', '1');",
			"INSERT INTO `employee_skill` (`employee_id`, `skill_id`) VALUES ('1', '2');",
			"INSERT INTO `employee_skill` (`employee_id`, `skill_id`) VALUES ('1', '3');",
			"INSERT INTO `employee_skill` (`employee_id`, `skill_id`) VALUES ('2', '2');",
			"INSERT INTO `employee_skill` (`employee_id`, `skill_id`) VALUES ('2', '3');",
			"INSERT INTO `employee_skill` (`employee_id`, `skill_id`) VALUES ('3', '3');",
	};

	public static String[] InsertQueriesTaskSkill = new String[]{
			"INSERT INTO `task_skill` (`task_id`, `skill_id`) VALUES ('1', '1');",
			"INSERT INTO `task_skill` (`task_id`, `skill_id`) VALUES ('2', '2');",
			"INSERT INTO `task_skill` (`task_id`, `skill_id`) VALUES ('3', '3');",
	};

}
