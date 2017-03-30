package constants;

public class DefaultDatabase {

	//Test1- Complete Bipratite Graph
	public static String[] InsertQueriesEmployee = new String[]{
			//Test1
			/*"INSERT INTO `employee` (`first_name`, `last_name`, `daily_salary`) VALUES ('Employee1', 'Dede', 2);",
			"INSERT INTO `employee` (`first_name`, `last_name`, `daily_salary`) VALUES ('Employee2', 'Emin', 3);",
			"INSERT INTO `employee` (`first_name`, `last_name`, `daily_salary`) VALUES ('Employee3', 'Ekin', 4);",
			"INSERT INTO `employee` (`first_name`, `last_name`, `daily_salary`) VALUES ('Employee4', 'Kartal', 1);",
			"INSERT INTO `employee` (`first_name`, `last_name`, `daily_salary`) VALUES ('Employee5', 'Aksoy', 3);",
			*///Test2
/*
			"INSERT INTO `employee` (`first_name`, `last_name`, `daily_salary`) VALUES ('Employee1', 'Dede', 2);",
			"INSERT INTO `employee` (`first_name`, `last_name`, `daily_salary`) VALUES ('Employee2', 'Emin', 3);",
			"INSERT INTO `employee` (`first_name`, `last_name`, `daily_salary`) VALUES ('Employee3', 'Ekin', 4);",
			"INSERT INTO `employee` (`first_name`, `last_name`, `daily_salary`) VALUES ('Employee4', 'Dede', 2);",
			"INSERT INTO `employee` (`first_name`, `last_name`, `daily_salary`) VALUES ('Employee5', 'Emin', 3);",
*/
//Evaluation
			"INSERT INTO `employee` (`first_name`, `last_name`) VALUES ('Employee', '1');",
			"INSERT INTO `employee` (`first_name`, `last_name`) VALUES ('Employee', '2');",
			"INSERT INTO `employee` (`first_name`, `last_name`) VALUES ('Employee', '3');",
			"INSERT INTO `employee` (`first_name`, `last_name`) VALUES ('Employee', '4');",
			"INSERT INTO `employee` (`first_name`, `last_name`) VALUES ('Employee', '5');",
			"INSERT INTO `employee` (`first_name`, `last_name`) VALUES ('Employee', '6');"

};

	public static String[] InsertQueriesTask = new String[]{
			//Test1
			/*"INSERT INTO `task` (`name`, `priority`, `start_time`, `end_time`, `project_id`) VALUES ('Task1', 'LOW', '2017-1-7', '2017-1-9', 1);",
			"INSERT INTO `task` (`name`, `priority`, `start_time`, `end_time`, `project_id`) VALUES ('Task2', 'LOW', '2017-1-7', '2017-1-29', 1);",
			"INSERT INTO `task` (`name`, `priority`, `start_time`, `end_time`, `project_id`) VALUES ('Task3', 'LOW', '2017-1-7', '2017-1-28', 1);",
			"INSERT INTO `task` (`name`, `priority`, `start_time`, `end_time`, `project_id`) VALUES ('Task4', 'LOW', '2017-1-7', '2017-1-28', 1);",
			"INSERT INTO `task` (`name`, `priority`, `start_time`, `end_time`, `project_id`) VALUES ('Task5', 'LOW', '2017-1-7', '2017-1-10', 1);",
			*///Test2
			/*"INSERT INTO `task` (`name`, `priority`, `start_time`, `end_time`, `project_id`) VALUES ('Task1', 'LOW', '2017-1-7', '2017-1-9', 1);",
			"INSERT INTO `task` (`name`, `priority`, `start_time`, `end_time`, `project_id`) VALUES ('Task2', 'LOW', '2017-1-7', '2017-1-29', 1);",
			"INSERT INTO `task` (`name`, `priority`, `start_time`, `end_time`, `project_id`) VALUES ('Task3', 'LOW', '2017-1-7', '2017-1-28', 1);",*/
			//Evaluation
			"INSERT INTO `task` (`name`, `priority`, `start_time`, `end_time`) VALUES ('Task1', 'HIGH', '2017-5-4', '2017-5-29');",
			"INSERT INTO `task` (`name`, `priority`, `start_time`, `end_time`) VALUES ('Task2', 'MEDIUM', '2017-1-3', '2017-2-13');",
			"INSERT INTO `task` (`name`, `priority`, `start_time`, `end_time`) VALUES ('Task3', 'MEDIUM', '2017-3-27', '2017-4-4');",
			"INSERT INTO `task` (`name`, `priority`, `start_time`, `end_time`) VALUES ('Task4', 'HIGH', '2017-2-21', '2017-4-4');",
			"INSERT INTO `task` (`name`, `priority`, `start_time`, `end_time`) VALUES ('Task5', 'HIGH', '2017-3-20', '2017-4-20');",
			"INSERT INTO `task` (`name`, `priority`, `start_time`, `end_time`) VALUES ('Task6', 'HIGH', '2017-5-10', '2017-5-28');",
			"INSERT INTO `task` (`name`, `priority`, `start_time`, `end_time`) VALUES ('Task7', 'MEDIUM', '2017-2-19', '2017-5-29');",
			"INSERT INTO `task` (`name`, `priority`, `start_time`, `end_time`) VALUES ('Task8', 'MEDIUM', '2017-2-28', '2017-3-14');",
			"INSERT INTO `task` (`name`, `priority`, `start_time`, `end_time`) VALUES ('Task9', 'LOW', '2017-4-15', '2017-4-27');",
			"INSERT INTO `task` (`name`, `priority`, `start_time`, `end_time`) VALUES ('Task10', 'HIGH', '2017-1-1', '2017-3-20');",
			"INSERT INTO `task` (`name`, `priority`, `start_time`, `end_time`) VALUES ('Task11', 'MEDIUM', '2017-4-8', '2017-5-8');",
			"INSERT INTO `task` (`name`, `priority`, `start_time`, `end_time`) VALUES ('Task12', 'MEDIUM', '2017-4-4', '2017-5-8');"


};

