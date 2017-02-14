package models.bipartite_matching;

import models.Employee;
import models.Skill;
import models.Task;

import java.io.IOException;
import java.util.*;

/**
 * Created by nura on 15/01/17.
 */
public class BipartiteGraph {
    private Map<Vertex, Map<Vertex, Boolean>> taskMap = new HashMap<>();
    private Map<Vertex, Map<Vertex, Boolean>> employeeMap = new HashMap<>();
    private int totalTaskMatches = 0;
    private int totalEmployeeMatches = 0;


    public BipartiteGraph(List<Task> tasksToAllocate) {
        for (Task task: (ArrayList<Task>)tasksToAllocate) {
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


                Vertex taskVertex = new Vertex(task.getId(), VertexType.TASK);
                Map<Vertex, Boolean> adjacentVerticesOfTask = new HashMap<>();
                //adds new entries to the adjacency map of both tasks and employees
                for (Employee employee: task.possibleAssignee) {
                    Vertex employeeVertex = new Vertex(employee.getId(), VertexType.EMPLOYEE);
                    //Integer employeeId = employee.getId();
                    Map<Vertex, Boolean> adjacentVerticesOfEmployee = new HashMap<>();

                    if (employeeMap.containsKey(employeeVertex)) {
                        adjacentVerticesOfEmployee = employeeMap.get(employeeVertex);
                    } else {
                        adjacentVerticesOfEmployee = new HashMap<>();
                    }
                    totalEmployeeMatches++;
                    adjacentVerticesOfEmployee.put(taskVertex, false);
                    employeeMap.put(employeeVertex, adjacentVerticesOfEmployee);

                    totalTaskMatches++;
                    adjacentVerticesOfTask.put(employeeVertex, false);
                }

                taskMap.put(taskVertex, adjacentVerticesOfTask);
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

        choosingNextEmployee:
        for (Employee employee: (ArrayList<Employee>) task.possibleAssignee) {
            try {
                if (employee.getTasks()==null || employee.getTasks().size()==0)
                    continue choosingNextEmployee;
                else {
                    for (Task employeeTask : employee.getTasks()) {
                        if (task.timeOverlapWith(employeeTask))
                            task.possibleAssignee.remove(employee);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
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
        taskMap.keySet().forEach(taskVertex ->System.out.println("task " + taskVertex.getVertexId()));

        System.out.println("---------Set of employees---------: ");
        employeeMap.keySet().forEach(employeeVertex ->System.out.println("employee " + employeeVertex.getVertexId()));

        System.out.println("---------Set of all task matches---------: ");
        for (Vertex taskVertex: taskMap.keySet()) {
            System.out.print("task " + taskVertex.getVertexId() + ": ");
            taskMap.get(taskVertex).forEach((vertex, isVisited) -> {
                System.out.print("employee " + (Integer)vertex.getVertexId()+ ", ");
            });
            System.out.println();
        }
        System.out.println("---------Set of all employee matches---------: ");
        for (Vertex employeeVertex: employeeMap.keySet()) {
            System.out.print("employee " + employeeVertex.getVertexId() + ": ");
            employeeMap.get(employeeVertex).forEach((vertex, isVisited) ->  {
                System.out.print("task " + (Integer)vertex.getVertexId()+ ", ");
            });
            System.out.println();
        }
        System.out.println(totalEmployeeMatches==totalTaskMatches);
        System.out.println("==============BIPARTITE GRAPH END==============");
    }

    public Map<Vertex, Map<Vertex, Boolean>> getTaskMap() {
        return taskMap;
    }

    public Map<Vertex, Map<Vertex, Boolean>> getEmployeeMap() {
        return employeeMap;
    }
}
