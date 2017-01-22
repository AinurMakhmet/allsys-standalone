/*
package models.graph_models;

import entity_utils.TaskUtils;
import javafx.util.Pair;
import models.Employee;
import models.Skill;
import models.Task;

import java.io.IOException;
import java.util.*;

*/
/**
 * Created by nura on 15/01/17.
 *//*

public class BipartiteGraph {
    private Map<Integer, Pair<Integer, Collection>> taskMap = new HashMap<>();
    private Map<Integer, Pair<Integer, Collection>> employeeMap = new HashMap<>();
    //private Set<BipartiteGraphEdge> allMatchings = new HashSet<>();
    private Set<BipartiteGraphEdge> largestMatching = new HashSet<>();
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

                Collection taskMatchings = new ArrayList<>();
                for (Employee employee: task.possibleAssignee) {
                    totalTaskMatches++;
                    employeeMap.put(employee.getId(), null);
                    //allMatchings.add(new BipartiteGraphEdge(task, employee));
                    taskMatchings.add(employee.getId());
                }
                //adds new entry to the adjacency list

                taskMap.put(task.getId(), new Pair(null, taskMatchings));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        for (Integer employeeId: employeeMap.keySet()) {
            Collection employeeMatches = new ArrayList<>();

            for (Integer taskId: taskMap.keySet())
                if (taskMap.get(taskId).getValue().contains(employeeId)) {
                    employeeMatches.add(taskId);
                    totalEmployeeMatches++;
                }
            employeeMap.put(employeeId, new Pair(null, employeeMatches));
            //allMatchings.add(new BipartiteGraphEdge(task, employee));
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
            for (Object employeeId: taskMap.get(taskId).getValue()) {
                System.out.print("employee " + (Integer)employeeId+ ", ");
            }
            System.out.println();
        }
        System.out.println("---------Set of all employee matches---------: ");
        for (Integer employeeId: employeeMap.keySet()) {
            System.out.print("employee " + employeeId + ": ");
            for (Object taskId: employeeMap.get(employeeId).getValue()) {
                System.out.print("task " + (Integer)taskId+ ", ");
            }
            System.out.println();
        }
        */
/*taskMap.keySet().forEach(
                task -> {
                        System.out.print("task " + task.getId() + ": ");
                        task.getEdges().forEach(edge ->System.out.print("employee " + edge.getEmployee().getId()+ ", "));
                        System.out.println();
                });*//*

        System.out.println(totalEmployeeMatches==totalTaskMatches);
        System.out.println("==============BIPARTITE GRAPH END==============");
    }

    public Map<Integer, Pair<Integer, Collection>> getTaskMap() {
        return taskMap;
    }

    public Map<Integer, Pair<Integer, Collection>> getEmployeeMap() {
        return employeeMap;
    }
*/
/*
    public Set<BipartiteGraphEdge> getAllMatchings() {
        return allMatchings;
    }*//*


    public Set<BipartiteGraphEdge> getLargestMatching() {
        return largestMatching;
    }


}
*/