	public static String[] InsertQueriesSkill = new String[]{
			//Test1 - Test2
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
			//Test1
			/*"INSERT INTO `employee_skill` (`employee_id`, `skill_id`) VALUES ('1', '1');",
			"INSERT INTO `employee_skill` (`employee_id`, `skill_id`) VALUES ('1', '2');",
			"INSERT INTO `employee_skill` (`employee_id`, `skill_id`) VALUES ('1', '3');",
			"INSERT INTO `employee_skill` (`employee_id`, `skill_id`) VALUES ('1', '5');",
			"INSERT INTO `employee_skill` (`employee_id`, `skill_id`) VALUES ('2', '2');",
			"INSERT INTO `employee_skill` (`employee_id`, `skill_id`) VALUES ('2', '5');",
			"INSERT INTO `employee_skill` (`employee_id`, `skill_id`) VALUES ('3', '1');",
			"INSERT INTO `employee_skill` (`employee_id`, `skill_id`) VALUES ('3', '4');",
			"INSERT INTO `employee_skill` (`employee_id`, `skill_id`) VALUES ('4', '1');",
			"INSERT INTO `employee_skill` (`employee_id`, `skill_id`) VALUES ('4', '3');",
			"INSERT INTO `employee_skill` (`employee_id`, `skill_id`) VALUES ('4', '5');",
			"INSERT INTO `employee_skill` (`employee_id`, `skill_id`) VALUES ('5', '1');",
			"INSERT INTO `employee_skill` (`employee_id`, `skill_id`) VALUES ('5', '2');",
			*/	//Test2
				/*"INSERT INTO `employee_skill` (`employee_id`, `skill_id`) VALUES ('1', '3');",
				"INSERT INTO `employee_skill` (`employee_id`, `skill_id`) VALUES ('2', '1');",
				"INSERT INTO `employee_skill` (`employee_id`, `skill_id`) VALUES ('4', '1');",
				"INSERT INTO `employee_skill` (`employee_id`, `skill_id`) VALUES ('4', '2');",
				"INSERT INTO `employee_skill` (`employee_id`, `skill_id`) VALUES ('4', '3');",
				"INSERT INTO `employee_skill` (`employee_id`, `skill_id`) VALUES ('5', '1');"*/
			//Evaluation
				"INSERT INTO `employee_skill` (`employee_id`, `skill_id`) VALUES ('1', '4');",
				"INSERT INTO `employee_skill` (`employee_id`, `skill_id`) VALUES ('1', '12');",
				"INSERT INTO `employee_skill` (`employee_id`, `skill_id`) VALUES ('2', '3');",
				"INSERT INTO `employee_skill` (`employee_id`, `skill_id`) VALUES ('3', '1');",
				"INSERT INTO `employee_skill` (`employee_id`, `skill_id`) VALUES ('3', '4');",
				"INSERT INTO `employee_skill` (`employee_id`, `skill_id`) VALUES ('4', '4');",
				"INSERT INTO `employee_skill` (`employee_id`, `skill_id`) VALUES ('5', '2');",
				"INSERT INTO `employee_skill` (`employee_id`, `skill_id`) VALUES ('5', '5');",
				"INSERT INTO `employee_skill` (`employee_id`, `skill_id`) VALUES ('5', '10');",
				"INSERT INTO `employee_skill` (`employee_id`, `skill_id`) VALUES ('6', '8');",
				"INSERT INTO `employee_skill` (`employee_id`, `skill_id`) VALUES ('6', '12');"

};
	public static String[] InsertQueriesTaskSkill = new String[]{
			//Test1
			/*"INSERT INTO `task_skill` (`task_id`, `skill_id`) VALUES ('1', '1');",
			"INSERT INTO `task_skill` (`task_id`, `skill_id`) VALUES ('2', '2');",
			"INSERT INTO `task_skill` (`task_id`, `skill_id`) VALUES ('3', '3');",
			"INSERT INTO `task_skill` (`task_id`, `skill_id`) VALUES ('4', '4');",
			"INSERT INTO `task_skill` (`task_id`, `skill_id`) VALUES ('5', '5');",
	*/		//Test2
			/*"INSERT INTO `task_skill` (`task_id`, `skill_id`) VALUES ('1', '1');",
			"INSERT INTO `task_skill` (`task_id`, `skill_id`) VALUES ('2', '2');",
			"INSERT INTO `task_skill` (`task_id`, `skill_id`) VALUES ('3', '3');",*/
			//Evaluation
			"INSERT INTO `task_skill` (`task_id`, `skill_id`) VALUES ('1', '12');",
			"INSERT INTO `task_skill` (`task_id`, `skill_id`) VALUES ('2', '10');",
			"INSERT INTO `task_skill` (`task_id`, `skill_id`) VALUES ('3', '6');",
			"INSERT INTO `task_skill` (`task_id`, `skill_id`) VALUES ('4', '12');",
			"INSERT INTO `task_skill` (`task_id`, `skill_id`) VALUES ('5', '1');",
			"INSERT INTO `task_skill` (`task_id`, `skill_id`) VALUES ('6', '6');",
			"INSERT INTO `task_skill` (`task_id`, `skill_id`) VALUES ('7', '9');",
			"INSERT INTO `task_skill` (`task_id`, `skill_id`) VALUES ('8', '6');",
			"INSERT INTO `task_skill` (`task_id`, `skill_id`) VALUES ('9', '4');",
			"INSERT INTO `task_skill` (`task_id`, `skill_id`) VALUES ('10', '9');",
			"INSERT INTO `task_skill` (`task_id`, `skill_id`) VALUES ('11', '2');",
			"INSERT INTO `task_skill` (`task_id`, `skill_id`) VALUES ('12', '12');"


};

	public static String[] InsertQueriesProject = new String[]{
			//Test1
			"INSERT INTO `project` (`name`, `price`) VALUES ('Project1', 15);",
			//"INSERT INTO `project` (`name`, `price`) VALUES ('Project2', 10);",
			//"INSERT INTO `project` (`name`, `price`) VALUES ('Project3', 15);",
			/*"INSERT INTO `project` (`name`, `cost`) VALUES ('Project4', 'Kartal');",
			"INSERT INTO `project` (`name`, `cost`) VALUES ('Project5', 'Aksoy');",
			"INSERT INTO `project` (`name`, `cost`) VALUES ('Project6', 'Nas');",
			"INSERT INTO `project` (`name`, `cost`) VALUES ('Project7', 'Albayrak');",*/
	};

}
