package models;

import entity_utils.EmployeeUtils;
import entity_utils.ProjectUtils;
import entity_utils.SkillUtils;
import entity_utils.TaskUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * All the main data on which the system operates.
 */
public class SystemData {
    public static final List<Skill> allSkills = SkillUtils.getAllSkills();

    private static final Map<Integer, Task> allTasksMap = new HashMap<>();
    public static Map<Integer, Task> getAllTasksMap() {
        if (allTasksMap.size()==0) {
            TaskUtils.getAllTasks().forEach(task -> allTasksMap.put(task.getId(), task));
        }
        return allTasksMap;
    }

    private static final Map<Integer, Employee> allEmployeesMap = new HashMap<>();
    public static Map<Integer, Employee> getAllEmployeesMap() {
        if (allEmployeesMap.size()==0) {
            EmployeeUtils.getAllEmployees().forEach(employee -> allEmployeesMap.put(employee.getId(), employee));
        }
        return allEmployeesMap;
    }

    private static final Map<Integer, Project> allProjectsMap = new HashMap<>();
    public static Map<Integer, Project> getAllProjectsMap() {
        if (allProjectsMap.size()==0) {
            ProjectUtils.getAllProjects().forEach(project -> allProjectsMap.put(project.getId(), project));
        }
        return allProjectsMap;
    }

    /**
     * Gets the data from the database before UI launches.
     */
    public static void getDataFromDatabase() {
        getAllTasksMap();
        getAllEmployeesMap();
        getAllProjectsMap();
        //list of skills is already populated when initialised;
    }


}
