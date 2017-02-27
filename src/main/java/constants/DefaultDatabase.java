package constants;

public class DefaultDatabase {

	public static String[] InsertQueriesEmployee = new String[]{
			//"SET SQL_MODE = \"NO_AUTO_VALUE_ON_ZERO\";",
			//"SET time_zone = \"+00:00\";",
			"INSERT INTO `employee` (`first_name`, `last_name`, `monthly_salary`) VALUES ('Employee1', 'Dede', 2);",
			"INSERT INTO `employee` (`first_name`, `last_name`, `monthly_salary`) VALUES ('Employee2', 'Emin', 2);",
			"INSERT INTO `employee` (`first_name`, `last_name`, `monthly_salary`) VALUES ('Employee3', 'Ekin', 3);",
			"INSERT INTO `employee` (`first_name`, `last_name`, `monthly_salary`) VALUES ('Employee4', 'Kartal', 1);",
			"INSERT INTO `employee` (`first_name`, `last_name`, `monthly_salary`) VALUES ('Employee5', 'Aksoy', 3);",
			"INSERT INTO `employee` (`first_name`, `last_name`, `monthly_salary`) VALUES ('Employee6', 'Nas', 5);",
			"INSERT INTO `employee` (`first_name`, `last_name`, `monthly_salary`) VALUES ('Employee7', 'Albayrak', 1);",
			//"INSERT INTO `employee` (`first_name`, `last_name`) VALUES ('Altan', 'Gunduz');",
			//"INSERT INTO `employee` (`first_name`, `last_name`) VALUES ('Asli', 'Kiraz');"
	};

