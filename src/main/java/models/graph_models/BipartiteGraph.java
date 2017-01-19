package models.graph_models;

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
    private Map<Integer, Task> taskMap = new HashMap<>();
    private Map<Integer, Employee> employeeMap = new HashMap<>();
    private Set<BipartiteGraphEdge> allMatchings = new HashSet<>();
    private Set<BipartiteGraphEdge> largestMatching = new HashSet<>();


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

                for (Employee employee: task.possibleAssignee) {
                    employeeMap.put(employee.getId(), employee);
                    allMatchings.add(new BipartiteGraphEdge(task, employee));
                }
                //adds new entry to the adjacency list
                taskMap.put(task.getId(), task);
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
        taskMap.values().forEach(task ->System.out.println("task " + task.getId()));
        System.out.println("---------Set of employees---------: ");
        employeeMap.values().forEach(employee ->System.out.println("employee " + employee.getId()));
        System.out.println("---------Set of allMatchings edges---------: ");
        taskMap.values().forEach(
                task -> {
                        System.out.print("task " + task.getId() + ": ");
                        task.getEdges().forEach(edge ->System.out.print("employee " + edge.getEmployee().getId()+ ", "));
                        System.out.println();
                });
        System.out.println("==============BIPARTITE GRAPH END==============");
    }

    public Map<Integer,Task> getTaskMap() {
        return taskMap;
    }

    public Map<Integer, Employee> getEmployeeMap() {
        return employeeMap;
    }

    public Set<BipartiteGraphEdge> getAllMatchings() {
        return allMatchings;
    }

    public Set<BipartiteGraphEdge> getLargestMatching() {
        return largestMatching;
    }


}
