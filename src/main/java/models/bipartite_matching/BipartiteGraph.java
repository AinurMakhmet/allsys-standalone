package models.bipartite_matching;

import javafx.util.Pair;
import logic.GreedyAlgorithm;
import logic.Strategy;
import models.Employee;
import models.Skill;
import models.SystemData;
import models.Task;
import servers.LocalServer;

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
    private List<Pair<Integer, ArrayList>> listOfAdjacencyLists = new ArrayList<>();
    Strategy strategy;
    Class strategyClass;

    public BipartiteGraph(Class strategyClass, List<Task> tasksToAllocate) {
        this.strategyClass = strategyClass;
        for (Task task: tasksToAllocate) {
            //creates a temporary list of skills and adds list of skills in a task to that list, for future opearations
            ArrayList<Skill> taskSkills = null;
            try {

                taskSkills = new ArrayList<>(task.getSkills());

                // finds the index of the skill that is possessed by smallest amount of employees.
                int indexSmallestSize = getIndexOfDefiningSkill(taskSkills);

                task.possibleAssignee = (ArrayList<Employee>) taskSkills.get(indexSmallestSize).getEmployees();
                //LocalServer.ffLogger.traceln("task" +task);
                taskSkills.remove(indexSmallestSize);

                ArrayList<Employee> definingSkillEmployees = (ArrayList<Employee>) task.getSkills().get(indexSmallestSize).getEmployees();
                filterPossibleAssignee(task, taskSkills, definingSkillEmployees);

                LocalServer.gLogger.trace("number of possible assignee for task {} is {}", task.getName(), task.possibleAssignee.size());
                LocalServer.ffLogger.trace("number of possible assignee for task {} is {}", task.getName(), task.possibleAssignee.size());

                if (task.possibleAssignee!=null && task.possibleAssignee.size()>0) {
                    listOfAdjacencyLists.add(new Pair(task.getId(), task.possibleAssignee));

                    if (!strategyClass.equals(GreedyAlgorithm.class)) {
                        initialiseMaps(task);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void initialiseMaps(Task task) {
        Vertex taskVertex = new Vertex(task.getId(), VertexType.TASK);
        Map<Vertex, Boolean> adjacentVerticesOfTask = new HashMap<>();
        //adds new entries to the adjacency map of both tasks and employees
        task.possibleAssignee.forEach(employee ->  {
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
        });
        taskMap.put(taskVertex, adjacentVerticesOfTask);
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
                        //LocalServer.ffLogger.trace("Employee " + employees.get(i) + " doesn'task have skill " + s.getName());
                        //LocalServer.ffLogger.trace("Employee skill: " + employees.get(i).getSkills());

                        //the employee doesn'task have all the required taskSkills to complete the task,
                        //so it will be removed from the list of candidates
                        task.possibleAssignee.remove(definingSkillEmployees.get(i));
                    }
                }
            }
        }


        if (!strategyClass.equals(GreedyAlgorithm.class)) {
            removeCandiatesWithTimeOverlappingTasks(task);
        }
    }

    private void removeCandiatesWithTimeOverlappingTasks(Task task) {
        Iterator it = task.possibleAssignee.iterator();

        candidateWithTimeOverlappingTasksFiltering:
        while (it.hasNext()) {
            Employee employee = SystemData.getAllEmployeesMap().get(((Employee) it.next()).getId());
            try {
                if ((employee.getTasks()==null || employee.getTasks().size()==0)&& (employee.getMatchedTasks().size()==0)) {
                    continue candidateWithTimeOverlappingTasksFiltering;
                } else {
                    if (!(employee.getTasks() == null || employee.getTasks().size() == 0)) {
                        for (Task employeeTask : employee.getTasks()) {
                            if (task.timeOverlapWith(employeeTask))
                                it.remove();
                        }
                    }
                    if (!(employee.getMatchedTasks() == null || employee.getMatchedTasks().size() == 0)) {
                        for (Task employeeMatchedTask : employee.getMatchedTasks()) {
                            if (task.timeOverlapWith(employeeMatchedTask))
                                it.remove();
                        }
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
        LocalServer.ffLogger.trace("=============================BIPARTITE GRAPH START==============");

        LocalServer.ffLogger.trace("-----------------------------Set of tasks---------: ");
        taskMap.keySet().forEach(taskVertex ->LocalServer.ffLogger.trace("         task {}", taskVertex.getVertexId()));

        LocalServer.ffLogger.trace("-----------------------------Set of employees---------: ");
        employeeMap.keySet().forEach(employeeVertex ->LocalServer.ffLogger.trace("         employee {}", employeeVertex.getVertexId()));

        LocalServer.ffLogger.trace("-----------------------------Set of all task matches---------: ");
        for (Vertex taskVertex: taskMap.keySet()) {
            LocalServer.ffLogger.trace("         task {}: ", taskVertex.getVertexId());
            taskMap.get(taskVertex).forEach((vertex, isVisited) -> {
                LocalServer.ffLogger.trace("                 employee {},", (Integer)vertex.getVertexId());
            });
        }
        LocalServer.ffLogger.trace("------------------------------Set of all employee matches---------: ");
        for (Vertex employeeVertex: employeeMap.keySet()) {
            LocalServer.ffLogger.trace("         employee {}:", employeeVertex.getVertexId());
            employeeMap.get(employeeVertex).forEach((vertex, isVisited) ->  {
                LocalServer.ffLogger.trace("                 task {},", (Integer)vertex.getVertexId());
            });
        }
        assert(totalEmployeeMatches==totalTaskMatches);
        LocalServer.ffLogger.trace("==============================BIPARTITE GRAPH END==============\n");
    }

    public Map<Vertex, Map<Vertex, Boolean>> getTaskMap() {
        return taskMap;
    }

    public Map<Vertex, Map<Vertex, Boolean>> getEmployeeMap() {
        return employeeMap;
    }

    public List<Pair<Integer, ArrayList>> getListOfAdjacencyLists() {
        return listOfAdjacencyLists;
    }

}
