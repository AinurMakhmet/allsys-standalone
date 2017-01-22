package models.bipartite_matching;

import entity_utils.TaskUtils;
import models.Employee;
import models.Skill;
import models.Task;

import java.io.IOException;
import java.util.*;

/**
 * Created by nura on 15/01/17.
 */
public class BipartiteGraph {
    private Map<Integer, Set<Vertex>> taskMap = new HashMap<>();
    private Map<Integer, Set<Vertex>> employeeMap = new HashMap<>();
    private int totalTaskMatches = 0;
    private int totalEmployeeMatches = 0;


    public BipartiteGraph() {
        for (Task task: (ArrayList<Task>) TaskUtils.getAllTasksValidForAllocation()) {
            //creates a temporary list of skills and adds list of skills in a task to that list, for future opearations
            ArrayList<Skill> taskSkills = null;
            try {

                taskSkills = new ArrayList<>(task.getSkills());

                // finds the index of the skill that is possessed by smallest amount of employees.
                int indexSmallestSize = getIndexOfDefiningSkill(taskSkills);

                task.possibleAssignee = (ArrayList<Employee>) taskSkills.get(indexSmallestSize).getEmployees();
                //System.out.println("task" +task);
                taskSkills.remove(indexSmallestSize);

                ArrayList<Employee> definingSkillEmployees = (ArrayList<Employee>) task.getSkills().get(indexSmallestSize).getEmployees();
                filterPossibleAssignee(task, taskSkills, definingSkillEmployees);

                Set<Vertex> adjacentVerticesOfTask = new HashSet<>();
                //adds new entries to the adjacency map of both tasks and employees
                for (Employee employee: task.possibleAssignee) {
                    totalTaskMatches++;
                    adjacentVerticesOfTask.add( new Vertex(employee.getId(), VertexType.EMPLOYEE));

                    Integer employeeId = employee.getId();
                    Set<Vertex> adjacentVerticesOfEmployee = new HashSet<>();
                    if (employeeMap.containsKey(employeeId)) {
                        adjacentVerticesOfEmployee = employeeMap.get(employeeId);
                    } else {
                        adjacentVerticesOfEmployee = new HashSet<>();
                    }
                    totalEmployeeMatches++;
                    adjacentVerticesOfEmployee.add( new Vertex(task.getId(), VertexType.TASK));
                    employeeMap.put(employeeId, adjacentVerticesOfEmployee);
                }

                taskMap.put(task.getId(), adjacentVerticesOfTask);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void filterPossibleAssignee(Task task, ArrayList<Skill> taskSkills, ArrayList<Employee> definingSkillEmployees) throws IOException {
        for (Skill s : taskSkills){
            for (int i=0; i<definingSkillEmployees.size(); i++) {
                ArrayList<Skill> employeeSkills = (ArrayList<Skill>) definingSkillEmployees.get(i).getSkills();
                for (int j = 0; j< employeeSkills.size(); j++) {
                    if (s.getId()==employeeSkills.get(j).getId())  {
                        break;
                    }
                    else if (j == employeeSkills.size()-1) {
                        //System.out.println("Employee " + employees.get(i) + " doesn'task have skill " + s.getName());
                        //System.out.println("Employee skill: " + employees.get(i).getSkills());

                        //the employee doesn'task have all the required taskSkills to complete the task,
                        //so it will be removed from the list of candidates
                        task.possibleAssignee.remove(definingSkillEmployees.get(i));
                    }
                }
            }
        }
    }

    private int getIndexOfDefiningSkill(ArrayList<Skill> taskSkills) throws IOException {
        //index of the skill in the taskSkills that have a minimum number of Employees that posses this skill
        int indexSmallestSize = 0;

        int minSize = taskSkills.get(0).getEmployees().size();
        for (int i = 0; i<taskSkills.size(); i++) {
            if (minSize > taskSkills.get(i).getEmployees().size()) {
                indexSmallestSize = i;
                minSize = taskSkills.get(i).getEmployees().size();
            }
        }
        return indexSmallestSize;

    }

    public void printGraph() {
        System.out.println("==============BIPARTITE GRAPH START==============");

        System.out.println("-------Set of tasks---------: ");
        taskMap.keySet().forEach(taskId ->System.out.println("task " + taskId));

        System.out.println("---------Set of employees---------: ");
        employeeMap.keySet().forEach(employeeId ->System.out.println("employee " + employeeId));

        System.out.println("---------Set of all task matches---------: ");
        for (Integer taskId: taskMap.keySet()) {
            System.out.print("task " + taskId + ": ");
            taskMap.get(taskId).forEach(vertex -> {
                System.out.print("employee " + (Integer)vertex.getVertexId()+ ", ");
            });
            System.out.println();
        }
        System.out.println("---------Set of all employee matches---------: ");
        for (Integer employeeId: employeeMap.keySet()) {
            System.out.print("employee " + employeeId + ": ");
            employeeMap.get(employeeId).forEach(vertex ->  {
                System.out.print("task " + (Integer)vertex.getVertexId()+ ", ");
            });
            System.out.println();
        }
        System.out.println(totalEmployeeMatches==totalTaskMatches);
        System.out.println("==============BIPARTITE GRAPH END==============");
    }

    public Map<Integer, Set<Vertex>> getTaskMap() {
        return taskMap;
    }

    public Map<Integer, Set<Vertex>> getEmployeeMap() {
        return employeeMap;
    }
}
