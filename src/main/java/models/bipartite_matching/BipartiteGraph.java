package models.bipartite_matching;

import javafx.util.Pair;
import logic.EdmondsKarpStrategy;
import logic.GreedyStrategy;
import logic.MaximumProfitStrategy;
import models.Employee;
import models.Skill;
import models.SystemData;
import models.Task;
import org.apache.logging.log4j.Logger;
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
    Class strategyClass;
    private Logger logger;

    public BipartiteGraph(Class strategyClass, List<Task> tasksToAllocate) {
        this.strategyClass = strategyClass;
        if (strategyClass.equals(GreedyStrategy.class)) {
            logger = LocalServer.gLogger;
        } else if (strategyClass.equals(EdmondsKarpStrategy.class)) {
            logger = LocalServer.ekLogger;
        } else if (strategyClass.equals(MaximumProfitStrategy.class)) {
            logger = LocalServer.gLogger;
        }
        for (Task task: tasksToAllocate) {
            //creates a temporary list of skills and adds list of skills in a task to that list, for future opearations
            ArrayList<Skill> taskSkills = null;
            try {

                taskSkills = new ArrayList<>(task.getSkills());

                // finds the index of the skill that is possessed by smallest amount of employees.
                int indexOfScarceSkill = getIndexOfScarceSkill(taskSkills);

                task.possibleAssignees = (ArrayList<Employee>) taskSkills.get(indexOfScarceSkill).getEmployees();
                //logger.trace("task" +task);
                taskSkills.remove(indexOfScarceSkill);

                ArrayList<Employee> scarceSkillEmployees = (ArrayList<Employee>) task.getSkills().get(indexOfScarceSkill).getEmployees();
                filterPossibleAssignees(task, taskSkills, scarceSkillEmployees);

                logger.trace("number of possible assignee for task {} is {}", task.getName(), task.possibleAssignees.size());

                if (task.possibleAssignees !=null && task.possibleAssignees.size()>0) {
                    if (strategyClass.equals(GreedyStrategy.class)) {
                        listOfAdjacencyLists.add(new Pair(task.getId(), task.possibleAssignees));
                    } else {
                        createGraphEdgesFor(task);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void createGraphEdgesFor(Task task) {
        Vertex taskVertex = new Vertex(task.getId(), VertexType.TASK);
        Map<Vertex, Boolean> adjacentVerticesOfTask = new HashMap<>();
        //adds new entries to the adjacency map of both tasks and employees
        task.possibleAssignees.forEach(employee ->  {
            Vertex employeeVertex = new Vertex(employee.getId(), VertexType.EMPLOYEE);
            if (strategyClass.equals(MaximumProfitStrategy.class)) {
                employeeVertex.setCost(employee.getDailySalary());
            }
            //Integer employeeId = employee.getId();
            Map<Vertex, Boolean> adjacentVerticesOfEmployee = new HashMap<>();

            if (employeeMap.containsKey(employeeVertex)) {
                adjacentVerticesOfEmployee = employeeMap.get(employeeVertex);
            } else {
                adjacentVerticesOfEmployee = new HashMap<>();
                if (strategyClass.equals(EdmondsKarpStrategy.class)) {
                    adjacentVerticesOfEmployee = new TreeMap(new Vertex.VertexComparator());
                }
            }
            totalEmployeeMatches++;
            adjacentVerticesOfEmployee.put(taskVertex, false);
            employeeMap.put(employeeVertex, adjacentVerticesOfEmployee);

            totalTaskMatches++;
            adjacentVerticesOfTask.put(employeeVertex, false);
        });
        taskMap.put(taskVertex, adjacentVerticesOfTask);
    }

    private void filterPossibleAssignees(Task task, ArrayList<Skill> taskSkills, ArrayList<Employee> scarceSkillEmployees) throws IOException {
        for (Skill s : taskSkills){
            for (int i=0; i<scarceSkillEmployees.size(); i++) {
                ArrayList<Skill> employeeSkills = (ArrayList<Skill>) scarceSkillEmployees.get(i).getSkills();
                for (int j = 0; j< employeeSkills.size(); j++) {
                    if (s.getId()==employeeSkills.get(j).getId())  {
                        break;
                    }
                    else if (j == employeeSkills.size()-1) {
                        //logger.trace("Employee " + employees.get(i) + " doesn'task have skill " + s.getName());
                        //logger.trace("Employee skill: " + employees.get(i).getSkills());

                        //the employee doesn'task have all the required taskSkills to complete the task,
                        //so it will be removed from the list of candidates
                        task.possibleAssignees.remove(scarceSkillEmployees.get(i));
                    }
                }
            }
        }


        if (!strategyClass.equals(GreedyStrategy.class)) {
            removeCandiatesWithTimeOverlappingTasks(task);
        }
    }

    private void removeCandiatesWithTimeOverlappingTasks(Task task) {
        Iterator it = task.possibleAssignees.iterator();

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
                                continue candidateWithTimeOverlappingTasksFiltering;
                        }
                    }
                    if (!(employee.getMatchedTasks() == null || employee.getMatchedTasks().size() == 0)) {
                        for (Task employeeMatchedTask : employee.getMatchedTasks()) {
                            if (task.timeOverlapWith(employeeMatchedTask))
                                it.remove();
                                continue candidateWithTimeOverlappingTasksFiltering;
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    int getIndexOfScarceSkill(ArrayList<Skill> taskSkills) throws IOException {
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
        logger.trace("\n\n=============================BIPARTITE GRAPH START==============");

        logger.trace("-----------------------------Set of tasks---------: ");
        taskMap.keySet().forEach(taskVertex ->logger.trace("\t\ttask {}", taskVertex.getVertexId()));

        logger.trace("-----------------------------Set of employees---------: ");
        employeeMap.keySet().forEach(employeeVertex ->logger.trace("\t\temployee {}", employeeVertex.getVertexId()));

        logger.trace("-----------------------------Set of all task matches---------: ");
        for (Vertex taskVertex: taskMap.keySet()) {
            logger.trace("\t\ttask {}: ", taskVertex.getVertexId());
            taskMap.get(taskVertex).forEach((vertex, isVisited) -> {
                logger.trace("\t\t\temployee {},", (Integer)vertex.getVertexId());
            });
        }
        logger.trace("------------------------------Set of all employee matches---------: ");
        for (Vertex employeeVertex: employeeMap.keySet()) {
            logger.trace("\t\temployee {}:", employeeVertex.getVertexId());
            employeeMap.get(employeeVertex).forEach((vertex, isVisited) ->  {
                logger.trace("\t\t\ttask {},", (Integer)vertex.getVertexId());
            });
        }
        assert(totalEmployeeMatches==totalTaskMatches);
        logger.trace("==============================BIPARTITE GRAPH END==============\n");
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