	public static String[] InsertQueriesTask = new String[]{

			/*"INSERT INTO `task` (`name`, `priority`, `start_time`, `end_time`, `duration`) VALUES ('java-dev', 'HIGH', '2016-12-1', '2016-12-3', 2);",
			"INSERT INTO `task` (`name`, `priority`, `start_time`, `end_time`, `duration`) VALUES ('scala-dev', 'HIGH', '2016-12-2', '2016-12-3', 2);",
			"INSERT INTO `task` (`name`, `priority`, `start_time`, `end_time`, `duration`) VALUES ('cpp-dev', 'LOW', '2016-12-1', '2016-12-2', 1);",
			"INSERT INTO `task` (`name`, `priority`, `start_time`, `end_time`, `duration`) VALUES ('java-test', 'HIGH', '2016-12-1', '2016-12-15', 2);",
			"INSERT INTO `task` (`name`, `priority`, `start_time`, `end_time`, `duration`) VALUES ('devops', 'HIGH', '2016-12-1', '2016-12-10', 7);",
*///			"INSERT INTO `task` (`name`, `priority`, `start_time`, `end_time`, `duration`) VALUES ('integration-test', 'HIGH', '2016-12-1', '2016-12-3', 2);",
//			"INSERT INTO `task` (`name`, `priority`, `start_time`, `end_time`, `duration`) VALUES ('bipartite_matching-config', 'LOW', '2016-12-2', '2016-12-2', 1);",
			"INSERT INTO `task` (`name`, `priority`, `start_time`, `end_time`, `project_id`) VALUES ('Task1', 'HIGH', '2017-1-7', null, '1');",
			"INSERT INTO `task` (`name`, `priority`, `start_time`, `end_time`, `project_id`) VALUES ('Task2', 'HIGH', '2017-1-26', '2017-1-29', 2);",
			"INSERT INTO `task` (`name`, `priority`, `start_time`, `end_time`, `project_id`) VALUES ('Task3', 'HIGH', '2017-1-13', '2017-1-28', 3);",
			"INSERT INTO `task` (`name`, `priority`, `start_time`, `end_time`, `project_id`) VALUES ('Task4', 'HIGH', '2017-1-27', '2017-1-28', 1);",
			"INSERT INTO `task` (`name`, `priority`, `start_time`, `end_time`, `project_id`) VALUES ('Task5', 'HIGH', '2017-1-5', '2017-1-10', 2);",
			"INSERT INTO `task` (`name`, `priority`, `start_time`, `end_time`, `project_id`) VALUES ('Task6', 'HIGH', '2017-1-2', '2017-1-27', 3);",
			/*"INSERT INTO `task` (`name`, `priority`, `start_time`, `end_time`) VALUES ('Task7', 'HIGH', '2017-1-9', '2017-1-29');",
			"INSERT INTO `task` (`name`, `priority`, `start_time`, `end_time`) VALUES ('Task8', 'HIGH', '2017-1-2', '2017-1-23');",
			"INSERT INTO `task` (`name`, `priority`, `start_time`, `end_time`) VALUES ('Task9', 'HIGH', '2017-1-25', '2017-1-28');",
			"INSERT INTO `task` (`name`, `priority`, `start_time`, `end_time`) VALUES ('Task10', 'HIGH', '2017-1-13', '2017-1-26');",
			"INSERT INTO `task` (`name`, `priority`, `start_time`, `end_time`) VALUES ('Task11', 'HIGH', '2017-1-25', '2017-1-25');",
			"INSERT INTO `task` (`name`, `priority`, `start_time`, `end_time`) VALUES ('Task12', 'HIGH', '2017-1-22', '2017-1-29');",
			"INSERT INTO `task` (`name`, `priority`, `start_time`, `end_time`) VALUES ('Task13', 'HIGH', '2017-1-5', '2017-1-10');",
			"INSERT INTO `task` (`name`, `priority`, `start_time`, `end_time`) VALUES ('Task14', 'HIGH', '2017-1-3', '2017-1-27');"
*/
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
/*
	public static String[] InsertQueriesEmployeeSkill = new String[]{

			"INSERT INTO `employee_skill` (`employee_id`, `skill_id`) VALUES ('1', '1');",
			"INSERT INTO `employee_skill` (`employee_id`, `skill_id`) VALUES ('1', '2');",
			//"INSERT INTO `employee_skill` (`employee_id`, `skill_id`) VALUES ('1', '3');",
			//"INSERT INTO `employee_skill` (`employee_id`, `skill_id`) VALUES ('1', '4');",

			//"INSERT INTO `employee_skill` (`employee_id`, `skill_id`) VALUES ('2', '1');",
			//"INSERT INTO `employee_skill` (`employee_id`, `skill_id`) VALUES ('2', '2');",
			"INSERT INTO `employee_skill` (`employee_id`, `skill_id`) VALUES ('2', '3');",

			//"INSERT INTO `employee_skill` (`employee_id`, `skill_id`) VALUES ('3', '3');",
			//"INSERT INTO `employee_skill` (`employee_id`, `skill_id`) VALUES ('3', '1');",
			"INSERT INTO `employee_skill` (`employee_id`, `skill_id`) VALUES ('3', '2');",
			"INSERT INTO `employee_skill` (`employee_id`, `skill_id`) VALUES ('3', '3');",
			"INSERT INTO `employee_skill` (`employee_id`, `skill_id`) VALUES ('3', '4');",
			"INSERT INTO `employee_skill` (`employee_id`, `skill_id`) VALUES ('3', '5');",

			"INSERT INTO `employee_skill` (`employee_id`, `skill_id`) VALUES ('4', '3');",

			//"INSERT INTO `employee_skill` (`employee_id`, `skill_id`) VALUES ('4', '4');",

	};*/


        public static String[] InsertQueriesEmployeeSkill = new String[]{
			"INSERT INTO `employee_skill` (`employee_id`, `skill_id`) VALUES ('1', '1');",
			"INSERT INTO `employee_skill` (`employee_id`, `skill_id`) VALUES ('1', '2');",
			"INSERT INTO `employee_skill` (`employee_id`, `skill_id`) VALUES ('2', '1');",
			"INSERT INTO `employee_skill` (`employee_id`, `skill_id`) VALUES ('2', '2');",
			"INSERT INTO `employee_skill` (`employee_id`, `skill_id`) VALUES ('3', '3');",
			"INSERT INTO `employee_skill` (`employee_id`, `skill_id`) VALUES ('3', '4');",
			"INSERT INTO `employee_skill` (`employee_id`, `skill_id`) VALUES ('3', '5');",
			"INSERT INTO `employee_skill` (`employee_id`, `skill_id`) VALUES ('3', '6');",
			"INSERT INTO `employee_skill` (`employee_id`, `skill_id`) VALUES ('4', '3');",
			"INSERT INTO `employee_skill` (`employee_id`, `skill_id`) VALUES ('4', '4');",
			"INSERT INTO `employee_skill` (`employee_id`, `skill_id`) VALUES ('4', '5');",
			"INSERT INTO `employee_skill` (`employee_id`, `skill_id`) VALUES ('4', '6');",
			"INSERT INTO `employee_skill` (`employee_id`, `skill_id`) VALUES ('5', '3');",
			"INSERT INTO `employee_skill` (`employee_id`, `skill_id`) VALUES ('5', '4');",
			"INSERT INTO `employee_skill` (`employee_id`, `skill_id`) VALUES ('5', '5');",
			"INSERT INTO `employee_skill` (`employee_id`, `skill_id`) VALUES ('5', '6');",
			"INSERT INTO `employee_skill` (`employee_id`, `skill_id`) VALUES ('6', '3');",
			"INSERT INTO `employee_skill` (`employee_id`, `skill_id`) VALUES ('6', '4');",
			"INSERT INTO `employee_skill` (`employee_id`, `skill_id`) VALUES ('6', '5');",
			"INSERT INTO `employee_skill` (`employee_id`, `skill_id`) VALUES ('6', '6');",
			"INSERT INTO `employee_skill` (`employee_id`, `skill_id`) VALUES ('7', '6');"
        };
	public static String[] InsertQueriesTaskSkill = new String[]{
			"INSERT INTO `task_skill` (`task_id`, `skill_id`) VALUES ('1', '1');",
			"INSERT INTO `task_skill` (`task_id`, `skill_id`) VALUES ('2', '2');",
			"INSERT INTO `task_skill` (`task_id`, `skill_id`) VALUES ('3', '3');",
			"INSERT INTO `task_skill` (`task_id`, `skill_id`) VALUES ('4', '4');",
			"INSERT INTO `task_skill` (`task_id`, `skill_id`) VALUES ('5', '5');",
			"INSERT INTO `task_skill` (`task_id`, `skill_id`) VALUES ('6', '6');",
			/*"INSERT INTO `task_skill` (`task_id`, `skill_id`) VALUES ('7', '1');",
			"INSERT INTO `task_skill` (`task_id`, `skill_id`) VALUES ('8', '2');",
			"INSERT INTO `task_skill` (`task_id`, `skill_id`) VALUES ('9', '3');",
			"INSERT INTO `task_skill` (`task_id`, `skill_id`) VALUES ('10', '4');",
			"INSERT INTO `task_skill` (`task_id`, `skill_id`) VALUES ('11', '5');",
			"INSERT INTO `task_skill` (`task_id`, `skill_id`) VALUES ('12', '6');",
			"INSERT INTO `task_skill` (`task_id`, `skill_id`) VALUES ('13', '1');",
			"INSERT INTO `task_skill` (`task_id`, `skill_id`) VALUES ('14', '2');"*/
	};

	public static String[] InsertQueriesProject = new String[]{
			"INSERT INTO `project` (`name`, `cost`) VALUES ('Project1', '15');",
			"INSERT INTO `project` (`name`, `cost`) VALUES ('Project2', '10');",
			"INSERT INTO `project` (`name`, `cost`) VALUES ('Project3', '15');",
			/*"INSERT INTO `project` (`name`, `cost`) VALUES ('Project4', 'Kartal');",
			"INSERT INTO `project` (`name`, `cost`) VALUES ('Project5', 'Aksoy');",
			"INSERT INTO `project` (`name`, `cost`) VALUES ('Project6', 'Nas');",
			"INSERT INTO `project` (`name`, `cost`) VALUES ('Project7', 'Albayrak');",*/
	};

}
