package constants;

public class DefaultDatabase {

	public static String[] InsertQueriesEmployee = new String[]{
			//"SET SQL_MODE = \"NO_AUTO_VALUE_ON_ZERO\";",
			//"SET time_zone = \"+00:00\";",
			"INSERT INTO `employee` (`first_name`, `last_name`) VALUES ('Aydin', 'Dede');",
			"INSERT INTO `employee` (`first_name`, `last_name`) VALUES ('Ahmet', 'Emin');",
			"INSERT INTO `employee` (`first_name`, `last_name`) VALUES ('Ahu', 'Ekin');",
			"INSERT INTO `employee` (`first_name`, `last_name`) VALUES ('Adem', 'Kartal');",
//			"INSERT INTO `employee` (`first_name`, `last_name`) VALUES ('Akila', 'Aksoy');",
//			"INSERT INTO `employee` (`first_name`, `last_name`) VALUES ('Aliye', 'Nas');",
//			"INSERT INTO `employee` (`first_name`, `last_name`) VALUES ('Alp', 'Albayrak');",
//			"INSERT INTO `employee` (`first_name`, `last_name`) VALUES ('Altan', 'Gunduz');",
//			"INSERT INTO `employee` (`first_name`, `last_name`) VALUES ('Asli', 'Kiraz');",
	};

	public static String[] InsertQueriesTask = new String[]{

			"INSERT INTO `task` (`name`, `priority`, `start_time`, `end_time`, `duration`) VALUES ('java-dev', 'HIGH', '2016-12-1', '2016-12-3', 2);",
			"INSERT INTO `task` (`name`, `priority`, `start_time`, `end_time`, `duration`) VALUES ('scala-dev', 'HIGH', '2016-12-2', '2016-12-3', 2);",
			"INSERT INTO `task` (`name`, `priority`, `start_time`, `end_time`, `duration`) VALUES ('cpp-dev', 'LOW', '2016-12-1', '2016-12-2', 1);",
			"INSERT INTO `task` (`name`, `priority`, `start_time`, `end_time`, `duration`) VALUES ('java-test', 'HIGH', '2016-12-1', '2016-12-15', 2);",
			"INSERT INTO `task` (`name`, `priority`, `start_time`, `end_time`, `duration`) VALUES ('devops', 'HIGH', '2016-12-1', '2016-12-10', 7);",
			"INSERT INTO `task` (`name`, `priority`, `start_time`, `end_time`, `duration`) VALUES ('integration-test', 'HIGH', '2016-12-1', '2016-12-3', 2);",
			"INSERT INTO `task` (`name`, `priority`, `start_time`, `end_time`, `duration`) VALUES ('network-config', 'LOW', '2016-12-2', '2016-12-2', 1);",
	};

	public static String[] InsertQueriesSkill = new String[]{
			"INSERT INTO `skill` (`name`, `level`) VALUES ('java', '5');",
			"INSERT INTO `skill` (`name`, `level`) VALUES ('scala', '5');",
			"INSERT INTO `skill` (`name`, `level`) VALUES ('cpp', '5');",
			"INSERT INTO `skill` (`name`, `level`) VALUES ('git', '5');",
			//"INSERT INTO `task` (`name`, `level`) VALUES ('java', '5');",
			//"INSERT INTO `task` (`name`, `level`) VALUES ('java', '5');",
	};

	public static String[] InsertQueriesEmployeeSkill = new String[]{

			"INSERT INTO `employee_skill` (`employee_id`, `skill_id`) VALUES ('1', '1');",
			"INSERT INTO `employee_skill` (`employee_id`, `skill_id`) VALUES ('1', '2');",
			"INSERT INTO `employee_skill` (`employee_id`, `skill_id`) VALUES ('1', '3');",
			"INSERT INTO `employee_skill` (`employee_id`, `skill_id`) VALUES ('1', '4');",

			"INSERT INTO `employee_skill` (`employee_id`, `skill_id`) VALUES ('2', '2');",
			"INSERT INTO `employee_skill` (`employee_id`, `skill_id`) VALUES ('2', '3');",

			"INSERT INTO `employee_skill` (`employee_id`, `skill_id`) VALUES ('3', '3');",
			"INSERT INTO `employee_skill` (`employee_id`, `skill_id`) VALUES ('3', '4');",
			//"INSERT INTO `employee_skill` (`employee_id`, `skill_id`) VALUES ('4', '1');",
			//"INSERT INTO `employee_skill` (`employee_id`, `skill_id`) VALUES ('4', '2');",
			//"INSERT INTO `employee_skill` (`employee_id`, `skill_id`) VALUES ('4', '3');",
			//"INSERT INTO `employee_skill` (`employee_id`, `skill_id`) VALUES ('4', '4');",

	};

	public static String[] InsertQueriesTaskSkill = new String[]{
			"INSERT INTO `task_skill` (`task_id`, `skill_id`) VALUES ('1', '1');",
			"INSERT INTO `task_skill` (`task_id`, `skill_id`) VALUES ('2', '2');",
			"INSERT INTO `task_skill` (`task_id`, `skill_id`) VALUES ('3', '3');",
			"INSERT INTO `task_skill` (`task_id`, `skill_id`) VALUES ('3', '4');"
	};

}
